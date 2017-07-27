package com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue;

import com.atlassian.scheduler.JobRunner;
import com.atlassian.scheduler.config.JobRunnerKey;

/**
 * Contains
 */
public interface SolmanPluginJobRunner extends JobRunner {
    //job key
    JobRunnerKey JOB_RUNNER_KEY = JobRunnerKey.of(SolmanPluginJobRunner.class.getName());

    //Solman Customer GUID
    String SOLMAN_CUSTOMER_GUID = "SOLMAN_CUSTOMER_GUID";
}
