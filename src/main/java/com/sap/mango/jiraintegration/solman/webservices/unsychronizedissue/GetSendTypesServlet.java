package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.SendType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet that returns all defined send types
 */
public class GetSendTypesServlet extends JsonServlet {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<SendTypeBean> sendTypeBeanList = new ArrayList<>();
        for (SendType sendType : SendType.values()) {
            sendTypeBeanList.add(new SendTypeBean(sendType.getValue(), sendType.getName()));
        }
        sendOK(resp, sendTypeBeanList);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
