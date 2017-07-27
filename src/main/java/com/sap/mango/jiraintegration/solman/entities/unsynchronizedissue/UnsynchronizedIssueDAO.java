package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMapping;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.List;

/**
 * DAO class, that executes crud operations over UnsynchronizedIssue entity
 */
public class UnsynchronizedIssueDAO {

    private ActiveObjects ao;

    public UnsynchronizedIssueDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer saveUnsychronizedIssue(UnsynchronizedIssueBean unsynchronizedIssueBean) {
        UnsynchronizedIssue unsynchronizedIssue = ao.create(UnsynchronizedIssue.class,
                new DBParam("SOLMAN_PARAMS_ID", unsynchronizedIssueBean.getSolManParamsId()),
                new DBParam("ISSUE_ID", 0l), new DBParam("ISSUE_KEY", "-"));
        unsynchronizedIssue.setIssueId(unsynchronizedIssueBean.getIssueId());
        unsynchronizedIssue.setIssueKey(unsynchronizedIssueBean.getIssueKey());
        return EntityManager.saveEntity(ao, unsynchronizedIssue);
    }


    public UnsynchronizedIssue getUnsynchronizedIssue(Long issueId) {
        return EntityManager.getEntity(ao, UnsynchronizedIssue.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(UnsynchronizedIssue.class, "UI").
                                where("UI.ISSUE_ID = ?", issueId);
            }
        });
    }

    public List<UnsynchronizedIssue> getUnsynchronizedIssues(String solManCustGuid) {
        return EntityManager.getEntities(ao, UnsynchronizedIssue.class, new Function0<Query>() {
            @Override
            public Query apply() {
                Query query = Query.select("ID, ISSUE_ID, ISSUE_KEY")
                        .alias(UnsynchronizedIssue.class, "UI")
                        .alias(SolmanParamsAO.class, "SM")
                        .alias(IssueTransition.class, "IT")
                        .join(SolmanParamsAO.class, "UI.SOLMAN_PARAMS_ID = SM.ID")
                        .join(IssueTransition.class, "UI.ID = IT.UNSYNCHRONIZED_ISSUE_ID")
                        .distinct()
                        .order("UI.ID ASC")
                        .where("SM.CUSTOMER_GIUD = ? and IT.SUCCESSFUL = false", solManCustGuid);
                return query;
            }
        });
    }

    public Boolean deleteUnsynchronizedIssues(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, UnsynchronizedIssue.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(UnsynchronizedIssue.class, "UI")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "UI.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

}
