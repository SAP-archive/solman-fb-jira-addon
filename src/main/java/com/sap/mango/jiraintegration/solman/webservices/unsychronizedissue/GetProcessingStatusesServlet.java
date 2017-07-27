package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.ProcessingStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet, that returns all processing statuses of unsynchronized issue
 */
public class GetProcessingStatusesServlet extends JsonServlet {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProcessingStatusBean> processingStatusBeanList = new ArrayList<>();
        for (ProcessingStatus processingStatus : ProcessingStatus.values()) {
            processingStatusBeanList.add(new ProcessingStatusBean(processingStatus.getValue(), processingStatus.getName()));
        }
        sendOK(resp, processingStatusBeanList);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
