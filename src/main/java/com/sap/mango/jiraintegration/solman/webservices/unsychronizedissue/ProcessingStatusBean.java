package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 *
 */
@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class ProcessingStatusBean {

    private Integer id;
    private String name;

    public ProcessingStatusBean(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProcessingStatusBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
