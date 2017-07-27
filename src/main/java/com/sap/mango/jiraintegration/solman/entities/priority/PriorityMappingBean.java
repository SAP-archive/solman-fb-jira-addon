package com.sap.mango.jiraintegration.solman.entities.priority;

/**
 * Bean, that represents PriorityMapping
 */
public class PriorityMappingBean {

    private Integer id;
    private String solmanPriority;
    private String jiraPriority;

    public PriorityMappingBean(Integer id, String solmanPriority, String jiraPriority) {
        this.id = id;
        this.solmanPriority = solmanPriority;
        this.jiraPriority = jiraPriority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSolmanPriority() {
        return solmanPriority;
    }

    public void setSolmanPriority(String solmanPriority) {
        this.solmanPriority = solmanPriority;
    }

    public String getJiraPriority() {
        return jiraPriority;
    }

    public void setJiraPriority(String jiraPriority) {
        this.jiraPriority = jiraPriority;
    }
}
