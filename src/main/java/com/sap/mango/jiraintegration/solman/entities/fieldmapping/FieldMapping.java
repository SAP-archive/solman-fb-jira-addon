package com.sap.mango.jiraintegration.solman.entities.fieldmapping;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * FieldMapping
 *
 */
@Preload("SOLMAN_PROCESS_TYPE, SOLMAN_FIELD, JIRA_FIELD")
public interface FieldMapping extends Entity {

    String getSolmanProcessType();
    void setSolmanProcessType(String solmanProcessType);

    String getSolmanField();
    void setSolmanField(String jiraIssueType);

    String getJiraField();
    void setJiraField(String jiraIssueType);

    SolmanParamsAO getSolmanParams();
    void setSolmanParams(SolmanParamsAO solmanParams);
}
