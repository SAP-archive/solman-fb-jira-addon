package com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue;

import com.atlassian.scheduler.JobRunnerRequest;
import com.atlassian.scheduler.JobRunnerResponse;
import com.sap.mango.jiraintegration.core.data.Either;
import com.sap.mango.jiraintegration.core.httpclient.RestClient;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.*;
import com.sap.mango.jiraintegration.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Implements the Solman issue synchronization job for specific Solman Customer
 */
public class SolmanPluginJobRunnerImpl implements SolmanPluginJobRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SolmanPluginJobRunnerImpl.class);

    private UnsynchronizedIssueDAO unsynchronizedIssueDAO;

    private IssueTransitionService issueTransitionService;

    private IssueTransitionDAO issueTransitionDAO;

    public SolmanPluginJobRunnerImpl(IssueTransitionDAO issueTransitionDAO, UnsynchronizedIssueDAO unsynchronizedIssueDAO, IssueTransitionService issueTransitionService) {
        this.issueTransitionDAO = issueTransitionDAO;
        this.unsynchronizedIssueDAO = unsynchronizedIssueDAO;
        this.issueTransitionService = issueTransitionService;
    }

    @Nullable
    @Override
    public JobRunnerResponse runJob(JobRunnerRequest jobRunnerRequest) {
        //retrieve the specific customer
        String solmanCustomerGuid = (String) jobRunnerRequest.getJobConfig().getParameters().get(SOLMAN_CUSTOMER_GUID);

        Integer synchronizedIssues = 0;
        Integer issuesToSynchronize = 0;

        Integer notSynchronizedIssuesCommError = 0;
        Integer notSynchronizedIssuesAppError = 0;
        Integer notSynchronizedIssuesInternalError = 0;

        LOG.info("Running Solman Issue Synchronization Job For Customer " + solmanCustomerGuid);

        List<UnsynchronizedIssue> unsynchronizedIssueList = unsynchronizedIssueDAO.getUnsynchronizedIssues(solmanCustomerGuid);
        issuesToSynchronize = unsynchronizedIssueList.size();
        LOG.info("Issues to synchronize: " + issuesToSynchronize);

        for (UnsynchronizedIssue unsynchronizedIssue : unsynchronizedIssueList) {
            boolean isSynchronizedIssue = true;
            LOG.info("Synchronizing issue: " + unsynchronizedIssue.getIssueKey());
            List<IssueTransition> issueTransitions = issueTransitionDAO.getUnsynchronizedIssues(unsynchronizedIssue.getID());
            for (IssueTransition issueTransition : issueTransitions) {
                try {
                    Either<RestClient.ServiceError, String> issueStatusSynchronization = issueTransitionService.executeIssueStatusSynchronization(issueTransition.getID(), false);
                    if (issueStatusSynchronization.isLeft()) {
                        isSynchronizedIssue = false;
                        if (HttpUtils.isCommunicationError(issueStatusSynchronization.toLeft().left_value.code)) {
                            LOG.error("Not synchronized issue {} because of communication error ", unsynchronizedIssue.getIssueKey());
                            notSynchronizedIssuesCommError++;
                            break;
                        } else {
                            LOG.error("Not synchronized issue {} because of application error ", unsynchronizedIssue.getIssueKey());
                            notSynchronizedIssuesAppError++;
                            break;
                        }
                    }
                } catch (IssueSynchronizationException ise) {
                    LOG.error("Internal exception while synchronizing issue " + unsynchronizedIssue.getIssueKey() + ise.getMessage());
                    notSynchronizedIssuesInternalError++;
                    break;
                }
            }
            if (isSynchronizedIssue) {
                synchronizedIssues++;
            }

        }
        return JobRunnerResponse.success(process(synchronizedIssues, notSynchronizedIssuesCommError, notSynchronizedIssuesAppError, notSynchronizedIssuesInternalError));
    }

    private String process(@Nonnull Integer synchronizedIssues, @Nonnull Integer notSynchronizedIssuesCommError,
                           @Nonnull Integer notSynchronizedIssuesAppError, @Nonnull Integer notSynchronizedIssuesInternalError) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Statistics: ");
        stringBuilder.append("Synchronized Issues: " + synchronizedIssues);
        stringBuilder.append("Not synchronized issues, internal error: " + notSynchronizedIssuesInternalError);
        stringBuilder.append("Not synchronized issues, communication error: " + notSynchronizedIssuesCommError);
        stringBuilder.append("Not synchronized issues, application error: " + notSynchronizedIssuesAppError);
        return stringBuilder.toString();
    }
}
