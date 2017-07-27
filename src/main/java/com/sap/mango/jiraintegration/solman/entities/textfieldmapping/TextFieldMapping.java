package com.sap.mango.jiraintegration.solman.entities.textfieldmapping;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * FieldMapping
 *
 */
@Preload("SOLMAN_PROCESS_TYPE, SOLMAN_TEXT_TYPE, JIRA_FIELD")
public interface TextFieldMapping extends Entity {

    String getSolmanProcessType();
    void setSolmanProcessType(String solmanProcessType);

    String getSolmanTextType();
    void setSolmanTextType(String textType);

    String getJiraField();
    void setJiraField(String jiraField);

    SolmanParamsAO getSolmanParams();
    void setSolmanParams(SolmanParamsAO solmanParams);
}
