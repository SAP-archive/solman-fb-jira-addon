package com.sap.mango.jiraintegration.solman.webservices.odata.userstory;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.config.IssueTypeManager;
import com.atlassian.jira.config.PriorityManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.context.GlobalIssueContext;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.customfields.option.Option;
import com.atlassian.jira.issue.customfields.option.Options;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.issue.priority.Priority;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.sap.mango.jiraintegration.solman.customfields.SolmanGuidCField;
import com.sap.mango.jiraintegration.solman.entities.appointmentmapping.AppointmentMapping;
import com.sap.mango.jiraintegration.solman.entities.appointmentmapping.AppointmentMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMapping;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.fileinfo.AttachmentTypeEnum;
import com.sap.mango.jiraintegration.solman.entities.fileinfo.FileInfoDAO;
import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMapping;
import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMapping;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping.PartnerFieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping.PartnerFldMap;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMapping;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMapping;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMapping;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMappingDAO;
import com.sap.mango.jiraintegration.utils.ODataUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.olingo.commons.api.data.*;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Service class, that saves Jira issue (Epic or Story)
 */
public class IssueEntityStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(IssueEntityStorageService.class);

    private IssueService issueService;
    private ProjectMappingDAO projectMappingDAO;
    private ProjectManager projectManager;
    private IssueTypeManager issueTypeManager;
    private JiraAuthenticationContext authenticationContext;
    private IssueTypeMappingDAO issueTypeMappingDAO;
    private CustomFieldManager customFieldManager;
    private WorkflowManager workflowManager;
    private IssueStatusMappingDAO issueStatusMappingDAO;
    private FieldMappingDAO fieldMappingDAO;
    private OptionsManager optionsManager;
    private PartnerFieldMappingDAO partnerFieldMappingDAO;
    private AppointmentMappingDAO appointmentMappingDAO;
    private TextFieldMappingDAO textFieldMappingDAO;
    private SolmanParamsDAO solmanParamsDAO;
    private FileInfoDAO fileInfoDAO;
    private PriorityMappingDAO priorityMappingDAO;
    private PriorityManager priorityManager;


    public IssueEntityStorageService(final IssueService issueService, final CustomFieldManager customFieldManager, final OptionsManager optionsManager,
                                     final ProjectMappingDAO projectMappingDAO, final IssueTypeMappingDAO issueTypeMappingDAO, final ProjectManager projectManager,
                                     final IssueTypeManager issueTypeManager, final JiraAuthenticationContext authenticationContext,
                                     final WorkflowManager workflowManager, final IssueStatusMappingDAO issueStatusMappingDAO, final FieldMappingDAO fieldMappingDAO,
                                     final PartnerFieldMappingDAO partnerFieldMappingDAO, final AppointmentMappingDAO appointmentMappingDAO,
                                     final TextFieldMappingDAO textFieldMappingDAO, final SolmanParamsDAO solmanParamsDAO, final FileInfoDAO fileInfoDAO,
                                     final PriorityMappingDAO priorityMappingDAO, final PriorityManager priorityManager) {
        this.issueService = issueService;
        this.projectManager = projectManager;
        this.customFieldManager = customFieldManager;
        this.issueTypeManager = issueTypeManager;
        this.projectMappingDAO = projectMappingDAO;
        this.issueTypeMappingDAO = issueTypeMappingDAO;
        this.authenticationContext = authenticationContext;
        this.workflowManager = workflowManager;
        this.issueStatusMappingDAO = issueStatusMappingDAO;
        this.fieldMappingDAO = fieldMappingDAO;
        this.optionsManager = optionsManager;
        this.partnerFieldMappingDAO = partnerFieldMappingDAO;
        this.appointmentMappingDAO = appointmentMappingDAO;
        this.textFieldMappingDAO = textFieldMappingDAO;
        this.solmanParamsDAO = solmanParamsDAO;
        this.fileInfoDAO = fileInfoDAO;
        this.priorityMappingDAO = priorityMappingDAO;
        this.priorityManager = priorityManager;
    }

    public Entity createIssue(Entity requestEntity, EdmEntityType edmEntityType, EdmEntitySet edmEntitySet) throws ODataApplicationException {
        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        Property solManCustGuiD = requestEntity.getProperty("SYSTEM_GUID");
        Property solManProjectID = requestEntity.getProperty("PPM_PROJECT_ID");
        Property description = requestEntity.getProperty("DESCRIPTION");
        Property createdBy = requestEntity.getProperty("CREATED_BY");
        Property processType = requestEntity.getProperty("PROCESS_TYPE");
        Property parentExternalID = requestEntity.getProperty("PARENT_EXTERNAL_ID");
        Property effort = requestEntity.getProperty("EFFORT");
        Property effortUnit = requestEntity.getProperty("EFFORT_UNIT");
        Property solmanPriority = requestEntity.getProperty("PRIORITY");

        ProjectMapping projectMapping = projectMappingDAO.getProjectTypeMapping(solManCustGuiD.getValue().toString(), solManProjectID.getValue().toString());
        if (projectMapping == null) {
            throw new ODataApplicationException("Not existing relevant Jira project for SolMan Project" + solManProjectID, HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_PROJECT_MAPPING);
        }

        IssueTypeMapping issueTypeMapping = issueTypeMappingDAO.getIssueTypeMapping(solManCustGuiD.getValue().toString(), processType.getValue().toString());
        if (issueTypeMapping == null) {
            throw new ODataApplicationException("Not existing relevant Issue mapping for Process Type" + processType.getValue().toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_ISSUE_MAPPING);
        }

        IssueType issueType = issueTypeManager.getIssueType(issueTypeMapping.getJiraIssueType().toString());

        if (issueType == null) {
            throw new ODataApplicationException("Not existing jira issue type with id = " + issueTypeMapping.getJiraIssueType().toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_ISSUE_TYPE);
        }

        PriorityMapping priorityMapping = priorityMappingDAO.getPriorityMapping(solManCustGuiD.getValue().toString(), solmanPriority.getValue().toString());

        if (priorityMapping == null) {
            throw new ODataApplicationException("Not existing Priority mapping with priority id = " + solmanPriority.getValue().toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_PRIORITY_MAPPING);
        }

        Priority priority = priorityManager.getPriority(priorityMapping.getJiraPriority());
        Project project = projectManager.getProjectObjByKey(projectMapping.getJiraProjectID());

        issueInputParameters.setProjectId(project.getId());
        issueInputParameters.setIssueTypeId(issueType.getId());
        issueInputParameters.setDescription(description.getValue().toString());
        issueInputParameters.setPriorityId(priority.getId());
        issueInputParameters.setReporterId(createdBy.getValue().toString()).setAssigneeId(createdBy.getValue().toString());
        issueInputParameters.setSummary(description.getValue().toString());
        issueInputParameters.setOriginalEstimate(calculateOriginalEstimate(Double.valueOf(effort.getValue().toString()).longValue(), effortUnit.getValue().toString()));

        fillIssueCustomFields(requestEntity, issueType, issueInputParameters);

        if (StringUtils.isNotEmpty(parentExternalID.getValue().toString()) && issueType.getName().equals("Epic")) {
            throw new ODataApplicationException("Not allowed to relate an epic to epic " + parentExternalID.getValue().toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.FORBIDDEN_LINK_OPERATION);
        }

        Entity resultEntity = createIssue(requestEntity, project, issueInputParameters, edmEntitySet, edmEntityType);
        return resultEntity;
    }

    private ActionDescriptor getActionDescriptor(JiraWorkflow jiraWorkflow, String jiraTransition) {
        for (ActionDescriptor actionDescriptor : jiraWorkflow.getAllActions()) {
            if (actionDescriptor.getName().equals(jiraTransition)) {
                return actionDescriptor;
            }
        }
        return null;
    }

    private String transformErrorMessages(Collection<String> errors) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String error : errors) {
            stringBuilder.append(error + System.getProperty("line.separator"));
        }
        return stringBuilder.toString();
    }

    public void fillIssueCustomFields(Entity requestEntity, IssueType issueType, IssueInputParameters issueInputParameters) throws ODataApplicationException {

        Property ticketGuid = requestEntity.getProperty("TICKET_GUID");
        Property solManProjectID = requestEntity.getProperty("PPM_PROJECT_ID");
        Property solManCustGuiD = requestEntity.getProperty("SYSTEM_GUID");
        Property processType = requestEntity.getProperty("PROCESS_TYPE");
        Property classification = requestEntity.getProperty("CLASSIFICATION");
        Property description = requestEntity.getProperty("DESCRIPTION");
        Property parentExternalID = requestEntity.getProperty("PARENT_EXTERNAL_ID");


        CustomField solManCustGuidCF = SolmanGuidCField.getCustomFieldByType("com.sap.mango.jiraintegration:solman-customer-guid");

        if (solManCustGuidCF == null) {
            throw new ODataApplicationException("Not existing 'Solman customer guid' custom field", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
        }
        CustomField solManGuidCF = SolmanGuidCField.getCustomFieldByType("com.sap.mango.jiraintegration:solman-guid");

        if (solManGuidCF == null) {
            throw new ODataApplicationException("Not existing 'Solman guid' custom field", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
        }

        issueInputParameters.addCustomFieldValue(solManCustGuidCF.getId(), solManCustGuiD.getValue().toString());
        issueInputParameters.addCustomFieldValue(solManGuidCF.getId(), ticketGuid.getValue().toString());

        if (issueType.getName().equals("Epic")) {

            if (!ODataUtil.isEmpty(description)) {
                FieldMapping epicNameFieldMapping =  fieldMappingDAO.getFieldMapping(solManCustGuiD.getValue().toString(), processType.getValue().toString(), description.getName());
                if (epicNameFieldMapping == null) {
                    throw new ODataApplicationException("Not finded mapping for DESCRIPTION/Epic name", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
                }
                CustomField epicNameCustomField = customFieldManager.getCustomFieldObject(epicNameFieldMapping.getJiraField());
                if (epicNameCustomField == null) {
                    throw new ODataApplicationException("Not existing custom field with id = " + epicNameFieldMapping.getJiraField(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
                }
                issueInputParameters.addCustomFieldValue(epicNameCustomField.getId(), description.getValue().toString());
            }

            if (!ODataUtil.isEmpty(solManProjectID)) {
                FieldMapping solManProjectFieldMapping = fieldMappingDAO.getFieldMapping(solManCustGuiD.getValue().toString(), processType.getValue().toString(), solManProjectID.getName());
                if (solManProjectFieldMapping == null) {
                    throw new ODataApplicationException("Not finded mapping for PPM_PROJECT_ID/SolMan Project ID", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
                }
                CustomField solManProjectCustomField = customFieldManager.getCustomFieldObject(solManProjectFieldMapping.getJiraField());
                if (solManProjectCustomField == null) {
                    throw new ODataApplicationException("Not existing custom field with id = " + solManProjectFieldMapping.getJiraField(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
                }
                issueInputParameters.addCustomFieldValue(solManProjectCustomField.getId(), solManProjectID.getValue().toString());
            }
        }

        if (!ODataUtil.isEmpty(parentExternalID)) {
            FieldMapping epicLinkFieldMapping = fieldMappingDAO.getFieldMapping(solManCustGuiD.getValue().toString(), processType.getValue().toString(), parentExternalID.getName());
            if (epicLinkFieldMapping == null) {
                throw new ODataApplicationException("Not finded mapping for PARENT_EXTERNAL_ID/Epic link", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
            }
            CustomField epicLinkCustomField = customFieldManager.getCustomFieldObject(epicLinkFieldMapping.getJiraField());
            if (epicLinkCustomField == null) {
                throw new ODataApplicationException("Not existing custom field with id = " + epicLinkFieldMapping.getJiraField(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
            }
            issueInputParameters.addCustomFieldValue(epicLinkCustomField.getId(), parentExternalID.getValue().toString());
        }

        if (!ODataUtil.isEmpty(classification)) {
            CustomField classificationCustomField = customFieldManager.getCustomFieldObjectByName(IssueCustomFieldsNames.classificationCFName);
            if (classificationCustomField == null) {
                throw new ODataApplicationException("Not existing 'Classification' custom field", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
            }
            Options options = optionsManager.getOptions(classificationCustomField.getRelevantConfig(GlobalIssueContext.getInstance()));
            Option newClassificationOption = options.getOptionForValue(getClassificationId(classification), null);
            issueInputParameters.addCustomFieldValue(classificationCustomField.getId(), newClassificationOption.getOptionId().toString());
        }
    }

    private String getClassificationId(Property classificationPropertyTxt) throws ODataApplicationException {
        switch (classificationPropertyTxt.getValue().toString()) {
            case "1":
                return "WRICEF";
            case "F":
                return "Fit";
            case "G":
                return "Gap";
            case "N":
                return "Non-Functional";
        }
        throw new ODataApplicationException("Not valid classificationCFName value " + classificationPropertyTxt.getValue().toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
    }

    private Long calculateOriginalEstimate(Long effort, String effortUnit) throws ODataApplicationException {
        Integer daysPerWeek = Integer.valueOf(5);
        Integer hoursPerDay = Integer.valueOf(8);
        Integer minutesPerHour = 60;
        switch (effortUnit) {
            case "W":
                return Long.valueOf(effort * daysPerWeek * hoursPerDay * minutesPerHour);
            case "D":
                return Long.valueOf(effort * hoursPerDay * minutesPerHour);
            case "H":
                return Long.valueOf(effort * minutesPerHour);
            case "MIN":
                return effort;
            case "":
                return effort;
            default:
                throw new ODataApplicationException("Not valid effort unit", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_VALID_EFFORT_UNIT);
        }
    }

    public Entity applyNavigationProperties(EdmEntitySet edmEntitySet, EdmEntityType edmEntityType, Entity entity, String rawServiceUri, IssueInputParameters issueInputParameters) throws ODataApplicationException {

        for (final Link link : entity.getNavigationLinks()) {
            final EdmNavigationProperty edmNavigationProperty = edmEntityType.getNavigationProperty(link.getTitle());
            final EdmEntitySet targetEntitySet = (EdmEntitySet) edmEntitySet.getRelatedBindingTarget(link.getTitle());

            if (edmNavigationProperty.isCollection() && link.getInlineEntitySet() != null && link.getInlineEntitySet().getEntities().size() != 0) {
                createEntityData(targetEntitySet, entity, link.getInlineEntitySet(), null, issueInputParameters);
            }
        }

        return entity;
    }

    public Entity createEntityData(EdmEntitySet edmEntitySet, Entity parentEntity, EntityCollection entityCollection, String rawServiceUri, IssueInputParameters issueInputParameters) throws ODataApplicationException {
        if (edmEntitySet.getName().equals(IssueEdmProvider.ES_WRICEF_ATTRIBUTES_NAME)) {
            applyWricefAttributes(parentEntity, entityCollection, issueInputParameters);
        } else if (edmEntitySet.getName().equals(IssueEdmProvider.ES_PARTNERS_NAME)) {
            applyPartnerFields(parentEntity, entityCollection, issueInputParameters);
        } else if (edmEntitySet.getName().equals(IssueEdmProvider.ES_APPOINTMENTS_NAME)) {
            applyAppointmentTypeField(parentEntity, entityCollection, issueInputParameters);
        } else if (edmEntitySet.getName().equals(IssueEdmProvider.ES_TEXTS_NAME)) {
            applyTextFields(parentEntity, entityCollection, issueInputParameters);
        } else if (edmEntitySet.getName().equals(IssueEdmProvider.ES_CATEGORIES_NAME)) {

        } else if (edmEntitySet.getName().equals(IssueEdmProvider.ES_PROCESS_STRUCTS_NAME)) {

        } else if (edmEntitySet.getName().equals(IssueEdmProvider.ES_ATTACHMENTS_NAME)) {
            applyAttachmentLinks(parentEntity, entityCollection, issueInputParameters);
        } else if (edmEntitySet.getName().equals(IssueEdmProvider.ES_DOCUMENTS_NAME)) {
            applyDocumentsLinks(parentEntity, entityCollection, issueInputParameters);
        }

        return null;
    }

    public Entity updateIssue(Entity updatedEntity, EdmEntityType edmEntityType, EdmEntitySet edmEntitySet, List<UriParameter> uriParameterList) throws ODataApplicationException {
        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        Property solManCustGuiD = updatedEntity.getProperty("SYSTEM_GUID");
        Property solManProjectID = updatedEntity.getProperty("PPM_PROJECT_ID");
        Property description = updatedEntity.getProperty("DESCRIPTION");
        Property createdBy = updatedEntity.getProperty("CREATED_BY");
        Property processType = updatedEntity.getProperty("PROCESS_TYPE");
        Property parentExternalID = updatedEntity.getProperty("PARENT_EXTERNAL_ID");
        Property status = updatedEntity.getProperty("STATUS");
        Property effort = updatedEntity.getProperty("EFFORT");
        Property effortUnit = updatedEntity.getProperty("EFFORT_UNIT");

        String issueId = StringUtils.replace(ODataUtil.getKeyValue(edmEntityType, uriParameterList), "'", "");

        MutableIssue issue = issueService.getIssue(this.authenticationContext.getLoggedInUser(), issueId).getIssue();

        if (issue == null) {
            throw new ODataApplicationException("Not existing issue with key " + issueId, HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_PROJECT_MAPPING);
        }

        ProjectMapping projectMapping = projectMappingDAO.getProjectTypeMapping(solManCustGuiD.getValue().toString(), solManProjectID.getValue().toString());
        if (projectMapping == null) {
            throw new ODataApplicationException("Not existing relevant Jira project for SolMan Project" + solManProjectID, HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_PROJECT_MAPPING);
        }

        IssueTypeMapping issueTypeMapping = issueTypeMappingDAO.getIssueTypeMapping(solManCustGuiD.getValue().toString(), processType.getValue().toString());
        if (issueTypeMapping == null) {
            throw new ODataApplicationException("Not existing relevant Issue mapping for Process Type" + processType.getValue().toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_ISSUE_MAPPING);
        }

        IssueStatusMapping issueStatusMapping = issueStatusMappingDAO.getIssueStatusMapping(solManCustGuiD.getValue().toString(), processType.getValue().toString(), status.getValue().toString());

        if (issueStatusMapping == null) {
            throw new ODataApplicationException("Not existing relevant Issue Inbound Status Mapping for SolMan Process Type " + processType.getValue().toString()
                    + " and SolMan Status " + status.getValue().toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_ISSUE_MAPPING);
        }

        IssueType issueType = issueTypeManager.getIssueType(issueTypeMapping.getJiraIssueType().toString());

        if (issueType == null) {
            throw new ODataApplicationException("Not existing jira issue type with id = " + issueTypeMapping.getJiraIssueType().toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_ISSUE_TYPE);
        }

        Project project = projectManager.getProjectObjByKey(projectMapping.getJiraProjectID());

        issueInputParameters.setProjectId(project.getId());
        issueInputParameters.setIssueTypeId(issueType.getId());
        issueInputParameters.setDescription(description.getValue().toString());
        issueInputParameters.setPriorityId("1");
        issueInputParameters.setReporterId(createdBy.getValue().toString()).setAssigneeId(createdBy.getValue().toString());
        issueInputParameters.setSummary(description.getValue().toString());
        issueInputParameters.setOriginalEstimate(calculateOriginalEstimate(Double.valueOf(effort.getValue().toString()).longValue(), effortUnit.getValue().toString()));
        fillIssueCustomFields(updatedEntity, issueType, issueInputParameters);

        if (StringUtils.isNotEmpty(parentExternalID.getValue().toString()) && issueType.getName().equals("Epic")) {
            throw new ODataApplicationException("Not allowed to relate an epic to epic " + parentExternalID.getValue().toString(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.FORBIDDEN_LINK_OPERATION);
        }
        Issue updatedIssue = updateIssue(issue, issueInputParameters);
        updatedEntity.getProperties().add(new Property(null, "EXTERNAL_ID", ValueType.PRIMITIVE, issue.getKey()));

        //update issue status
        updateIssueStatus(issue, issueStatusMapping, issueInputParameters);
        return updatedEntity;
    }

    public Entity createIssue(Entity requestEntity, Project project, IssueInputParameters issueInputParameters, EdmEntitySet edmEntitySet, EdmEntityType edmEntityType) throws ODataApplicationException {
        Entity resultEntity = new Entity();
        applyNavigationProperties(edmEntitySet, edmEntityType, requestEntity, null, issueInputParameters);

        IssueService.CreateValidationResult createValidationResult = issueService.validateCreate(this.authenticationContext.getLoggedInUser(), issueInputParameters);
        if (createValidationResult.isValid()) {
            IssueService.IssueResult createResult = issueService.create(this.authenticationContext.getLoggedInUser(), createValidationResult);
            Issue issue = createResult.getIssue();
            resultEntity.setId(URI.create(issue.getKey()));
            LOG.debug("Succesfully created issue with id: " + issue.getKey() + " in project " + project.getName());
            resultEntity.getProperties().add(new Property(null, "EXTERNAL_ID", ValueType.PRIMITIVE, issue.getKey()));
            return resultEntity;
        } else {
            throw new ODataApplicationException("Internal problem while creating issue for project " + project.getKey() + ". " +
                    transformErrorMessages(createValidationResult.getErrorCollection().getErrors().values()), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.INTERNAL_ISSUE_CREATION_ERROR);
        }
    }

    public MutableIssue updateIssue(MutableIssue issue, IssueInputParameters issueInputParameters) throws ODataApplicationException {
        IssueService.UpdateValidationResult updateValidationResult = issueService.validateUpdate(this.authenticationContext.getLoggedInUser(), issue.getId(), issueInputParameters);
        if (updateValidationResult.isValid()) {
            IssueService.IssueResult updateResult = issueService.update(this.authenticationContext.getLoggedInUser(), updateValidationResult);
            LOG.debug("Succesfully updated issue with id: " + issue.getKey() + " in project " + issue.getProjectObject().getName());
            return updateResult.getIssue();
        } else {
            throw new ODataApplicationException("Internal problem while updating issue for project " + issue.getProjectObject().getKey() + ". " +
                    transformErrorMessages(updateValidationResult.getErrorCollection().getErrorMessages()), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.INTERNAL_ISSUE_CREATION_ERROR);
        }
    }

    public void updateIssueStatus(Issue issue, IssueStatusMapping issueStatusMapping, IssueInputParameters issueInputParameters) throws ODataApplicationException {
        JiraWorkflow jiraWorkflow = workflowManager.getWorkflow(issue);

        ActionDescriptor actionDescriptor = getActionDescriptor(jiraWorkflow, issueStatusMapping.getJiraTransition());

        if (actionDescriptor == null) {
            throw new ODataApplicationException("Not existing transition with name " + issueStatusMapping.getJiraTransition() + " in workflow " + jiraWorkflow.getName() + " for jira type " + issue.getIssueTypeObject().getName(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_ISSUE_TYPE);
        }

        IssueService.TransitionValidationResult transitionValidationResult = issueService.validateTransition(this.authenticationContext.getLoggedInUser(), issue.getId(), actionDescriptor.getId(), issueInputParameters);
        if (transitionValidationResult.isValid()) {
            IssueService.IssueResult issueResult = issueService.transition(this.authenticationContext.getLoggedInUser(), transitionValidationResult);

        } else {
            throw new ODataApplicationException("Internal problem while executing transition " + actionDescriptor.getName() +
                    transformErrorMessages(transitionValidationResult.getErrorCollection().getErrorMessages()), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.INTERNAL_ISSUE_CREATION_ERROR);
        }
    }

    public void applyWricefAttributes(Entity parentEntity, EntityCollection wricefAttributes, IssueInputParameters issue) throws ODataApplicationException {
        List<String> wricefOptions = new ArrayList<>();

        CustomField wricefAttributesCF = customFieldManager.getCustomFieldObjectByName(IssueCustomFieldsNames.wricefAttributesCFName);
        if (wricefAttributesCF == null) {
            throw new ODataApplicationException("Not existing 'Wricef Attributes' custom field", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_EXISTING_FIELD_MAPPING);
        }
        Options options = optionsManager.getOptions(wricefAttributesCF.getRelevantConfig(GlobalIssueContext.getInstance()));

        for (Entity wricefAttribute : wricefAttributes.getEntities()) {
            Property key = wricefAttribute.getProperty("KEY");
            String optionValue = WricefElementEnum.getValueByKey(key.getValue().toString());
            if (optionValue == null) {
                LOG.error("Not existing Wricef Attribute with key " + key.getValue().toString());
                continue;
            }
            wricefOptions.add(options.getOptionForValue(optionValue, null).getOptionId().toString());
        }

        issue.addCustomFieldValue(wricefAttributesCF.getId(), wricefOptions.toArray(new String[wricefOptions.size()]));
    }

    public void applyPartnerFields(Entity parentEntity, EntityCollection partners, IssueInputParameters issueInputParameters) throws ODataApplicationException {
        Property solManCustGuiD = parentEntity.getProperty("SYSTEM_GUID");
        Property solmanProcessType = parentEntity.getProperty("PROCESS_TYPE");

        Map<String, List<PartnerFunctionBean>> partnerFunctions = new HashMap<>();

        for (Entity partner : partners.getEntities()) {
            Property partnerFct = partner.getProperty("PARTNER_FCT");
            Property isMain = partner.getProperty("PARTNER_MAIN");
            Property partnerNameField = partner.getProperty("PARTNER_NAME");
            Property partnerEmailField = partner.getProperty("PARTNER_EMAIL");
            List<PartnerFunctionBean> partnerFunctionBeanList = partnerFunctions.get(partnerFct.getValue().toString());
            if (partnerFunctionBeanList == null) {
                partnerFunctionBeanList = new ArrayList<>();
            }
            partnerFunctionBeanList.add(new PartnerFunctionBean(partnerFct.getValue().toString(), partnerNameField.getValue().toString(), partnerEmailField.getValue().toString(), ODataUtil.isEmpty(isMain)));
            partnerFunctions.put(partnerFct.getValue().toString(), partnerFunctionBeanList);
        }

        for (String partnerFct : partnerFunctions.keySet()) {
            //we take the main partner field or the first one if main is not present
            PartnerFunctionBean partnerFunctionBean = getPartnerFunctionBean(partnerFunctions.get(partnerFct));
            PartnerFldMap partnerNameFldMap = partnerFieldMappingDAO.getFieldMapping(solManCustGuiD.getValue().toString(), solmanProcessType.getValue().toString(), partnerFct, "PARTNER_NAME");
            PartnerFldMap partnerEmailFldMap = partnerFieldMappingDAO.getFieldMapping(solManCustGuiD.getValue().toString(), solmanProcessType.getValue().toString(), partnerFct, "PARTNER_EMAIL");

            if (partnerNameFldMap != null) {
                CustomField partnerNameCF = customFieldManager.getCustomFieldObject(partnerNameFldMap.getJiraField());
                if (partnerNameCF == null) {
                    throw new ODataApplicationException("Not existing PARTNER NAME custom field with id = " + partnerNameFldMap.getJiraField(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.INTERNAL_ISSUE_CREATION_ERROR);
                }
                issueInputParameters.addCustomFieldValue(partnerNameCF.getId(), partnerFunctionBean.getPartnerName());
            }

            if (partnerEmailFldMap != null) {
                CustomField partnerEmailCF = customFieldManager.getCustomFieldObject(partnerEmailFldMap.getJiraField());
                if (partnerEmailCF == null) {
                    throw new ODataApplicationException("Not existing PARTNER EMAIL custom field with id = " + partnerEmailFldMap.getJiraField(), HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.INTERNAL_ISSUE_CREATION_ERROR);
                }
                issueInputParameters.addCustomFieldValue(partnerEmailCF.getId(), partnerFunctionBean.getPartnerEmail());
            }
        }
    }

    private PartnerFunctionBean getPartnerFunctionBean(List<PartnerFunctionBean> partnerFunctionBeans) {
        for (PartnerFunctionBean partnerFunctionBean : partnerFunctionBeans) {
            if (partnerFunctionBean.getMain()) {
                return partnerFunctionBean;
            }
        }
        return partnerFunctionBeans.get(0);
    }

    public void applyAppointmentTypeField(Entity parentEntity, EntityCollection appointments, IssueInputParameters issueInputParameters) throws ODataApplicationException {
        Property solManCustGuiD = parentEntity.getProperty("SYSTEM_GUID");
        Property solmanProcessType = parentEntity.getProperty("PROCESS_TYPE");

        Entity appointment = null;
        AppointmentMapping appointmentMapping = null;

        for (Entity appointEntity : appointments.getEntities()) {
            Property appointmentType = appointEntity.getProperty("APPT_TYPE");

            appointmentMapping = appointmentMappingDAO.getFieldMapping(solManCustGuiD.getValue().toString(), solmanProcessType.getValue().toString(), appointmentType.getValue().toString());
            if (appointmentMapping == null) {
                continue;
            }

            appointment = appointEntity;

            Property dueDateTimeStamp = appointment.getProperty("TIMESTAMP");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            Date dueDate = null;
            try {
                dueDate = simpleDateFormat.parse(dueDateTimeStamp.getValue().toString());
            } catch (ParseException e) {
                throw new ODataApplicationException("The due date should be in the format yyyyMMddHHmmss", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, IssueEntityStorageErrorCodes.NOT_VALID_DATE_FORMAT);
            }
            CustomField appointmentCF = customFieldManager.getCustomFieldObject(appointmentMapping.getJiraField());
            if (appointmentCF != null) {
                issueInputParameters.addCustomFieldValue(appointmentCF.getId(), new SimpleDateFormat("d/MMM/yy").format(dueDate));
                continue;
            }
            issueInputParameters.setDueDate(new SimpleDateFormat("d/MMM/yy").format(dueDate));
        }
    }

    public void applyTextFields(Entity parentEntity, EntityCollection texts, IssueInputParameters issueInputParameters) throws ODataApplicationException {
        Property solManCustGuiD = parentEntity.getProperty("SYSTEM_GUID");
        Property solmanProcessType = parentEntity.getProperty("PROCESS_TYPE");

        TextFieldMapping textFieldMapping = null;
        Entity textEntity = null;

        for (Entity txtEntity : texts.getEntities()) {
            Property textID = txtEntity.getProperty("TEXTID");
            textFieldMapping = textFieldMappingDAO.getFieldMapping(solManCustGuiD.getValue().toString(), solmanProcessType.getValue().toString(), textID.getValue().toString());

            if (textFieldMapping == null) {
                continue;
            }

            textEntity = txtEntity;

            Property text = textEntity.getProperty("TEXT");
            String description = StringUtils.replace(text.getValue().toString(), "\\n", "\n");

            CustomField descriptionCF = customFieldManager.getCustomFieldObject(textFieldMapping.getJiraField());
            if (descriptionCF != null) {
                issueInputParameters.addCustomFieldValue(descriptionCF.getId(), description);
                continue;
            }
            issueInputParameters.setDescription(description);
        }
    }

    public void applyAttachmentLinks(Entity parentEntity, EntityCollection attachments, IssueInputParameters issueInputParameters) {
        Property ticketGuid = parentEntity.getProperty("TICKET_GUID");
        Property solManCustGuiD = parentEntity.getProperty("SYSTEM_GUID");

        for (Entity attachment : attachments.getEntities()) {
            Property url = attachment.getProperty("URL");
            Property extension = attachment.getProperty("EXTENSION");
            Property filename = attachment.getProperty("FILENAME");
            Property technFileName = attachment.getProperty("TECHN_FILENAME");

            SolmanParamsAO solmanParamsAO = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD.getValue().toString()).get(0);

            fileInfoDAO.saveFileInfo(ticketGuid.getValue().toString(), filename.getValue().toString(), technFileName.getValue().toString(),
                    extension.getValue().toString(), url.getValue().toString(), new Date(), AttachmentTypeEnum.ATTACHMENT.getValue(), solmanParamsAO);
        }
    }

    public void applyDocumentsLinks(Entity parentEntity, EntityCollection documents, IssueInputParameters issueInputParameters) {
        Property ticketGuid = parentEntity.getProperty("TICKET_GUID");
        Property solManCustGuiD = parentEntity.getProperty("SYSTEM_GUID");

        for (Entity document : documents.getEntities()) {
            Property url = document.getProperty("URL");
            Property filename = document.getProperty("FILENAME");
            String extension = "DOCX";

            SolmanParamsAO solmanParamsAO = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD.getValue().toString()).get(0);
            fileInfoDAO.saveFileInfo(ticketGuid.getValue().toString(), filename.getValue().toString(), filename.getValue().toString(),
                    extension, url.getValue().toString(), new Date(), AttachmentTypeEnum.DOCUMENT.getValue(), solmanParamsAO);
        }
    }
}
