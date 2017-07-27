package com.sap.mango.jiraintegration.solman.entities.priority;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * Entity, that stores the mapping
 */
@Preload("SOLMAN_PRIORITY, JIRA_PRIORITY")
public interface PriorityMapping extends Entity {
    SolmanParamsAO getSolmanParams();
    void setSolmanParams(SolmanParamsAO solmanParams);

    String getSolmanPriority();
    void setSolmanPriority(String solmanPriority);

    String getJiraPriority();
    void setJiraPriority(String jiraPriority);
}
