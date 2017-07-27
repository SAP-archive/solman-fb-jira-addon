package com.sap.mango.jiraintegration.solman.entities.solmanparams;


import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMapping;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMapping;
import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.Preload;
import net.java.ao.schema.Unique;

/**
 * Created by vlal on 26.11.15.
 *
 */
@Preload("SOLMAN_URL, USERNAME, PASSWORD, CUSTOMER_GIUD, CUSTOMER_DESCRIPTION, SAP_CLIENT, AUTHENTICATION_TYPE, TOKEN_HCP_ACCOUNT_URL")
public interface SolmanParamsAO extends Entity {
    public String getSolmanUrl();
    public void setSolmanUrl(String solmanUrl);

    public String getUsername();
    public void setUsername(String username);

    public String getPassword();
    public void setPassword(String password);

    public String getCustomerGiud();
    public void setCustomerGiud(String customerGiud);

    public String getCustomerDescription();
    public void setCustomerDescription(String customerDescription);

    public String getSapClient();
    public void setSapClient(String sapClient);

    public Integer getAuthenticationType();
    public void setAuthenticationType(Integer authenticationType);

    public void setTokenHcpAccountUrl(String tokenUrl);
    public String getTokenHcpAccountUrl();

    @Unique
    @OneToMany(reverse = "getSolmanParams")
    public ProjectMapping[] getProjectTypeMappings();

    @OneToMany(reverse = "getSolmanParams")
    public IssueStatusMapping[] getIssueStatuses();

}
