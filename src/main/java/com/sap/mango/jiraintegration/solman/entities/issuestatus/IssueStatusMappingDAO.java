package com.sap.mango.jiraintegration.solman.entities.issuestatus;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.List;

/**
 * DAO class, used to store, retrieve IssueStatusMapping-s
 */
public class IssueStatusMappingDAO {

    private ActiveObjects ao;

    public IssueStatusMappingDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer saveIssueStatusMapping(final SolmanParamsAO solmanParamsAO, final String solmanProcessType, final String solmanStatus, final String jiraTransition) {
        IssueStatusMapping issueTypeMapping = ao.create(IssueStatusMapping.class,
                new DBParam("SOLMAN_PROCESS_TYPE", solmanProcessType),
                new DBParam("SOLMAN_STATUS", solmanStatus),
                new DBParam("JIRA_TRANSITION", jiraTransition));
        issueTypeMapping.setSolmanParams(solmanParamsAO);

        return EntityManager.saveUpdateEntity(ao, issueTypeMapping, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(IssueStatusMapping.class, "ISM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "ISM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? and ( ISM.SOLMAN_PROCESS_TYPE = ? and ISM.SOLMAN_STATUS = ?)", solmanParamsAO.getCustomerGiud(), solmanProcessType, solmanStatus);

            }
        }).getID();
    }

    public boolean deleteIssueStatusMapping(final Integer id) {
        return EntityManager.deleteEntity(ao, IssueStatusMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public IssueStatusMapping getIssueStatusMapping(final String solManCustGuid, final String solManProcessType, final String solManStatus) {
        return EntityManager.getReadOnlyEntity(ao, IssueStatusMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_STATUS, JIRA_TRANSITION")
                        .alias(IssueStatusMapping.class, "ISM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "ISM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? and ISM.SOLMAN_PROCESS_TYPE = ? and ISM.SOLMAN_STATUS = ?", solManCustGuid, solManProcessType, solManStatus);
            }
        });
    }

    public List<IssueStatusMapping> getIssueStatusMappings(final String solManCustGuid) {
        return EntityManager.getEntities(ao, IssueStatusMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_STATUS, JIRA_TRANSITION")
                        .alias(IssueStatusMapping.class, "ISM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "ISM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public Boolean deleteIssueStatusMappings(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, IssueStatusMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(IssueStatusMapping.class, "ISM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "ISM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }


    public IssueStatusMapping getIssueStatusMapping(final Integer id) {
        return EntityManager.getReadOnlyEntity(ao, IssueStatusMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public boolean deleteIssueStatusMapping(final String solManCustGuiD, final String id) {
        return EntityManager.deleteEntity(ao, IssueStatusMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(IssueStatusMapping.class, "ISM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "ISM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("ISM.ID = ? and SM.CUSTOMER_GIUD = ?", id, solManCustGuiD);
            }
        });
    }
}
