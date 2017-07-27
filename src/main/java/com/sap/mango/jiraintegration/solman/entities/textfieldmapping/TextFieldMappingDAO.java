package com.sap.mango.jiraintegration.solman.entities.textfieldmapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.List;

/**
 * TextFieldMappingDAO
 *
 */
public class TextFieldMappingDAO {

    private ActiveObjects ao;

    public TextFieldMappingDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer createFieldMapping(SolmanParamsAO solmanParams, TextFieldMappingBean fieldMappingBean) {
        TextFieldMapping fieldMapping = getFieldMappingMut(solmanParams, fieldMappingBean.getSolmanProcessType(), fieldMappingBean.getSolmanTextType());
        if (fieldMapping == null){
            fieldMapping = ao.create(TextFieldMapping.class,
                    new DBParam("SOLMAN_PROCESS_TYPE", fieldMappingBean.getSolmanProcessType()),
                    new DBParam("SOLMAN_TEXT_TYPE", fieldMappingBean.getSolmanTextType()),
                    new DBParam("JIRA_FIELD", fieldMappingBean.getJiraField()),
                    new DBParam("SOLMAN_PARAMS_ID", solmanParams.getID())
            );
        }else{
            fieldMapping.setSolmanTextType(fieldMappingBean.getSolmanTextType());
            fieldMapping.setJiraField(fieldMappingBean.getJiraField());
        }
        return EntityManager.saveEntity(ao, fieldMapping);
    }


    private TextFieldMapping getFieldMappingMut(SolmanParamsAO solmanParams, String solManProcessType, String solManField) {
        TextFieldMapping fieldMapping = null;

        final TextFieldMapping[] fieldMappings = ao.find(TextFieldMapping.class, getSelQuery(solmanParams.getCustomerGiud(), solManProcessType, solManField));
        for (TextFieldMapping mapping : fieldMappings) {
            fieldMapping = mapping;
        }

        return fieldMapping;
    }


    private static Query getSelQuery(String solManCustGuiD, String solManProcessType, String solManField) {
        return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_TEXT_TYPE, JIRA_FIELD")
                .alias(TextFieldMapping.class, "FM")
                .alias(SolmanParamsAO.class, "SM")
                .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                .where("SM.CUSTOMER_GIUD = ? AND FM.SOLMAN_PROCESS_TYPE = ? AND FM.SOLMAN_TEXT_TYPE = ?", solManCustGuiD, solManProcessType, solManField);
    }



    public List<TextFieldMapping> getFieldMappings() {
        return Arrays.asList(EntityManager.getEntities(ao, TextFieldMapping.class));
    }

    public TextFieldMapping getFieldMapping(final Integer id) {
        return EntityManager.getReadOnlyEntity(ao, TextFieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public TextFieldMapping getFieldMapping(final String solManCustGuid, final String solManProcessType, final String solManField) {
        return EntityManager.getReadOnlyEntity(ao, TextFieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType, solManField);
            }
        });
    }

    public boolean deleteFieldMapping(final Integer id) {
        return EntityManager.deleteEntity(ao, TextFieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public void updateFieldMapping(final TextFieldMapping fieldMapping) {
        EntityManager.saveUpdateEntity(ao, fieldMapping, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", fieldMapping.getID());
            }
        });
    }

    public List<TextFieldMapping> getFieldMappings(final String solManCustGuid) {
        return EntityManager.getEntities(ao, TextFieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_TEXT_TYPE, JIRA_FIELD")
                        .alias(TextFieldMapping.class, "FM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public Boolean deleteFieldMappings(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, TextFieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(TextFieldMapping.class, "FM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public List<TextFieldMapping> getFieldMappings(final String solManCustGuid, final String solManProcessType, final String solManField) {
        return EntityManager.getEntities(ao, TextFieldMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType, solManField);
            }
        });
    }
}
