package com.sap.mango.jiraintegration.solman.customfields;


import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.customfields.impl.GenericTextCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.CustomField;

import java.util.List;


/**
 * Created by vld on 07.01.16.
 *
 */
public class SolmanGuidCField extends GenericTextCFType{

    public SolmanGuidCField(CustomFieldValuePersister customFieldValuePersister,
                            GenericConfigManager genericConfigManager) {

        super(customFieldValuePersister, genericConfigManager);
    }

    public static CustomField getCustomFieldByType(String cFieldType){
        CustomFieldManager customFieldManager =  ComponentAccessor.getCustomFieldManager();

        final List<CustomField> customFieldObjects = customFieldManager.getCustomFieldObjects();

        for (CustomField customFieldObject : customFieldObjects) {
            if (customFieldObject.getCustomFieldType().getKey().equals(cFieldType)){
                return customFieldObject;
            }

        }

        return null;
    }
}
