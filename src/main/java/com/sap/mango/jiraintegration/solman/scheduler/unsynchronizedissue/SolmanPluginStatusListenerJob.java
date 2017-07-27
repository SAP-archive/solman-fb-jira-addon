package com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue;

import com.atlassian.plugin.PluginAccessor;
import com.atlassian.sal.api.scheduling.PluginJob;
import com.atlassian.sal.api.scheduling.PluginScheduler;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Defines a job, that listens for plugin status to become 'enabled' in order to create solman schedulers
 */
public class SolmanPluginStatusListenerJob implements PluginJob {

    private static final long DEFAULT_SCHEDULE_BASE_INTERVALS_MILLIS = 60000l;

    private static final String PLUGIN_KEY = "com.sap.mango.jiraintegration";

    public static final String JOB_NAME = SolmanPluginStatusListenerJob.class.getName() + ":job";

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SolmanPluginStatusListenerJob.class);

    @Override
    public void execute(Map<String, Object> map) {
        final PluginAccessor pluginAccessor = (PluginAccessor)map.get("pluginAccessor");
        final PluginScheduler pluginScheduler = (PluginScheduler) map.get("pluginScheduler");
        final SolmanParamsDAO solmanParamsDAO = (SolmanParamsDAO) map.get("solmanParamsDAO");
        final SolmanPluginSchedulerManager solmanPluginSchedulerManager = (SolmanPluginSchedulerManager) map.get("solmanPluginSchedulerManager");

        if (pluginAccessor.isPluginEnabled(PLUGIN_KEY)) {
                LOG.info("Solman Customers:" + solmanParamsDAO.getSolmanParamsAll(false));
                for (SolmanParams solmanParams : solmanParamsDAO.getSolmanParamsAll(false)) {
                    solmanPluginSchedulerManager.createSolmanIssueSynchronizationJobSchedule(solmanParams, DEFAULT_SCHEDULE_BASE_INTERVALS_MILLIS);
                }
            pluginScheduler.unscheduleJob(JOB_NAME);
        }
    }
}
