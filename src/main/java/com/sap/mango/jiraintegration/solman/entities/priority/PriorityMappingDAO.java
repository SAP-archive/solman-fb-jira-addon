package com.sap.mango.jiraintegration.solman.entities.priority;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.List;

/**
 * DAO class, used for CRUD operations over PriorityMappings
 */
public class PriorityMappingDAO {

    private ActiveObjects ao;

    public PriorityMappingDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer saveUpdatePriorityMapping(SolmanParamsAO solmanParams, String solmanPriority, String jiraPriority) {
        PriorityMapping priorityMapping = ao.create(PriorityMapping.class, new DBParam("SOLMAN_PRIORITY", "-"), new DBParam("JIRA_PRIORITY", "-"));
        priorityMapping.setSolmanParams(solmanParams);
        priorityMapping.setSolmanPriority(solmanPriority);
        priorityMapping.setJiraPriority(jiraPriority);
        return EntityManager.saveUpdateEntity(ao, priorityMapping, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(PriorityMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? and ( PM.SOLMAN_PRIORITY = ? or PM.JIRA_PRIORITY = ?)", solmanParams.getCustomerGiud(), solmanPriority, jiraPriority);
            }
        }).getID();
    }

    public List<PriorityMapping> getPriorityMappings(final String solManCustGuid) {
        return EntityManager.getEntities(ao, PriorityMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PRIORITY, JIRA_PRIORITY")
                        .alias(PriorityMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public PriorityMapping getPriorityMapping(final String solManCustGuid, String solmanPriority) {
        return EntityManager.getEntity(ao, PriorityMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(PriorityMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? and PM.SOLMAN_PRIORITY = ?", solManCustGuid, solmanPriority);
            }
        });
    }

    public boolean deletePriorityMapping(final Integer id) {
        return EntityManager.deleteEntity(ao, PriorityMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public Boolean deletePriorityMappings(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, PriorityMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(PriorityMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

}
