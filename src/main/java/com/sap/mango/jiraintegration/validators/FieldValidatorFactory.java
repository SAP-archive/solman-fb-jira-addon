package com.sap.mango.jiraintegration.validators;

import com.atlassian.core.util.map.EasyMap;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by vld on 09.12.15.
 *
 */
public class FieldValidatorFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory {
    private static final String FIELD_NAME = "field123";
    private static final String FIELDS = "fields";
    private static final String NOT_DEFINED = "Not Defined v";

    private final CustomFieldManager customFieldManager;

    public FieldValidatorFactory(CustomFieldManager customFieldManager) {
        this.customFieldManager = customFieldManager;
    }

    @Override
    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        velocityParams.put(FIELD_NAME, getFieldName(descriptor));
        velocityParams.put(FIELDS, getCFFields());
    }

    @Override
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        velocityParams.put(FIELDS, getCFFields());
    }

    @Override
    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        velocityParams.put(FIELD_NAME, getFieldName(descriptor));
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getDescriptorParams(Map<String, Object> conditionParams) {
        if (conditionParams != null && conditionParams.containsKey(FIELD_NAME)) {
            return EasyMap.build(FIELD_NAME, extractSingleParam(conditionParams, FIELD_NAME));
        }

        // Create a 'hard coded' parameter
        return EasyMap.build();
    }

    private String getFieldName(AbstractDescriptor descriptor) {
        //Extract field from the workflow
        if (!(descriptor instanceof ValidatorDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a ConditionDescriptor.");
        }

        ValidatorDescriptor validatorDescriptor = (ValidatorDescriptor) descriptor;

        String field = (String) validatorDescriptor.getArgs().get(FIELD_NAME);
        if (field != null && field.trim().length() > 0)
            return field;
        else
            return NOT_DEFINED;
    }

    private Collection<CustomField> getCFFields() {
        //Get list of custom fields
        List<CustomField> customFields = customFieldManager.getCustomFieldObjects();
        return customFields;
    }
}