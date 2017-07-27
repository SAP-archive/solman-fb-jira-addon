package com.sap.mango.jiraintegration.solman.webservices.odata.userstory;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.apache.olingo.commons.api.ex.ODataException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class, that declares the metadata of the OData web service, url: http://localhost:2990/jira/plugins/servlet/IssueService.svc/$metadata
 */
public class
IssueEdmProvider extends org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider {

    public static final String NAMESPACE = "OData.Issue";

    //edm container
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    //entity type names
    public static final String ET_USERSTORY_NAME = "Issue";
    public static final FullQualifiedName ET_USERSTORY_FQN = new FullQualifiedName(NAMESPACE, ET_USERSTORY_NAME);

    public static final String ET_WRICEF_ATTRIBUTE_NAME = "WRICEF_ATTRIBUTES";
    public static final FullQualifiedName ET_WRICEF_ATTRIBUTE_FQN = new FullQualifiedName(NAMESPACE, ET_WRICEF_ATTRIBUTE_NAME);

    public static final String ET_PARTNER_NAME = "PARTNER";
    public static final FullQualifiedName ET_PARTNER_FQN = new FullQualifiedName(NAMESPACE, ET_PARTNER_NAME);

    public static final String ET_APPOITMENT_NAME = "APPOINTMENT";
    public static final FullQualifiedName ET_APPOITMENT_FQN = new FullQualifiedName(NAMESPACE, ET_APPOITMENT_NAME);

    public static final String ET_TEXT_NAME = "TEXT";
    public static final FullQualifiedName ET_TEXT_FQN = new FullQualifiedName(NAMESPACE, ET_TEXT_NAME);

    public static final String ET_CATEGORY_NAME = "CATEGORY";
    public static final FullQualifiedName ET_CATEGORY_FQN = new FullQualifiedName(NAMESPACE, ET_CATEGORY_NAME);

    public static final String ET_PROCESS_STRUCT_NAME = "PROCESS_STRUCT";
    public static final FullQualifiedName ET_PROCESS_STRUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PROCESS_STRUCT_NAME);

    public static final String ET_DOCUMENT_NAME = "DOCUMENT";
    public static final FullQualifiedName ET_DOCUMENT_FQN = new FullQualifiedName(NAMESPACE, ET_DOCUMENT_NAME);

    public static final String ET_ATTACHMENT_NAME = "ATTACHMENT";
    public static final FullQualifiedName ET_ATTACHMENT_FQN = new FullQualifiedName(NAMESPACE, ET_ATTACHMENT_NAME);


    //entity set names
    public static final String ES_USERSTORIES_NAME = "Issues";
    public static final String ES_WRICEF_ATTRIBUTES_NAME = "WRICEF_ATTRIBUTES";
    public static final String ES_PARTNERS_NAME = "PARTNER";
    public static final String ES_TEXTS_NAME = "TEXT";
    public static final String ES_APPOINTMENTS_NAME = "APPOINTMENT";
    public static final String ES_DOCUMENTS_NAME = "DOCUMENT";
    public static final String ES_ATTACHMENTS_NAME = "ATTACHMENT";
    public static final String ES_PROCESS_STRUCTS_NAME = "PROCESS_STRUCT";
    public static final String ES_CATEGORIES_NAME = "CATEGORY";



    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
        CsdlEntityType entityType = null;

        if (entityTypeName.equals(ET_USERSTORY_FQN)) {
            List<CsdlProperty> csdlProperties = new ArrayList<>();
            //create entity type properties
            //Customer SolMan GUID
            CsdlProperty customerSolManGuiD = new CsdlProperty().setName("SYSTEM_GUID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(customerSolManGuiD);
            //EXTERNAL ID
            CsdlProperty externalId = new CsdlProperty().setName("EXTERNAL_ID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(externalId);
            //PARENT_EXTERNAL_ID
            CsdlProperty parentExternalId = new CsdlProperty().setName("PARENT_EXTERNAL_ID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(parentExternalId);
            //SolMan ID
            CsdlProperty solManId = new CsdlProperty().setName("TICKET_ID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(solManId);
            //SolMan GuiD
            CsdlProperty solManGuid = new CsdlProperty().setName("TICKET_GUID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(solManGuid);
            //Description
            CsdlProperty description = new CsdlProperty().setName("DESCRIPTION").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(description);
            //Issue Type
            CsdlProperty issueType = new CsdlProperty().setName("PROCESS_TYPE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(issueType);
            //CREATED_AT
            CsdlProperty createdAt = new CsdlProperty().setName("CREATED_AT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(createdAt);
            //CREATED BY
            CsdlProperty createdBy = new CsdlProperty().setName("CREATED_BY").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(createdBy);
            //CHANGED_AT
            CsdlProperty changedAt = new CsdlProperty().setName("CHANGED_AT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(changedAt);
            //CHANGED_BY
            CsdlProperty changedBy = new CsdlProperty().setName("CHANGED_BY").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(changedBy);
            //Status
            CsdlProperty status = new CsdlProperty().setName("STATUS").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(status);
            //STATUS_TXT
            CsdlProperty statusTxt = new CsdlProperty().setName("STATUS_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(statusTxt);
            //SolDoc Project
            CsdlProperty soldocProject = new CsdlProperty().setName("SOLDOC_PROJECT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(soldocProject);
            //Soldoc Project Txt
            CsdlProperty soldocProjectTxt = new CsdlProperty().setName("SOLDOC_PROJECT_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(soldocProjectTxt);
            //Priority
            CsdlProperty priority = new CsdlProperty().setName("PRIORITY").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(priority);
            //Priority txt
            CsdlProperty priorityTxt = new CsdlProperty().setName("PRIORITY_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(priorityTxt);
            //Impact
            CsdlProperty impact = new CsdlProperty().setName("IMPACT").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(impact);
            //Impact Txt
            CsdlProperty impactTxt = new CsdlProperty().setName("IMPACT_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(impactTxt);
            //Urgency
            CsdlProperty urgency = new CsdlProperty().setName("URGENCY").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(urgency);
            //Urgency TXT
            CsdlProperty urgencyTxt = new CsdlProperty().setName("URGENCY_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(urgencyTxt);
            //EFFORT
            CsdlProperty effort = new CsdlProperty().setName("EFFORT").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(effort);
            //EFFORT UNIT
            CsdlProperty effortUnit = new CsdlProperty().setName("EFFORT_UNIT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(effortUnit);
            //PPM_PROJECT_ID
            CsdlProperty ppmProjectId = new CsdlProperty().setName("PPM_PROJECT_ID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(ppmProjectId);
            //"PPM_PROJECT_TXT":
            CsdlProperty ppmProjectTxt = new CsdlProperty().setName("PPM_PROJECT_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(ppmProjectTxt);
            //"PPM_WAVE":
            CsdlProperty ppmWave = new CsdlProperty().setName("PPM_WAVE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(ppmWave);
            //"PPM_WAVE_TXT"
            CsdlProperty ppmWaveTxt = new CsdlProperty().setName("PPM_WAVE_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(ppmWaveTxt);
            //"PPM_START_DATE"
            CsdlProperty ppmStartDate = new CsdlProperty().setName("PPM_START_DATE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(ppmStartDate);
            //"PPM_FINISH_DATE"
            CsdlProperty ppmFinishDate = new CsdlProperty().setName("PPM_FINISH_DATE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(ppmFinishDate);
            //"PPM_COMPLETION"
            CsdlProperty ppmCompletion = new CsdlProperty().setName("PPM_COMPLETION").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(ppmCompletion);
            //"PPM_TOTAL_WORK"
            CsdlProperty ppmTotalWork = new CsdlProperty().setName("PPM_TOTAL_WORK").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(ppmTotalWork);
            //PPM_TOTAL_WORK_UNIT
            CsdlProperty ppmTotalWorkUnit = new CsdlProperty().setName("PPM_TOTAL_WORK_UNIT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(ppmTotalWorkUnit);
            //REQUESTED_RELEASE_NO
            CsdlProperty requestedReleaseNo = new CsdlProperty().setName("REQUESTED_RELEASE_NO").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(requestedReleaseNo);
            //"REQUESTED_RELEASE_CLASS"
            CsdlProperty requestedReleaseClass = new CsdlProperty().setName("REQUESTED_RELEASE_CLASS").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(requestedReleaseClass);
            //"REQUESTED_RELEASE_TYPE"
            CsdlProperty requestedReleaseType = new CsdlProperty().setName("REQUESTED_RELEASE_TYPE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(requestedReleaseType);
            //"REQUESTED_GO_LIVE_DATE"
            CsdlProperty requestedGoLiveDate = new CsdlProperty().setName("REQUESTED_GO_LIVE_DATE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(requestedGoLiveDate);
            //"FORECAST_RELEASE_NO"
            CsdlProperty forecastReleaseNo = new CsdlProperty().setName("FORECAST_RELEASE_NO").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(forecastReleaseNo);
            //"FORECAST_RELEASE_CLASS"
            CsdlProperty forecastReleaseClass = new CsdlProperty().setName("FORECAST_RELEASE_CLASS").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(forecastReleaseClass);
            //"FORECAST_RELEASE_TYPE"
            CsdlProperty forecastReleaseType = new CsdlProperty().setName("FORECAST_RELEASE_TYPE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(forecastReleaseType);
            //"FORECAST_GO_LIVE_DATE"
            CsdlProperty forecastGoLiveDate = new CsdlProperty().setName("FORECAST_GO_LIVE_DATE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(forecastGoLiveDate);
            //"ACTUAL_RELEASE_NO"
            CsdlProperty actualReleaseNo = new CsdlProperty().setName("ACTUAL_RELEASE_NO").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(actualReleaseNo);
            //"ACTUAL_RELEASE_CLASS"
            CsdlProperty actualReleaseClass = new CsdlProperty().setName("ACTUAL_RELEASE_CLASS").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(actualReleaseClass);
            //"ACTUAL_RELEASE_TYPE"
            CsdlProperty actualReleaseType = new CsdlProperty().setName("ACTUAL_RELEASE_TYPE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(actualReleaseType);
            //"ACTUAL_GO_LIVE_DATE"
            CsdlProperty actualGoLiveDate = new CsdlProperty().setName("ACTUAL_GO_LIVE_DATE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(actualGoLiveDate);
            //"CLASSIFICATION"
            CsdlProperty classification = new CsdlProperty().setName("CLASSIFICATION").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(classification);
            //"CLASSIFICATION_TXT"
            CsdlProperty classificationTxt = new CsdlProperty().setName("CLASSIFICATION_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()).setNullable(true);
            csdlProperties.add(classificationTxt);

            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("EXTERNAL_ID");

            List<CsdlNavigationProperty> navigationPropertyList = new ArrayList<>();

            CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName(ES_WRICEF_ATTRIBUTES_NAME)
                    .setType(ET_WRICEF_ATTRIBUTE_FQN).setCollection(true).setPartner("Issue");
            navigationPropertyList.add(navProp);

            CsdlNavigationProperty partnerAttributeNavProperty = new CsdlNavigationProperty().setName(ES_PARTNERS_NAME).setType(ET_PARTNER_FQN)
                    .setCollection(true).setPartner(ET_USERSTORY_NAME);
            navigationPropertyList.add(partnerAttributeNavProperty);

            CsdlNavigationProperty appointmentAttributeNavProperty = new CsdlNavigationProperty().setName(ES_APPOINTMENTS_NAME).setType(ET_APPOITMENT_FQN)
                    .setCollection(true).setPartner(ET_USERSTORY_NAME);
            navigationPropertyList.add(appointmentAttributeNavProperty);

            CsdlNavigationProperty documentAttributeNavProperty = new CsdlNavigationProperty().setName(ES_DOCUMENTS_NAME).setType(ET_DOCUMENT_FQN)
                    .setCollection(true).setPartner(ET_USERSTORY_NAME);
            navigationPropertyList.add(documentAttributeNavProperty);

            CsdlNavigationProperty attachmentAttributeNavProperty = new CsdlNavigationProperty().setName(ES_ATTACHMENTS_NAME).setType(ET_ATTACHMENT_FQN)
                    .setCollection(true).setPartner(ET_USERSTORY_NAME);
            navigationPropertyList.add(attachmentAttributeNavProperty);

            CsdlNavigationProperty categoryAttributeNavProperty = new CsdlNavigationProperty().setName(ES_CATEGORIES_NAME).setType(ET_CATEGORY_FQN)
                    .setCollection(true).setPartner(ET_USERSTORY_NAME);
            navigationPropertyList.add(categoryAttributeNavProperty);

            CsdlNavigationProperty textAttributeNavProperty = new CsdlNavigationProperty().setName(ES_TEXTS_NAME).setType(ET_TEXT_FQN)
                    .setCollection(true).setPartner(ET_USERSTORY_NAME);
            navigationPropertyList.add(textAttributeNavProperty);

            CsdlNavigationProperty processStructAttributeNavProperty = new CsdlNavigationProperty().setName(ES_PROCESS_STRUCTS_NAME).setType(ET_PROCESS_STRUCT_FQN)
                    .setCollection(true).setPartner(ET_USERSTORY_NAME);
            navigationPropertyList.add(processStructAttributeNavProperty);


            //configure entity type
            entityType = new CsdlEntityType();
            entityType.setName(ET_USERSTORY_NAME);
            entityType.setProperties(csdlProperties);
            entityType.setKey(Collections.singletonList(propertyRef));
            entityType.setNavigationProperties(navigationPropertyList);

            return entityType;

        } else if (entityTypeName.equals(ET_WRICEF_ATTRIBUTE_FQN)) {

            CsdlProperty key = new CsdlProperty().setName("KEY").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty value = new CsdlProperty().setName("VALUE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName("Issue").setType(ET_USERSTORY_FQN)
                    .setNullable(false).setPartner(ES_WRICEF_ATTRIBUTES_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);

            entityType = new CsdlEntityType();
            entityType.setName(ET_WRICEF_ATTRIBUTE_NAME);
            entityType.setNavigationProperties(navPropList);
            entityType.setProperties(Arrays.asList(key, value));

            return entityType;
        } else if (entityTypeName.equals(ET_PARTNER_FQN)) {
            CsdlProperty partnerFct = new CsdlProperty().setName("PARTNER_FCT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty partnerFctTxt = new CsdlProperty().setName("PARTNER_FCT_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty partnerNo = new CsdlProperty().setName("PARTNER_NO").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty partnerName = new CsdlProperty().setName("PARTNER_NAME").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty partnerEmail = new CsdlProperty().setName("PARTNER_EMAIL").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty mainPartner = new CsdlProperty().setName("PARTNER_MAIN").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName("Issue").setType(ET_USERSTORY_FQN)
                    .setNullable(false).setPartner(ES_PARTNERS_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);

            entityType = new CsdlEntityType();
            entityType.setName(ET_PARTNER_NAME);
            entityType.setProperties(Arrays.asList(partnerFct, partnerFctTxt, partnerNo, partnerName, partnerEmail, mainPartner));
            entityType.setNavigationProperties(navPropList);

            return entityType;
        } else if (entityTypeName.equals(ET_APPOITMENT_FQN)) {
            CsdlProperty apptType = new CsdlProperty().setName("APPT_TYPE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty apptTypeText = new CsdlProperty().setName("APPT_TYPE_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty timestamp = new CsdlProperty().setName("TIMESTAMP").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty timezone = new CsdlProperty().setName("TIMEZONE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty isDuration = new CsdlProperty().setName("IS_DURATION").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty duration = new CsdlProperty().setName("DURATION").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
            CsdlProperty timeUnit = new CsdlProperty().setName("TIME_UNIT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName("Issue").setType(ET_USERSTORY_FQN)
                    .setNullable(false).setPartner(ES_APPOINTMENTS_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);

            entityType = new CsdlEntityType();
            entityType.setName(ET_APPOITMENT_NAME);
            entityType.setProperties(Arrays.asList(apptType, apptTypeText, timestamp, timezone, isDuration, duration, timeUnit));
            entityType.setNavigationProperties(navPropList);

            return entityType;
        } else if (entityTypeName.equals(ET_TEXT_FQN)) {
            CsdlProperty textID = new CsdlProperty().setName("TEXTID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty textIdTxt = new CsdlProperty().setName("TEXTID_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty changedBy = new CsdlProperty().setName("CHANGED_BY").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty changeDate = new CsdlProperty().setName("CHANGE_DATE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty changeTime = new CsdlProperty().setName("CHANGE_TIME").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
           // CsdlProperty changeTime = new CsdlProperty().setName("IS_DURATION").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty text = new CsdlProperty().setName("TEXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName("Issue").setType(ET_USERSTORY_FQN)
                    .setNullable(false).setPartner(ES_TEXTS_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);

            entityType = new CsdlEntityType();
            entityType.setName(ET_TEXT_NAME);
            entityType.setProperties(Arrays.asList(textID, textIdTxt, changedBy, changeDate, changeTime, text));
            entityType.setNavigationProperties(navPropList);

            return entityType;
        } else if (entityTypeName.equals(ET_CATEGORY_FQN)) {
            CsdlProperty guid = new CsdlProperty().setName("GUID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty description = new CsdlProperty().setName("DESCRIPTION").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty level = new CsdlProperty().setName("LEVEL").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());

            CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName("Issue").setType(ET_USERSTORY_FQN)
                    .setNullable(false).setPartner(ES_CATEGORIES_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);

            entityType = new CsdlEntityType();
            entityType.setName(ET_CATEGORY_NAME);
            entityType.setProperties(Arrays.asList(guid, description, level));
            entityType.setNavigationProperties(navPropList);

            return entityType;
        } else if (entityTypeName.equals(ET_PROCESS_STRUCT_FQN)) {
            CsdlProperty projectID = new CsdlProperty().setName("PROJECT_ID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty nodeID = new CsdlProperty().setName("NODE_ID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty processStepTxt = new CsdlProperty().setName("PROCESSSTEP_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty processTxt = new CsdlProperty().setName("PROCESS_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty scenarioTxt = new CsdlProperty().setName("SCENARIO_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName("Issue").setType(ET_USERSTORY_FQN)
                    .setNullable(false).setPartner(ES_PROCESS_STRUCTS_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);

            entityType = new CsdlEntityType();
            entityType.setName(ET_PROCESS_STRUCT_NAME);
            entityType.setProperties(Arrays.asList(projectID, nodeID, processStepTxt, processTxt, scenarioTxt));
            entityType.setNavigationProperties(navPropList);

            return entityType;
        } else if (entityTypeName.equals(ET_DOCUMENT_FQN)) {
            CsdlProperty filename = new CsdlProperty().setName("FILENAME").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty title = new CsdlProperty().setName("TITLE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty status = new CsdlProperty().setName("STATUS").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty statusTxt = new CsdlProperty().setName("STATUS_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty docType = new CsdlProperty().setName("DOCU_TYPE").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty docTypeTxt = new CsdlProperty().setName("DOCU_TYPE_TXT").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty author = new CsdlProperty().setName("AUTHOR").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty authorName = new CsdlProperty().setName("AUTHOR_NAME").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty url = new CsdlProperty().setName("URL").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName("Issue").setType(ET_USERSTORY_FQN)
                    .setNullable(false).setPartner(ES_DOCUMENTS_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);

            entityType = new CsdlEntityType();
            entityType.setName(ET_DOCUMENT_NAME);
            entityType.setProperties(Arrays.asList(filename, title, status, statusTxt, docType, docTypeTxt, author, authorName, url));
            entityType.setNavigationProperties(navPropList);

            return entityType;
        } else if (entityTypeName.equals(ET_ATTACHMENT_FQN)) {
            CsdlProperty filename = new CsdlProperty().setName("FILENAME").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty technFilename = new CsdlProperty().setName("TECHN_FILENAME").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty extension = new CsdlProperty().setName("EXTENSION").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty url = new CsdlProperty().setName("URL").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName("Issue").setType(ET_USERSTORY_FQN)
                    .setNullable(false).setPartner(ES_ATTACHMENTS_NAME);
            List<CsdlNavigationProperty> navPropList = new ArrayList<>();
            navPropList.add(navProp);

            entityType = new CsdlEntityType();
            entityType.setName(ET_ATTACHMENT_NAME);
            entityType.setProperties(Arrays.asList(filename, technFilename, extension, url));
            entityType.setNavigationProperties(navPropList);

            return entityType;
        }

        return null;
    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) throws ODataException {
        if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
            CsdlEntityContainerInfo csdlEntityContainerInfo = new CsdlEntityContainerInfo();
            csdlEntityContainerInfo.setContainerName(CONTAINER);
            return csdlEntityContainerInfo;
        }
        return null;
    }

    @Override
    public List<CsdlSchema> getSchemas() throws ODataException {
        //create schemas
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(NAMESPACE);

        //adding entity types
        List<CsdlEntityType> entityTypes = new ArrayList<>();
        entityTypes.add(getEntityType(ET_USERSTORY_FQN));
        entityTypes.add(getEntityType(ET_WRICEF_ATTRIBUTE_FQN));
        entityTypes.add(getEntityType(ET_PARTNER_FQN));
        entityTypes.add(getEntityType(ET_APPOITMENT_FQN));
        entityTypes.add(getEntityType(ET_TEXT_FQN));
        entityTypes.add(getEntityType(ET_CATEGORY_FQN));
        entityTypes.add(getEntityType(ET_PROCESS_STRUCT_FQN));
        entityTypes.add(getEntityType(ET_DOCUMENT_FQN));
        entityTypes.add(getEntityType(ET_ATTACHMENT_FQN));
        schema.setEntityTypes(entityTypes);

        //add entity container
        schema.setEntityContainer(getEntityContainer());

        //finally
        List<CsdlSchema> schemas = new ArrayList<>();
        schemas.add(schema);

        return schemas;
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {
        CsdlEntitySet entitySet = null;
        if (entityContainer.equals(CONTAINER)) {
            if (entitySetName.equals(ES_USERSTORIES_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_USERSTORIES_NAME);
                entitySet.setType(ET_USERSTORY_FQN);

                List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<CsdlNavigationPropertyBinding>();
                CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setTarget("WRICEF_ATTRIBUTES");
                navPropBinding.setPath("WRICEF_ATTRIBUTES");
                navPropBindingList.add(navPropBinding);

                navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setTarget("PARTNER");
                navPropBinding.setPath("PARTNER");
                navPropBindingList.add(navPropBinding);

                navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setTarget("APPOINTMENT");
                navPropBinding.setPath("APPOINTMENT");
                navPropBindingList.add(navPropBinding);

                navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setTarget("TEXT");
                navPropBinding.setPath("TEXT");
                navPropBindingList.add(navPropBinding);

                navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setTarget("CATEGORY");
                navPropBinding.setPath("CATEGORY");
                navPropBindingList.add(navPropBinding);

                navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setTarget("PROCESS_STRUCT");
                navPropBinding.setPath("PROCESS_STRUCT");
                navPropBindingList.add(navPropBinding);

                navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setTarget("DOCUMENT");
                navPropBinding.setPath("DOCUMENT");
                navPropBindingList.add(navPropBinding);

                navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setTarget("ATTACHMENT");
                navPropBinding.setPath("ATTACHMENT");

                navPropBindingList.add(navPropBinding);
                entitySet.setNavigationPropertyBindings(navPropBindingList);

                return entitySet;
            } else if (entitySetName.equals(ES_WRICEF_ATTRIBUTES_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_WRICEF_ATTRIBUTES_NAME);
                entitySet.setType(ET_WRICEF_ATTRIBUTE_FQN);
                return entitySet;
            } else if (entitySetName.equals(ES_PARTNERS_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_PARTNERS_NAME);
                entitySet.setType(ET_PARTNER_FQN);
                return entitySet;
            } else if (entitySetName.equals(ES_APPOINTMENTS_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_APPOINTMENTS_NAME);
                entitySet.setType(ET_APPOITMENT_FQN);
                return entitySet;
            } else if (entitySetName.equals(ES_TEXTS_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_TEXTS_NAME);
                entitySet.setType(ET_TEXT_FQN);
                return entitySet;
            } else if (entitySetName.equals(ES_CATEGORIES_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_CATEGORIES_NAME);
                entitySet.setType(ET_CATEGORY_FQN);
                return entitySet;
            } else if (entitySetName.equals(ES_PROCESS_STRUCTS_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_PROCESS_STRUCTS_NAME);
                entitySet.setType(ET_PROCESS_STRUCT_FQN);
                return entitySet;
            } else if (entitySetName.equals(ES_DOCUMENTS_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_DOCUMENTS_NAME);
                entitySet.setType(ET_DOCUMENT_FQN);
                return entitySet;
            } else if (entitySetName.equals(ES_ATTACHMENTS_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_ATTACHMENTS_NAME);
                entitySet.setType(ET_ATTACHMENT_FQN);
                return entitySet;
            }
        }
        return null;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() throws ODataException {
        List<CsdlEntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(CONTAINER, ES_USERSTORIES_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_WRICEF_ATTRIBUTES_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_PARTNERS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_APPOINTMENTS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_TEXTS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_CATEGORIES_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_PROCESS_STRUCTS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_DOCUMENTS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_ATTACHMENTS_NAME));

        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(CONTAINER_NAME);
        entityContainer.setEntitySets(entitySets);

        return entityContainer;
    }

}
