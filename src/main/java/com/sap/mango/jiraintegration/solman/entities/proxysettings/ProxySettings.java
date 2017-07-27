package com.sap.mango.jiraintegration.solman.entities.proxysettings;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * Entity, that stores the proxy settings for Solution Manager connection.
 */
@Preload("PROXY_HOST, PORT")
public interface ProxySettings extends Entity {

     void setSolmanParams(SolmanParamsAO solmanParams);
     SolmanParamsAO getSolmanParams();

     void setProxyHost(String proxyHost);
     String getProxyHost();

     Integer getPort();
     void setPort(Integer port);

}
