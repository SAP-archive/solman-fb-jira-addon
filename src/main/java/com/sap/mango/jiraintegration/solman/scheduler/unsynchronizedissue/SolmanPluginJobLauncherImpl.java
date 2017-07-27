package com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue;


import com.atlassian.plugin.PluginAccessor;
import com.atlassian.sal.api.lifecycle.LifecycleAware;
import com.atlassian.sal.api.scheduling.PluginScheduler;
import com.atlassian.scheduler.SchedulerService;
import com.google.common.collect.ImmutableMap;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import static com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue.SolmanPluginStatusListenerJob.*;
import java.util.Date;
import java.util.Map;

/**
 * Coordinate the startup information as job runner and job implementations
 */
public class SolmanPluginJobLauncherImpl implements LifecycleAware, InitializingBean, DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(SolmanPluginJobLauncherImpl.class);
    private static final long DEFAULT_SCHEDULE_BASE_INTERVALS_MILLIS = 60000l;

    private final SchedulerService schedulerService;
    private final SolmanPluginJobRunner solmanPluginJobRunner;
    private final SolmanPluginSchedulerManager solmanPluginSchedulerManager;
    private final PluginScheduler pluginScheduler;
    private final PluginAccessor pluginAccessor;
    private final SolmanParamsDAO solmanParamsDAO;


    public SolmanPluginJobLauncherImpl(final PluginAccessor pluginAccessor,
            final PluginScheduler pluginScheduler, final SolmanParamsDAO solmanParamsDAO,
            final SchedulerService schedulerService, final SolmanPluginJobRunner solmanPluginJobRunner,
            final SolmanPluginSchedulerManager solmanPluginSchedulerManager) {

        this.schedulerService = schedulerService;
        this.solmanPluginJobRunner = solmanPluginJobRunner;
        this.pluginAccessor = pluginAccessor;
        this.pluginScheduler = pluginScheduler;
        this.solmanParamsDAO = solmanParamsDAO;
        this.solmanPluginSchedulerManager = solmanPluginSchedulerManager;
    }

    @Override
    public void onStop() {

    }

    private void registerJobRunner() {
        LOG.info("registerJobRunner");
        schedulerService.registerJobRunner(SolmanPluginJobRunner.JOB_RUNNER_KEY, solmanPluginJobRunner);
    }

    @Override
    public void destroy() throws Exception {
        unscheduleSolmanIssueSynchronizationJobs();
        unregisterJobRunner();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void onStart() {
        registerJobRunner();
        registerSolmanStatusJob(DEFAULT_SCHEDULE_BASE_INTERVALS_MILLIS);
    }

    private void unscheduleSolmanIssueSynchronizationJobs() {
        LOG.info("unscheduleSolmanIssueSynchronizationJobs");
        for (SolmanParams solmanParams : solmanParamsDAO.getSolmanParamsAll(false)) {
            solmanPluginSchedulerManager.unscheduleSolmanIssueSynchronizationJobSchedule(solmanParams);
        }
    }

    private void unregisterJobRunner() {
        LOG.info("unregisterJobRunner");
        schedulerService.unregisterJobRunner(SolmanPluginJobRunner.JOB_RUNNER_KEY);
    }

    /**
     * Create Job, that should detect when the plugin is in status 'enabled', in order to be able to use
     * active objects and to create the Solution Manager Issue Synchronization Jobs
     *
     * @param intervalInSeconds
     */
    public void registerSolmanStatusJob(long intervalInSeconds) {
        Map<String, Object> jobDataMap = ImmutableMap.of(
                "pluginScheduler", pluginScheduler,
                "pluginAccessor", pluginAccessor,
                "solmanPluginSchedulerManager", solmanPluginSchedulerManager,
                "solmanParamsDAO", solmanParamsDAO);

        pluginScheduler.scheduleJob(JOB_NAME,
                SolmanPluginStatusListenerJob.class,
                jobDataMap,
                new Date(),
                intervalInSeconds
        );
    }

}
