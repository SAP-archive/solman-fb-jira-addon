package com.sap.mango.jiraintegration.solman.entities.appointmentmapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.List;

/**
 * AppointmentMappingDAO
 *
 */
public class AppointmentMappingDAO {

    private ActiveObjects ao;

    public AppointmentMappingDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer createFieldMapping(SolmanParamsAO solmanParams, AppointmentMappingBean fieldMappingBean) {
        AppointmentMapping fieldMapping = getFieldMappingMut(solmanParams, fieldMappingBean.getSolmanProcessType(), fieldMappingBean.getSolmanAppointment());
        if (fieldMapping == null){
            fieldMapping = ao.create(AppointmentMapping.class,
                    new DBParam("SOLMAN_PROCESS_TYPE", fieldMappingBean.getSolmanProcessType()),
                    new DBParam("SOLMAN_APPOINTMENT", fieldMappingBean.getSolmanAppointment()),
                    new DBParam("JIRA_FIELD", fieldMappingBean.getJiraField()),
                    new DBParam("SOLMAN_PARAMS_ID", solmanParams.getID())
            );
        }else{
            fieldMapping.setSolmanAppointment(fieldMappingBean.getSolmanAppointment());
            fieldMapping.setJiraField(fieldMappingBean.getJiraField());
        }
        return EntityManager.saveEntity(ao, fieldMapping);
    }

    private AppointmentMapping getFieldMappingMut(SolmanParamsAO solmanParams, String solManProcessType, String solManField) {
        AppointmentMapping fieldMapping = null;

        final AppointmentMapping[] fieldMappings = ao.find(AppointmentMapping.class, getSelQuery(solmanParams.getCustomerGiud(), solManProcessType, solManField));
        for (AppointmentMapping mapping : fieldMappings) {
            fieldMapping = mapping;
        }

        return fieldMapping;
    }

    private static Query getSelQuery(String solManCustGuiD, String solManProcessType, String solManField) {
        return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_APPOINTMENT, JIRA_FIELD")
                .alias(AppointmentMapping.class, "FM")
                .alias(SolmanParamsAO.class, "SM")
                .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                .where("SM.CUSTOMER_GIUD = ? AND FM.SOLMAN_PROCESS_TYPE = ? AND FM.SOLMAN_APPOINTMENT = ?", solManCustGuiD, solManProcessType, solManField);
    }

    public List<AppointmentMapping> getFieldMappings() {
        return Arrays.asList(EntityManager.getEntities(ao, AppointmentMapping.class));
    }

    public AppointmentMapping getFieldMapping(final Integer id) {
        return EntityManager.getReadOnlyEntity(ao, AppointmentMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public AppointmentMapping getFieldMapping(final String solManCustGuid, final String solManProcessType, final String solManField) {
        return EntityManager.getReadOnlyEntity(ao, AppointmentMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType, solManField);
            }
        });
    }

    public boolean deleteFieldMapping(final Integer id) {
        return EntityManager.deleteEntity(ao, AppointmentMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public void updateFieldMapping(final AppointmentMapping fieldMapping) {
        EntityManager.saveUpdateEntity(ao, fieldMapping, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", fieldMapping.getID());
            }
        });
    }

    public List<AppointmentMapping> getFieldMappings(final String solManCustGuid) {
        return EntityManager.getEntities(ao, AppointmentMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PROCESS_TYPE, SOLMAN_APPOINTMENT, JIRA_FIELD")
                        .alias(AppointmentMapping.class, "FM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public Boolean deleteEntities(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, AppointmentMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(AppointmentMapping.class, "FM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public List<AppointmentMapping> getFieldMappings(final String solManCustGuid, final String solManProcessType, final String solManField) {
        return EntityManager.getEntities(ao, AppointmentMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType, solManField);
            }
        });
    }
}
