package com.sap.mango.jiraintegration.solman.entities.projecttype;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * Stores the project mapping SolMan Project <-> Jira Project for specific Solman Customer
 */
@Preload("SOLMAN_PROJECT_ID, JIRA_PROJECT_ID")
public interface ProjectMapping extends Entity {

    //@NotNull
    public String getSolmanProjectID();
    public void setSolmanProjectID(String solmanProjectID);

    //@NotNull
    public String getJiraProjectID();
    public void setJiraProjectID(String jiraProjectID);

    public void setSolmanParams(SolmanParamsAO solmanParams);
    public SolmanParamsAO getSolmanParams();

}
