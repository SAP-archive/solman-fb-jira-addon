package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.*;
import com.sap.mango.jiraintegration.utils.JsonEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Returns all unsynchronized issues for specific Solution Manager Customer.
 */
public class GetUnsychronizedIssuesServlet extends JsonServlet {

    private static final String solManGuidCustField = "Solman guid";
    private IssueTransitionDAO issueTransitionDAO;
    private IssueService issueService;
    private JiraAuthenticationContext authenticationContext;

    public GetUnsychronizedIssuesServlet(IssueTransitionDAO issueTransitionDAO, UnsynchronizedIssueDAO unsynchronizedIssueDAO, IssueService issueService, JiraAuthenticationContext authenticationContext) {
        this.issueService = issueService;
        this.authenticationContext = authenticationContext;
        this.issueTransitionDAO = issueTransitionDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int recordsPerPage = 10;

        GetUnsynchronizedIssuesRequestBean getUnsynchronizedIssuesRequestBean = JsonEncoder.mapper.readValue(req.getParameter("unsynchronizedIssueRequestParameters"), GetUnsynchronizedIssuesRequestBean.class);

        if (getUnsynchronizedIssuesRequestBean.getSolManCustGuiD() == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        String solManCustGuiD = getUnsynchronizedIssuesRequestBean.getSolManCustGuiD();
        Date creationDate = getUnsynchronizedIssuesRequestBean.getUnsynchronizedIssueCreationDate();

        if (getUnsynchronizedIssuesRequestBean.getPage() != null) {
            page = getUnsynchronizedIssuesRequestBean.getPage();
        }

        if (getUnsynchronizedIssuesRequestBean.getRecordsPerPage() != null) {
            recordsPerPage = getUnsynchronizedIssuesRequestBean.getRecordsPerPage();
        }

        String issueKey = getUnsynchronizedIssuesRequestBean.getIssueKey();
        String sortBy = getUnsynchronizedIssuesRequestBean.getOrderBy();
        String direction = getUnsynchronizedIssuesRequestBean.getDirection();

        List<IssueTransition> issueTransitions = issueTransitionDAO.getIssueTransitions(solManCustGuiD, (page - 1) * recordsPerPage, recordsPerPage, issueKey, creationDate, sortBy, direction);
        int listSize = issueTransitionDAO.getIssueTransitions(solManCustGuiD, null, null, issueKey, creationDate, null, null).size();

        resp.addHeader("maxPageSize", String.valueOf((int) Math.ceil((listSize * 1.0) / recordsPerPage)));
        resp.addHeader("currentPage", String.valueOf(page));
        resp.addHeader("recordsPerPage", String.valueOf(recordsPerPage));
        if (getUnsynchronizedIssuesRequestBean.getIssueKey() != null && !getUnsynchronizedIssuesRequestBean.getIssueKey().isEmpty()) {
            resp.addHeader("issueKey", getUnsynchronizedIssuesRequestBean.getIssueKey());
        }

        sendOK(resp, transformUnsynchronizedIssues(issueTransitions));
    }

    private List<IssueTransitionBean> transformUnsynchronizedIssues(List<IssueTransition> issueTransitions) {
        List<IssueTransitionBean> issueTransitionBeanList = new ArrayList<>();

        CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
        CustomField solManGuidCF = customFieldManager.getCustomFieldObjectByName(solManGuidCustField);

        for (IssueTransition issueTransition : issueTransitions) {
            Issue issue = issueService.getIssue(this.authenticationContext.getLoggedInUser(), issueTransition.getUnsynchronizedIssue().getIssueId()).getIssue();
            String solManGuidCFValue = (String) issue.getCustomFieldValue(solManGuidCF);
            issueTransitionBeanList.add(new IssueTransitionBean(issueTransition.getID(), issueTransition.getUnsynchronizedIssue().getIssueId(), issueTransition.getUnsynchronizedIssue().getIssueKey(),
                    issueTransition.getUnsynchronizedIssue().getIssueId(), issueTransition.getCreationDate(), issueTransition.getFieldId(),
                    issueTransition.getOldValue(), issueTransition.getNewValue(), issueTransition.getLastProcessingDate(), issueTransition.isSuccessful(),
                    issueTransition.getSynchronizationDate(), issueTransition.getLastProcessor(), issueTransition.getSendType(), issueTransition.getProcessingStatus(),
                    solManGuidCFValue, issueTransition.getProcessingUser()));
        }
        return issueTransitionBeanList;
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
