package com.sap.mango.jiraintegration.solman.entities.fieldmapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.List;

/**
 * FieldMappingDAO
 *
 */
public class FieldMappingDAO {

    private ActiveObjects ao;

    public FieldMappingDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer createFieldMapping(SolmanParamsAO solmanParams, FieldMappingBean fieldMappingBean) {
        FieldMapping fieldMapping = getFieldMappingMut(solmanParams, fieldMappingBean.getSolmanProcessType(), fieldMappingBean.getSolmanField());
        if (fieldMapping == null){
            fieldMapping = ao.create(FieldMapping.class,
                    new DBParam("SOLMAN_PROCESS_TYPE", fieldMappingBean.getSolmanProcessType()),
                    new DBParam("SOLMAN_FIELD", fieldMappingBean.getSolmanField()),
                    new DBParam("JIRA_FIELD", fieldMappingBean.getJiraField()),
                    new DBParam("SOLMAN_PARAMS_ID", solmanParams.getID())
            );
        }else{
            fieldMapping.setSolmanField(fieldMappingBean.getSolmanField());
            fieldMapping.setJiraField(fieldMappingBean.getJiraField());
        }
        return EntityManager.saveEntity(ao, fieldMapping);
    }


    private FieldMapping getFieldMappingMut(SolmanParamsAO solmanParams, String solManProcessType, String solManField) {
        FieldMapping fieldMapping = null;

        final FieldMapping[] fieldMappings = ao.find(FieldMapping.class, getSelQuery(solmanParams.getCustomerGiud(), solManProcessType, solManField));
        for (FieldMapping mapping : fieldMappings) {
            fieldMapping = mapping;
        }

        return fieldMapping;
    }


    private static Query getSelQuery(String solManCustGuiD, String solManProcessType, String solManField) {
        return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_FIELD, JIRA_FIELD")
                .alias(FieldMapping.class, "FM")
                .alias(SolmanParamsAO.class, "SM")
                .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                .where("SM.CUSTOMER_GIUD = ? AND FM.SOLMAN_PROCESS_TYPE = ? AND FM.SOLMAN_FIELD = ?", solManCustGuiD, solManProcessType, solManField);
    }



    public List<FieldMapping> getFieldMappings() {
        return Arrays.asList(EntityManager.getEntities(ao, FieldMapping.class));
    }

    public FieldMapping getFieldMapping(final Integer id) {
        return EntityManager.getReadOnlyEntity(ao, FieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public FieldMapping getFieldMapping(final String solManCustGuid, final String solManProcessType, final String solManField) {
        return EntityManager.getReadOnlyEntity(ao, FieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType, solManField);
            }
        });
    }

    public boolean deleteFieldMapping(final Integer id) {
        return EntityManager.deleteEntity(ao, FieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public void updateFieldMapping(final FieldMapping fieldMapping) {
        EntityManager.saveUpdateEntity(ao, fieldMapping, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", fieldMapping.getID());
            }
        });
    }

    public List<FieldMapping> getFieldMappings(final String solManCustGuid) {
        return EntityManager.getEntities(ao, FieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_FIELD, JIRA_FIELD")
                        .alias(FieldMapping.class, "FM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public Boolean deleteFieldMappings(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, FieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(FieldMapping.class, "FM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public List<FieldMapping> getFieldMappings(final String solManCustGuid, final String solManProcessType, final String solManField) {
        return EntityManager.getEntities(ao, FieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType, solManField);
            }
        });
    }
}
