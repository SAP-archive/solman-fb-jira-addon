package com.sap.mango.jiraintegration.solman.entities.issuestatus;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * IssueStatusMapping inbound
 */
@Preload("SOLMAN_PROCESS_TYPE, SOLMAN_STATUS, JIRA_TRANSITION")
public interface IssueStatusMapping extends Entity {
    public String getSolmanProcessType();
    public void setSolmanProcessType(String solmanProcessType);

    public String getSolmanStatus();
    public void setSolmanStatus(String solmanStatus);

    public String getJiraTransition();
    public void setJiraTransition(String jiraTransition);

    public SolmanParamsAO getSolmanParams();
    public void setSolmanParams(SolmanParamsAO solmanParams);
}
