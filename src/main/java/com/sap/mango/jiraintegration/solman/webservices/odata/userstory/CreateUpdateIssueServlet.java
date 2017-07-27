package com.sap.mango.jiraintegration.solman.webservices.odata.userstory;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.utils.PropertiesUtils;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.edmx.EdmxReference;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * OData web service, that creates/updates a jira issue (epic or story) <br/>
 * Service url:  /IssueService.svc/* <br/>
 * Metadata url: /IssueService.svc/$metadata <br/>
 */
public class CreateUpdateIssueServlet extends JsonServlet {

    private IssueEntityStorageService issueEntityStorageService;

    public CreateUpdateIssueServlet(IssueEntityStorageService issueEntityStorageService) {
        this.issueEntityStorageService = issueEntityStorageService;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       super.service(req, resp);
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            OData odata = OData.newInstance();
            ServiceMetadata edm = odata.createServiceMetadata(new IssueEdmProvider(), new ArrayList<EdmxReference>());
            ODataHttpHandler handler = odata.createHandler(edm);
            handler.register(new IssueEntityProcessor(issueEntityStorageService));

            handler.process(req, resp);
        } catch (RuntimeException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public String requiredRoles() {
        return PropertiesUtils.getValue("solman.admin.authorization.odataws.usergroup");
    }
}
