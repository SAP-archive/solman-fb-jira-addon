package com.sap.mango.jiraintegration.solman.webservices.proxysettings;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * Bean, that stores the proxy settings information.
 */
@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class SolmanProxySettingsBean {

    private Integer id;
    private String solManCustGuiD;
    private String proxyHost;
    private Integer port;

    public SolmanProxySettingsBean() {
    }

    public SolmanProxySettingsBean(Integer id, String solManCustGuiD, String proxyHost, Integer port) {
        this.id = id;
        this.solManCustGuiD = solManCustGuiD;
        this.proxyHost = proxyHost;
        this.port = port;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSolManCustGuiD() {
        return solManCustGuiD;
    }

    public void setSolManCustGuiD(String solManCustGuiD) {
        this.solManCustGuiD = solManCustGuiD;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}
}
