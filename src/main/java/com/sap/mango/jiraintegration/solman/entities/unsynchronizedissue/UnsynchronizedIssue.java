package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.Preload;

/**
 * Entity, that stores unsynchronized issue field (Jira - SolMan synchronization)
 */
@Preload("ISSUE_ID, ISSUE_KEY")
public interface
UnsynchronizedIssue extends Entity{

    SolmanParamsAO getSolmanParams();
    void setSolmanParams(SolmanParamsAO solmanParams);

    void setIssueId(Long issueId);
    Long getIssueId();

    String getIssueKey();
    void setIssueKey(String issueKey);

    @OneToMany
    IssueTransition[] getIssueTransitions();
}
