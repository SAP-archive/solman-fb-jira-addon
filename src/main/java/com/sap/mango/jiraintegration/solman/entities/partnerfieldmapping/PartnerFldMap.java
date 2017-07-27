package com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * FieldMapping
 *
 */
@Preload("SOLMAN_PROCESS_TYPE, SOLMAN_PARTNER_FUNCTION, SOLMAN_FIELD, JIRA_FIELD")
public interface PartnerFldMap extends Entity {

    String getSolmanProcessType();
    void setSolmanProcessType(String solmanProcessType);

    String getSolmanPartnerFunction();
    void setSolmanPartnerFunction(String jiraIssueType);

    String getSolmanField();
    void setSolmanField(String jiraIssueType);

    String getJiraField();
    void setJiraField(String jiraIssueType);

    SolmanParamsAO getSolmanParams();
    void setSolmanParams(SolmanParamsAO solmanParams);
}
