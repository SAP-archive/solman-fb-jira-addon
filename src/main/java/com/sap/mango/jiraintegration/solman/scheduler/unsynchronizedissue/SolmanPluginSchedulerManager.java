package com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue;

import com.atlassian.scheduler.status.RunDetails;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage all solman job schedules
 */
public interface SolmanPluginSchedulerManager {
    /**
     * Creates job schedule for specific Solman Customer
     * @param intervalInMillis
     */
    void createSolmanIssueSynchronizationJobSchedule(SolmanParams solmanParams, long intervalInMillis);

    /**
     * Creates job schedule for specific Solman Customer with default timestamp
     * @param solmanParams
     */
    void createSolmanIssueSynchronizationJobSchedule(SolmanParams solmanParams);

    /**
     * Deletes job sched
     * @param solmanParams
     */
    void unscheduleSolmanIssueSynchronizationJobSchedule(SolmanParams solmanParams);

    /**
     * Returns info about the last execution of specific Solman Customer issue sync job
     * @param solmanParams
     * @return
     */
    RunDetails getSolmanIssueSynchronizationRunDetails(SolmanParams solmanParams);
}
