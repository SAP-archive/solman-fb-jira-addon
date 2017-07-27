package com.sap.mango.jiraintegration.solman;

import com.opensymphony.workflow.loader.AbstractDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by vld on 08.12.15.
 *
 */
public class IssueValidatorFunc implements com.atlassian.jira.plugin.workflow.WorkflowPluginConditionFactory  {


    private static final Logger log = LoggerFactory.getLogger(IssueEventListener.class);


//    @Override
//    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
//        log.info("Transient: " + transientVars + " Args: "+ args+ "Property set: " + ps);
//        throw new WorkflowException("Test to stop update process...");
//    }

    @Override
    public Map<String, ?> getVelocityParams(String s, AbstractDescriptor abstractDescriptor) {

        return null;
    }

    @Override
    public Map<String, ?> getDescriptorParams(Map<String, Object> map) {

        return null;
    }
}
