package com.sap.mango.jiraintegration.utils;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.search.SearchException;

import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.jira.jql.parser.JqlQueryParser;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.query.Query;
import com.atlassian.sal.api.search.SearchProvider;
import com.atlassian.sal.api.search.SearchResults;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.sap.mango.jiraintegration.core.data.Either;
import org.apache.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vld on 06.01.16.
 *
 */
public class IssueUtils {

    private static final org.apache.log4j.Logger log = LogManager.getLogger(IssueUtils.class);


    public static Issue getIssueByKey(String issueKey){
        String filter = "issuekey = " + issueKey;

        JqlQueryParser jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser.class);
        SearchProvider searchProvider = ComponentAccessor.getComponent(SearchProvider.class);
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getUser();
        try {
            Query query = jqlQueryParser.parseQuery(filter);

            final SearchResults results = searchProvider.search(user.getUsername(), query.getQueryString());

            return null;

        } catch (JqlParseException e) {
            log.error(e);
        }

        return null;
    }

    public static Either<String, String> transitionIssue(Issue issue, int actionID, String targetStatus) {
        final IssueService issueService = ComponentAccessor.getIssueService();
        final JiraAuthenticationContext authContext = ComponentAccessor.getJiraAuthenticationContext();

        if (actionID > 0) {
            final IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
            issueInputParameters.setStatusId(targetStatus);
            issueInputParameters.setSkipScreenCheck(true);
            issueInputParameters.setResolutionId(null);
            IssueService.TransitionValidationResult validationResult = issueService.validateTransition(authContext.getLoggedInUser(), issue.getId(), actionID, issueInputParameters);
            if (validationResult.isValid()) {
                IssueService.IssueResult transitionResult = issueService.transition(authContext.getLoggedInUser(), validationResult);
                if(transitionResult.isValid()){
                    return new Either.Right<>("Succesfully transitioned: " + transitionResult.getIssue().toString() +
                            " Status: " + transitionResult.getIssue().getStatusObject().getName());
                } else {
                    return new Either.Left<>("Error! transition result is not valid. " + transitionResult.toString());
                }
            } else {
                return new Either.Left<>("Error! validation result is not valid. " + validationResult);
            }
        } else {
            return new Either.Left<>("NO valid ActionID!");
        }
    }




    public static Either<String, String> transitionIssueWF(Issue issue, int actionID, String targetStatus) {

        final WorkflowManager workflowManager = ComponentAccessor.getWorkflowManager();
        if ( issue == null ) {
            return new Either.Left<>("Error! issue is null.");
        }

        final JiraWorkflow workflow = workflowManager.getWorkflow(issue);
        List<Integer> actionIds = new ArrayList<>();
        while (true){
            Issue issue1 = getIssueByKey(issue.getKey());
            if (issue1 == null){
                return new Either.Left<>("Error! Cannot read issue: " + issue.getKey());
            }
            final StepDescriptor linkedStep = workflow.getLinkedStep(issue1.getStatusObject());
            final List<ActionDescriptor> actions = linkedStep.getActions();
            if (actions.size() == 0){
                return new Either.Left<>("Error! No next step");
            }

            ActionDescriptor action = actions.get(0);

            if (actionIds.contains(action.getId())){
                return new Either.Left<>("Error! Returned in the same step in the workflow with next transition: " + action.getName());
            }
            final Either<String, String> result = transitionIssue(issue1, action.getId(), null);
            if (result.isLeft()){
                return result;
            }else{
                if (action.getId() == actionID ||
                        (targetStatus != null && action.getUnconditionalResult().getStatus().toUpperCase().equals(targetStatus.toUpperCase()) ) ){
                    return result;
                }
            }

            actionIds.add(action.getId());
        }
    }
}
