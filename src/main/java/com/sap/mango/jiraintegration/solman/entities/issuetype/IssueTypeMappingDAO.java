package com.sap.mango.jiraintegration.solman.entities.issuetype;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.List;

/**
 * IssueTypeMappingDAO
 *
 */
public class IssueTypeMappingDAO {

    private ActiveObjects ao;

    public IssueTypeMappingDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer createIssueTypeMapping(SolmanParamsAO solmanParams, Integer issueType, String solManProcessType) {
        IssueTypeMapping issueTypeMapping = getIssueTypeMappingMut(solmanParams, solManProcessType);
        if (issueTypeMapping == null){
            issueTypeMapping = ao.create(IssueTypeMapping.class,
                    new DBParam("SOLMAN_PROCESS_TYPE", solManProcessType),
                    new DBParam("JIRA_ISSUE_TYPE", issueType),
                    new DBParam("SOLMAN_PARAMS_ID", solmanParams.getID())
            );
        }else{
            issueTypeMapping.setJiraIssueType(issueType);
            issueTypeMapping.setSolmanProcessType(solManProcessType);
        }
        return EntityManager.saveEntity(ao, issueTypeMapping);
    }

    private IssueTypeMapping getIssueTypeMappingMut(SolmanParamsAO solmanParams, String solManProcessType) {
        IssueTypeMapping issueTypeMapping = null;

        final IssueTypeMapping[] issueTypeMappings = ao.find(IssueTypeMapping.class, getSelQuery(solmanParams.getCustomerGiud(), solManProcessType));
        for (IssueTypeMapping typeMapping : issueTypeMappings) {
            issueTypeMapping = typeMapping;
        }
        return issueTypeMapping;
    }

    private static Query getSelQuery(String solManCustGuiD, String solManProcessType) {
        return Query.select("ID, SOLMAN_PROCESS_TYPE, JIRA_ISSUE_TYPE")
                .alias(IssueTypeMapping.class, "IM")
                .alias(SolmanParamsAO.class, "SM")
                .join(SolmanParamsAO.class, "IM.SOLMAN_PARAMS_ID = SM.ID")
                .where("SM.CUSTOMER_GIUD = ? and IM.SOLMAN_PROCESS_TYPE = ?", solManCustGuiD, solManProcessType);
    }

    public List<IssueTypeMapping> getIssueTypeMappings() {
        return Arrays.asList(EntityManager.getEntities(ao, IssueTypeMapping.class));
    }

    public IssueTypeMapping getIssueTypeMapping(final Integer id) {
        return EntityManager.getReadOnlyEntity(ao, IssueTypeMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public IssueTypeMapping getIssueTypeMapping(final String solManCustGuid, final String solManProcessType) {
        return EntityManager.getReadOnlyEntity(ao, IssueTypeMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType);            }
        });
    }

    public boolean deleteIssueTypeMapping(final Integer id) {
        return EntityManager.deleteEntity(ao, IssueTypeMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public void updateIssueTypeMapping(final IssueTypeMapping issueTypeMapping) {
        EntityManager.saveUpdateEntity(ao, issueTypeMapping, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", issueTypeMapping.getID());
            }
        });
    }

    public List<IssueTypeMapping> getIssueTypeMappings(final String solManCustGuid) {
        return EntityManager.getEntities(ao, IssueTypeMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PROCESS_TYPE, JIRA_ISSUE_TYPE")
                        .alias(IssueTypeMapping.class, "IM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "IM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public Boolean deleteIssueTypeMappings(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, IssueTypeMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(IssueTypeMapping.class, "IM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "IM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public List<IssueTypeMapping> getIssueTypeMappings(final String solManCustGuid, final String solManProcessType) {
        return EntityManager.getEntities(ao, IssueTypeMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return getSelQuery(solManCustGuid, solManProcessType);
            }
        });
    }
}
