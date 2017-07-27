package com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.List;

/**
 * PartnerFieldMappingDAO
 *
 */
public class PartnerFieldMappingDAO {

    private ActiveObjects ao;


    public PartnerFieldMappingDAO(ActiveObjects ao) {
        this.ao = ao;
    }


    public Integer createFieldMapping(SolmanParamsAO solmanParams, PartnerFieldMappingBean fieldMappingBean) {
        PartnerFldMap fieldMapping = getFieldMappingMut(solmanParams, fieldMappingBean.getSolmanProcessType(), fieldMappingBean.getSolmanPartnerFunction(), fieldMappingBean.getSolmanField());
        if (fieldMapping == null){
            fieldMapping = ao.create(PartnerFldMap.class,
                    new DBParam("SOLMAN_PROCESS_TYPE", fieldMappingBean.getSolmanProcessType()),
                    new DBParam("SOLMAN_PARTNER_FUNCTION", fieldMappingBean.getSolmanPartnerFunction()),
                    new DBParam("SOLMAN_FIELD", fieldMappingBean.getSolmanField()),
                    new DBParam("JIRA_FIELD", fieldMappingBean.getJiraField()),
                    new DBParam("SOLMAN_PARAMS_ID", solmanParams.getID())
            );
        }else{
            fieldMapping.setSolmanPartnerFunction(fieldMappingBean.getSolmanPartnerFunction());
            fieldMapping.setSolmanField(fieldMappingBean.getSolmanField());
            fieldMapping.setJiraField(fieldMappingBean.getJiraField());
        }
        return EntityManager.saveEntity(ao, fieldMapping);
    }


    private PartnerFldMap getFieldMappingMut(SolmanParamsAO solmanParams, String solManProcessType, String solManPartnerFunction, String solManField) {
        PartnerFldMap fieldMapping = null;

        final PartnerFldMap[] fieldMappings = ao.find(PartnerFldMap.class, getSelQuery(solmanParams.getCustomerGiud(), solManProcessType, solManPartnerFunction, solManField));
        for (PartnerFldMap mapping : fieldMappings) {
            fieldMapping = mapping;
        }

        return fieldMapping;
    }


    private static Query getSelQuery(String solManCustGuiD, String solManProcessType, String solManPartnerFunction, String solManField) {
        return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_PARTNER_FUNCTION, SOLMAN_FIELD, JIRA_FIELD")
                .alias(PartnerFldMap.class, "FM")
                .alias(SolmanParamsAO.class, "SM")
                .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                .where("SM.CUSTOMER_GIUD = ? AND FM.SOLMAN_PROCESS_TYPE = ? AND FM.SOLMAN_PARTNER_FUNCTION = ? AND FM.SOLMAN_FIELD = ?", solManCustGuiD, solManProcessType, solManPartnerFunction, solManField);
    }



    public List<PartnerFldMap> getFieldMappings() {
        return Arrays.asList(EntityManager.getEntities(ao, PartnerFldMap.class));
    }

    public PartnerFldMap getFieldMapping(final Integer id) {
        return EntityManager.getReadOnlyEntity(ao, PartnerFldMap.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public PartnerFldMap getFieldMapping(final String solManCustGuid, final String solManProcessType, final String solManPartnerFunction, final String solManField) {
        return EntityManager.getReadOnlyEntity(ao, PartnerFldMap.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType, solManPartnerFunction, solManField);
            }
        });
    }

    public boolean deleteFieldMapping(final Integer id) {
        return EntityManager.deleteEntity(ao, PartnerFldMap.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public void updateFieldMapping(final PartnerFldMap fieldMapping) {
        EntityManager.saveUpdateEntity(ao, fieldMapping, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", fieldMapping.getID());
            }
        });
    }

    public List<PartnerFldMap> getFieldMappings(final String solManCustGuid) {
        return EntityManager.getEntities(ao, PartnerFldMap.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_PARTNER_FUNCTION, SOLMAN_FIELD, JIRA_FIELD")
                        .alias(PartnerFldMap.class, "FM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public Boolean deleteFieldMappings(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, PartnerFldMap.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(PartnerFldMap.class, "FM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public List<PartnerFldMap> getFieldMappings(final String solManCustGuid, final String solManProcessType, final String solManPartnerFunction, final String solManField) {
        return EntityManager.getEntities(ao, PartnerFldMap.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType, solManPartnerFunction, solManField);
            }
        });
    }
}
