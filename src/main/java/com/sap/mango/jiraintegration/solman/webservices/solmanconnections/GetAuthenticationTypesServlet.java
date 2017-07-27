package com.sap.mango.jiraintegration.solman.webservices.solmanconnections;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.AuthenticationTypeEnum;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Web service, that returns all authentication types.
 */
public class GetAuthenticationTypesServlet extends JsonServlet {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<AuthenticationType> authenticationTypes = new ArrayList<>();
        for (AuthenticationTypeEnum authenticationTypeEnum : AuthenticationTypeEnum.values()){
            authenticationTypes.add(new AuthenticationType(authenticationTypeEnum.value, AuthenticationTypeEnum.getValueByKey(authenticationTypeEnum.value)));
        }
        sendOK(resp, authenticationTypes);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }

    class AuthenticationType {
        private String authenticationType;
        private Integer id;

        public AuthenticationType() {

        }

        public AuthenticationType(Integer id, String authenticationType) {
            this.authenticationType = authenticationType;
            this.id = id;
        }

        public String getAuthenticationType() {
            return authenticationType;
        }

        public void setAuthenticationType(String authenticationType) {
            this.authenticationType = authenticationType;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "AuthenticationType{" +
                    "authenticationType='" + authenticationType + '\'' +
                    ", id=" + id +
                    '}';
        }
    }

}
