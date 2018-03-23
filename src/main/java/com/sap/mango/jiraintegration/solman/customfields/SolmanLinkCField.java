package com.sap.mango.jiraintegration.solman.customfields;


import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.GenericTextCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.CustomField;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettings;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettingsDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;

import java.util.List;

/**
 * Created by vld on 07.01.16.
 *
 */
public class SolmanLinkCField extends GenericTextCFType{

    private final SolmanParamsDAO solmanParamsDAO;

    private final JumpUrlSettingsDAO jumpUrlSettigsDAO;

    public SolmanLinkCField(CustomFieldValuePersister customFieldValuePersister,
                            GenericConfigManager genericConfigManager, SolmanParamsDAO solmanParamsDAO, JumpUrlSettingsDAO jumpUrlSettigsDAO) {

        super(customFieldValuePersister, genericConfigManager);
        this.solmanParamsDAO = solmanParamsDAO;
        this.jumpUrlSettigsDAO = jumpUrlSettigsDAO;
    }

    public static String getCustomFieldValueByType(String cFieldType, Issue issue){
        CustomFieldManager customFieldManager =  ComponentAccessor.getCustomFieldManager();

        final List<CustomField> customFieldObjects = customFieldManager.getCustomFieldObjects();

        for (CustomField customFieldObject : customFieldObjects) {
            if (customFieldObject.getCustomFieldType().getKey().equals(cFieldType)){

                Object object = issue.getCustomFieldValue(customFieldObject);
                if (object != null){
                    final String value = object.toString();
                    if (value != null) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getValueFromIssue(CustomField field, Issue issue) {
        final String guid = getCustomFieldValueByType("com.sap.mango.jiraintegration:solman-guid", issue);
        if (guid == null){
            return "No guid maintaned";
        }

        final String customerGuid = getCustomFieldValueByType("com.sap.mango.jiraintegration:solman-customer-guid", issue);
        if (customerGuid == null){
            return "No customer guid maintaned";
        }

        final List<SolmanParams> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(customerGuid, false);
        if (solmanParams.size() != 1){
            return "Invalid configuration for customer guid: " + customerGuid;
        }
        final SolmanParams sp = solmanParams.get(0);

        final JumpUrlSettings jumpUrlSettings = jumpUrlSettigsDAO.getJumpUrlSettings(sp.customerGuid);

        if (jumpUrlSettings == null) {
            return "Invalid configuration for customer guid: " + customerGuid;
        }

        if (issue.getIssueTypeObject().getName().toUpperCase().contains("EPIC")){
            //return "<a href='https://hostname:port/sap/bc/ui5_ui5/salm/ost_wp/index.html?WP_GUID=" +  guid  +  " '>WP in SolMan</a>";
            return "<a href='" + jumpUrlSettings.getWorkPackageAppJumpUrl() +  guid  +  "' target='_blank'>WP in SolMan</a>";
        }else if (issue.getIssueTypeObject().getName().toUpperCase().contains("STORY")){
            //return "<a href='https://hostname:port/sap/bc/ui5_ui5/salm/ost_wi/index.html?WS_GUID=" +  guid  +  " '>WI in SolMan</a>";
            return "<a href='" + jumpUrlSettings.getWorkItemAppJumpUrl() +  guid  +  "' target='_blank'>WI in SolMan</a>";
        }else{
            return "Unknown issue type";
        }
    }
}
