package com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue;

import com.atlassian.scheduler.SchedulerHistoryService;
import com.atlassian.scheduler.SchedulerService;
import com.atlassian.scheduler.SchedulerServiceException;
import com.atlassian.scheduler.config.JobConfig;
import com.atlassian.scheduler.config.JobId;
import com.atlassian.scheduler.config.RunMode;
import com.atlassian.scheduler.config.Schedule;
import com.atlassian.scheduler.status.JobDetails;
import com.atlassian.scheduler.status.RunDetails;
import com.google.common.collect.ImmutableMap;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue.SolmanPluginJobRunner.JOB_RUNNER_KEY;
import static com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue.SolmanPluginJobRunner.SOLMAN_CUSTOMER_GUID;

/**
 *
 */
public class SolmanPluginSchedulerManagerImpl implements SolmanPluginSchedulerManager {

    private static final Logger LOG = LoggerFactory.getLogger(SolmanPluginSchedulerManagerImpl.class);

    private static final Integer MIN_DELAY = 15000;

    private static final Integer MAX_JITTER = 10000;

    private static final String JOB_ID_PREFIX = "SolmanJobScheduler : ";

    private static final long DEFAULT_SCHEDULE_BASE_INTERVALS_MILLIS = 60000l;

    private static final Random RANDOM = new Random();

    private final SchedulerService schedulerService;

    private final SchedulerHistoryService schedulerHistoryService;


    public SolmanPluginSchedulerManagerImpl(SchedulerService schedulerService, SchedulerHistoryService schedulerHistoryService) {
        this.schedulerService = schedulerService;
        this.schedulerHistoryService = schedulerHistoryService;
    }

    @Override
    public void createSolmanIssueSynchronizationJobSchedule(SolmanParams solmanParams, long intervalInMillis) {
        final int jitter = RANDOM.nextInt(MAX_JITTER);
        //
        final Date firstRun = new Date(System.currentTimeMillis() + MIN_DELAY + jitter);
        final Map<String, Serializable> parameters = ImmutableMap.of(SOLMAN_CUSTOMER_GUID, solmanParams.customerGuid);

        final JobConfig jobConfig = JobConfig.forJobRunnerKey(JOB_RUNNER_KEY)
                .withRunMode(RunMode.RUN_LOCALLY)
                .withSchedule(Schedule.forInterval(intervalInMillis, firstRun))
                .withParameters(parameters);

        LOG.info("Scheduling job with jitter = " + jitter + jobConfig);
        try {
            final JobId jobId = toJobId(solmanParams.customerGuid);

            JobDetails jobDetails = schedulerService.getJobDetails(jobId);

            if(jobDetails != null) {
                LOG.info("We will be replacing an existing job with jobid = " + jobId + " : " + jobDetails);
            }
            schedulerService.scheduleJob(jobId, jobConfig);
        }
        catch(SchedulerServiceException sse) {

        }
    }

    @Override
    public void createSolmanIssueSynchronizationJobSchedule(SolmanParams solmanParams) {
        createSolmanIssueSynchronizationJobSchedule(solmanParams, DEFAULT_SCHEDULE_BASE_INTERVALS_MILLIS);
    }

    @Override
    public void unscheduleSolmanIssueSynchronizationJobSchedule(SolmanParams solmanParams)  {
        final JobId id = toJobId(solmanParams.customerGuid);
        final JobDetails jobDetails = schedulerService.getJobDetails(id);

        if (jobDetails != null) {
            unscheduleSolmanIssueSynchronizationJobSchedule(jobDetails);
        }
    }

    @Override
    public RunDetails getSolmanIssueSynchronizationRunDetails(SolmanParams solmanParams) {
        final JobId id = toJobId(solmanParams.customerGuid);
        RunDetails runDetails = schedulerHistoryService.getLastRunForJob(id);
        return runDetails;
    }

    private void unscheduleSolmanIssueSynchronizationJobSchedule(@Nonnull JobDetails jobDetails) {
        if (!JOB_RUNNER_KEY.equals(jobDetails.getJobRunnerKey())) {
            LOG.info("JobId " + jobDetails.getJobId() + " 'does not belong to SolmanSchedulerManager");
        }
        schedulerService.unscheduleJob(jobDetails.getJobId());
    }

    static JobId toJobId(String solmanCustomerGuid) {
        return JobId.of(JOB_ID_PREFIX + solmanCustomerGuid);
    }

}
