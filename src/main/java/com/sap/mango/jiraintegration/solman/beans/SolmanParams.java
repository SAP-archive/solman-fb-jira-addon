package com.sap.mango.jiraintegration.solman.beans;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * Created by vlal on 26.11.15.
 *
 */
@JsonSerialize(using = JsonHtmlXssSerializer.class )
public class SolmanParams {

    public String solmanUrl;
    public String userName;
    public String password;
    public String customerGuid;
    public String customerDescription;
    public String sapClient;
    public String authenticationType;
    public String tokenHcpAccountUrl;

    @SuppressWarnings("unused")
    public SolmanParams(){
    }
    public SolmanParams(String solmanUrl, String userName, String password, String customerGuid, String customerDescription, String sapClient,
                        String authenticationType, String tokenHcpAccountUrl) {
        this.solmanUrl = solmanUrl;
        this.userName = userName;
        this.password = password;
        this.customerGuid = customerGuid;
        this.customerDescription = customerDescription;
        this.sapClient = sapClient;
        this.authenticationType = authenticationType;
        this.tokenHcpAccountUrl = tokenHcpAccountUrl;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SolmanParams that = (SolmanParams) o;

        if (customerDescription != null ? !customerDescription.equals(this.customerDescription) : this.customerDescription != null) return false;
        if (solmanUrl != null ? !solmanUrl.equals(that.solmanUrl) : that.solmanUrl != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (customerGuid != null ? !customerGuid.equals(that.customerGuid) : that.customerGuid != null) return false;
        if (sapClient != null ? !sapClient.equals(this.sapClient) : that.sapClient != null) return false;
        if (authenticationType != null ? !(authenticationType.equals(this.authenticationType)) : that.authenticationType != null) return false;
        return (tokenHcpAccountUrl != null ? !tokenHcpAccountUrl.equals(this.tokenHcpAccountUrl) : that.tokenHcpAccountUrl != null);
    }

    @Override
    public int hashCode() {
        int result = solmanUrl != null ? solmanUrl.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (customerGuid != null ? customerGuid.hashCode() : 0);
        result = 31 * result + (customerDescription != null ? customerDescription.hashCode() : 0);
        result = 31 * result + (sapClient != null ? sapClient.hashCode() : 0);
        result = 31 * result + (authenticationType != null ? authenticationType.hashCode() : 0);
        result = 31 * result + (tokenHcpAccountUrl != null ? tokenHcpAccountUrl.hashCode() : 0);

        return result;
    }


    @Override
    public String toString() {
        return "SolmanParams{" +
                ", solmanUrl='" + solmanUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", customerGuid='" + customerGuid + '\'' +
                ", customerDescription='" + customerDescription + '\'' +
                ", sapClient='" + sapClient + '\'' +
                ", authenticationType='" + authenticationType + '\'' +
                ", tokenHcpAccountUrl='" + tokenHcpAccountUrl + '\'' +
                '}';
    }
}
