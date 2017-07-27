package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DAO class, that executes crud operations over IssueTransition entity
 */
public class IssueTransitionDAO {

    private ActiveObjects ao;

    public IssueTransitionDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer saveIssueTransition(UnsynchronizedIssue unsynchronizedIssue, IssueTransitionBean issueTransitionBean) {
        IssueTransition issueTransition = ao.create(IssueTransition.class,
                new DBParam("CREATION_DATE", new Date()), new DBParam("FIELD_ID", "-"),
                new DBParam("OLD_VALUE", "-"), new DBParam("NEW_VALUE", "-"),
                new DBParam("PROCESSING_STATUS", 0), new DBParam("SEND_TYPE", 2),
                new DBParam("SUCCESSFUL", false),
                new DBParam("LAST_PROCESSOR", "-"), new DBParam("PROCESSING_USER", "-"));
        issueTransition.setCreationDate(new Date());
        issueTransition.setUnsynchronizedIssue(unsynchronizedIssue);
        issueTransition.setFieldId(issueTransitionBean.getFieldId());
        issueTransition.setProcessingUser(issueTransitionBean.getProcessingUser());
        issueTransition.setOldValue(issueTransitionBean.getOldValue());
        issueTransition.setNewValue(issueTransitionBean.getNewValue());
        issueTransition.setProcessingStatus(issueTransitionBean.getProcessingStatus());
        issueTransition.setSendType(issueTransitionBean.getSendType());
        return EntityManager.saveEntity(ao, issueTransition);
    }

    public IssueTransition getIssueTransition(Integer id) {
        return EntityManager.getEntity(ao, IssueTransition.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(IssueTransition.class, "IT").
                                where("IT.ID = ?", id);
            }
        });
    }

    public int setIssueTransitionToSynchronized(IssueTransition issueTransition, String username) {
        issueTransition.setSynchronizationDate(new Date());
        issueTransition.setSuccessful(true);
        issueTransition.setProcessingStatus(ProcessingStatus.Success.getValue());
        issueTransition.setLastProcessor(username);
        issueTransition.setLastProcessingDate(new Date());
        return EntityManager.saveEntity(ao, issueTransition);
    }

    public int updateIssueTransition(IssueTransition issueTransition) {
        return EntityManager.saveEntity(ao, issueTransition);
    }

    public boolean deleteAllSuccessfulEntries(String solManCustGuid) {
         return EntityManager.deleteEntity(ao, IssueTransition.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(IssueTransition.class, "IT")
                        .alias(UnsynchronizedIssue.class, "UI")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(UnsynchronizedIssue.class, "IT.UNSYNCHRONIZED_ISSUE_ID = UI.ID")
                        .join(SolmanParamsAO.class, "UI.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? and IT.SUCCESSFUL = true", solManCustGuid);
            }
        });
    }

    public boolean isIssueUnsynchronizedCommError(Long issueId) {
        return EntityManager.getEntity(ao, IssueTransition.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID").
                alias(IssueTransition.class, "IT").
                alias(UnsynchronizedIssue.class, "UI").
                join(UnsynchronizedIssue.class, "IT.UNSYNCHRONIZED_ISSUE_ID = UI.ID").
                where("UI.ISSUE_ID = ? and IT.PROCESSING_STATUS = ? and IT.SUCCESSFUL = ?", issueId, ProcessingStatus.CommError.getValue(), false);
            }
        }) != null;
    }

    public boolean isIssueUnsynchronizedAppError(Long issueId) {
        return EntityManager.getEntity(ao, IssueTransition.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID").
                        alias(IssueTransition.class, "IT").
                        alias(UnsynchronizedIssue.class, "UI").
                        join(UnsynchronizedIssue.class, "IT.UNSYNCHRONIZED_ISSUE_ID = UI.ID").
                        where("UI.ISSUE_ID = ? and IT.PROCESSING_STATUS = ? and IT.SUCCESSFUL = ?", issueId, ProcessingStatus.AppError.getValue(), false);
            }
        }) != null;
    }

    public boolean existsOlderUnsynchronizedIssue(Integer id, Long issueId) {
        return !EntityManager.getEntities(ao, IssueTransition.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, CREATION_DATE, FIELD_ID, OLD_VALUE, NEW_VALUE, PROCESSING_STATUS, SEND_TYPE, LAST_PROCESSING_DATE, SUCCESSFUL, SYNCHRONIZATION_DATE, LAST_PROCESSOR, PROCESSING_USER")
                        .alias(IssueTransition.class, "IT")
                        .alias(UnsynchronizedIssue.class, "UI")
                        .join(UnsynchronizedIssue.class, "IT.UNSYNCHRONIZED_ISSUE_ID = UI.ID")
                        .where("UI.ISSUE_ID = ? and IT.SUCCESSFUL = ? and IT.ID < ?", issueId, false, id);
            }
        }).isEmpty();
    }

    public List<IssueTransition> getUnsynchronizedIssues(Integer unsynchronizedIssueID) {
        return EntityManager.getEntities(ao, IssueTransition.class, new Function0<Query>() {
            @Override
            public Query apply() {
                Query query = Query.select("ID, CREATION_DATE, FIELD_ID, OLD_VALUE, NEW_VALUE, PROCESSING_STATUS, SEND_TYPE, LAST_PROCESSING_DATE, SUCCESSFUL, SYNCHRONIZATION_DATE, LAST_PROCESSOR, PROCESSING_USER, UNSYNCHRONIZED_ISSUE_ID")
                        .alias(IssueTransition.class, "IT")
                        .alias(UnsynchronizedIssue.class, "UI")
                        .join(UnsynchronizedIssue.class, "IT.UNSYNCHRONIZED_ISSUE_ID = UI.ID")
                        .order("IT.ID ASC")
                        .where("IT.UNSYNCHRONIZED_ISSUE_ID = ? and IT.SUCCESSFUL = false ", unsynchronizedIssueID);
                return query;
            }
        });
    }

    public List<IssueTransition> getIssueTransitions(String solManCustGuid, Integer offset, Integer limit, String issueKey, Date creationDate, String orderBy, String direction) {
        Map<Integer, UnsynchronizedIssue> unsynchronizedIssueMap = EntityManager.getEntitiesAsMap(ao, UnsynchronizedIssue.class, new Function0<Query>() {
            @Override
            public Query apply() {
                Query query = Query.select("ID, ISSUE_ID, ISSUE_KEY")
                        .alias(UnsynchronizedIssue.class, "UI")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "UI.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? ", solManCustGuid);
                return query;
            }
        });

        IssueTransition[] issueTransitions = EntityManager.getEntitiesAsArray(ao, IssueTransition.class, new Function0<Query>() {
            @Override
            public Query apply() {
                Query query = Query.select("ID, CREATION_DATE, FIELD_ID, OLD_VALUE, NEW_VALUE, PROCESSING_STATUS, SEND_TYPE, LAST_PROCESSING_DATE, SUCCESSFUL, SYNCHRONIZATION_DATE, LAST_PROCESSOR, PROCESSING_USER, UNSYNCHRONIZED_ISSUE_ID")
                        .alias(IssueTransition.class, "IT")
                        .alias(UnsynchronizedIssue.class, "UI")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(UnsynchronizedIssue.class, "UNSYNCHRONIZED_ISSUE_ID = UI.ID")
                        .join(SolmanParamsAO.class, "UI.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? ", solManCustGuid);

                if (StringUtils.isNotEmpty(issueKey)) {
                    query.setWhereClause(query.getWhereClause() + " and UI.ISSUE_KEY like '%" + issueKey + "%'");
                }
                if (creationDate != null) {
                    query = query.where("IT.CREATION_DATE between ? and ?", creationDate, DateUtils.addDays(creationDate, 1));
                }
                if (offset != null) {
                    query.offset(offset);
                }
                if (limit != null) {
                    query.limit(limit);
                }
                if (orderBy != null) {
                    query.setOrderClause(getOrderByClause(orderBy, direction));
                }
                return query;
            }
        });

        for (IssueTransition issueTransition : issueTransitions) {
            issueTransition.setUnsynchronizedIssue(unsynchronizedIssueMap.get(issueTransition.getUnsynchronizedIssue().getID()));
        }

        return Arrays.asList(issueTransitions);
    }

    private String getOrderByClause(String orderBy, String directon) {
        switch(orderBy) {
            case "issueKey" : return "UI.ISSUE_KEY " + directon;
            case "creationDate" : return "IT.CREATION_DATE " + directon;
            case "processingStatus" : return "IT.PROCESSING_STATUS " + directon;
            case "sendType" : return "IT.SEND_TYPE " + directon;
            case "sendDetails" : return "IT.NEW_VALUE " + directon;
            case "lastProcessor" : return "IT.LAST_PROCESSOR " + directon;
            case "successful" : return "IT.SUCCESSFUL " + directon;
            case "synchronizationDate" : return "IT.SYNCHRONIZATION_DATE " + directon;
            case "lastProcessingDate" : return "IT.LAST_PROCESSING_DATE " + directon;
        }
        return null;
    }

    public Boolean deleteIssueTransitions(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, IssueTransition.class, new Function0<Query>() {
            @Override
            public Query apply() {
                Query query = Query.select("ID, CREATION_DATE, FIELD_ID, OLD_VALUE, NEW_VALUE, PROCESSING_STATUS, SEND_TYPE, LAST_PROCESSING_DATE, SUCCESSFUL, SYNCHRONIZATION_DATE, LAST_PROCESSOR, PROCESSING_USER, UNSYNCHRONIZED_ISSUE_ID")
                        .alias(IssueTransition.class, "IT")
                        .alias(UnsynchronizedIssue.class, "UI")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(UnsynchronizedIssue.class, "UNSYNCHRONIZED_ISSUE_ID = UI.ID")
                        .join(SolmanParamsAO.class, "UI.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? ", solManCustGuid);
                return query;
            }
        });
    }
}
