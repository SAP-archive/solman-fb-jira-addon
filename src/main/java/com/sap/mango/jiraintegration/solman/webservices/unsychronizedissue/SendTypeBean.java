package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * Stores SendType enum
 */

@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class SendTypeBean {
    private Integer id;
    private String name;

    public SendTypeBean(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "SendTypeBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
