package com.sap.mango.jiraintegration.solman.entities.issuetype;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * IssueTypeMapping
 *
 */
@Preload("SOLMAN_PROCESS_TYPE, JIRA_ISSUE_TYPE")
public interface IssueTypeMapping extends Entity {

    String getSolmanProcessType();
    void setSolmanProcessType(String solmanProcessType);

    Integer getJiraIssueType();
    void setJiraIssueType(Integer jiraIssueType);

    SolmanParamsAO getSolmanParams();
    void setSolmanParams(SolmanParamsAO solmanParams);
}
