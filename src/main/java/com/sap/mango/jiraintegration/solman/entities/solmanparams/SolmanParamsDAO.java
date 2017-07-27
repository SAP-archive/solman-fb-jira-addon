package com.sap.mango.jiraintegration.solman.entities.solmanparams;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.appointmentmapping.AppointmentMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.fileinfo.FileInfoDAO;
import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettingsDAO;
import com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping.PartnerFieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettingsDAO;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.IssueTransitionDAO;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.UnsynchronizedIssueDAO;
import com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue.SolmanPluginSchedulerManager;
import com.sap.mango.jiraintegration.utils.Configuration;
import com.sap.mango.jiraintegration.utils.EncryptionUtils;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for SolmanParams, that executes CRUD operations with this entity
 */
public class SolmanParamsDAO {

    private ActiveObjects ao;

    private AppointmentMappingDAO appointmentMappingDAO;

    private FieldMappingDAO fieldMappingDAO;

    private IssueStatusMappingDAO issueStatusMappingDAO;

    private IssueTypeMappingDAO issueTypeMappingDAO;

    private PartnerFieldMappingDAO partnerFieldMappingDAO;

    private ProjectMappingDAO projectMappingDAO;

    private TextFieldMappingDAO textFieldMappingDAO;

    private SolmanPluginSchedulerManager solmanPluginSchedulerManager;

    private FileInfoDAO fileInfoDAO;

    private JumpUrlSettingsDAO jumpUrlSettingsDAO;

    private ProxySettingsDAO proxySettingsDAO;

    private PriorityMappingDAO priorityMappingDAO;

    private UnsynchronizedIssueDAO unsynchronizedIssueDAO;

    private IssueTransitionDAO issueTransitionDAO;

    public SolmanParamsDAO(ActiveObjects ao, AppointmentMappingDAO appointmentMappingDAO,
                           FieldMappingDAO fieldMappingDAO, IssueStatusMappingDAO issueStatusMappingDAO,
                           IssueTypeMappingDAO issueTypeMappingDAO, PartnerFieldMappingDAO partnerFieldMappingDAO,
                           ProjectMappingDAO projectMappingDAO, TextFieldMappingDAO textFieldMappingDAO,
                           SolmanPluginSchedulerManager solmanPluginSchedulerManager, FileInfoDAO fileInfoDAO,
                           JumpUrlSettingsDAO jumpUrlSettingsDAO, ProxySettingsDAO proxySettingsDAO,
                           PriorityMappingDAO priorityMappingDAO, UnsynchronizedIssueDAO unsynchronizedIssueDAO,
                           IssueTransitionDAO issueTransitionDAO) {
        this.ao = ao;
        this.appointmentMappingDAO = appointmentMappingDAO;
        this.fieldMappingDAO = fieldMappingDAO;
        this.issueStatusMappingDAO = issueStatusMappingDAO;
        this.issueTypeMappingDAO = issueTypeMappingDAO;
        this.partnerFieldMappingDAO = partnerFieldMappingDAO;
        this.projectMappingDAO = projectMappingDAO;
        this.textFieldMappingDAO = textFieldMappingDAO;
        this.fileInfoDAO = fileInfoDAO;
        this.jumpUrlSettingsDAO = jumpUrlSettingsDAO;
        this.proxySettingsDAO = proxySettingsDAO;
        this.priorityMappingDAO = priorityMappingDAO;
        this.solmanPluginSchedulerManager = solmanPluginSchedulerManager;
        this.unsynchronizedIssueDAO = unsynchronizedIssueDAO;
        this.issueTransitionDAO = issueTransitionDAO;
    }

    public void saveSolmanParams(final List<SolmanParams> sparams) {
        for (SolmanParams sparam : sparams) {
            SolmanParamsAO solmanParamsAO = EntityManager.getEntity(ao, SolmanParamsAO.class, new Function0<Query>() {
                @Override
                public Query apply() {
                    return Query.select("ID")
                            .alias(SolmanParamsAO.class, "SM")
                            .where("SM.CUSTOMER_GIUD = ?", sparam.customerGuid);
                }
            });
            if (solmanParamsAO == null) {
                solmanParamsAO = ao.create(SolmanParamsAO.class, new DBParam("SOLMAN_URL", "-"), new DBParam("USERNAME", "-"),
                        new DBParam("PASSWORD", "-"), new DBParam("CUSTOMER_GIUD", "-"), new DBParam("CUSTOMER_DESCRIPTION", "-"),
                        new DBParam("SAP_CLIENT", "-"), new DBParam("AUTHENTICATION_TYPE", 0), new DBParam("TOKEN_HCP_ACCOUNT_URL", "-"));
            }
            solmanParamsAO.setSolmanUrl(sparam.solmanUrl);
            solmanParamsAO.setUsername(sparam.userName);
            solmanParamsAO.setPassword(EncryptionUtils.encrypt(sparam.password, Configuration.pass));
            solmanParamsAO.setCustomerGiud(sparam.customerGuid);
            solmanParamsAO.setCustomerDescription(sparam.customerDescription);
            solmanParamsAO.setSapClient(sparam.sapClient);
            solmanParamsAO.setAuthenticationType(Integer.valueOf(sparam.authenticationType));
            solmanParamsAO.setTokenHcpAccountUrl(sparam.tokenHcpAccountUrl);
            EntityManager.saveEntity(ao, solmanParamsAO);
            SolmanParams solmanParams = convertSolmanParams(new SolmanParamsAO[]{solmanParamsAO}, false).get(0);
            //creating the related solman customer
            solmanPluginSchedulerManager.createSolmanIssueSynchronizationJobSchedule(solmanParams);
        }
    }

    private static List<SolmanParams> convertSolmanParams(SolmanParamsAO[] solmanParamsAOs, boolean getPass) {
        List<SolmanParams> result = new ArrayList<>();
        for (SolmanParamsAO solmanParamsAO : solmanParamsAOs) {
            result.add(new SolmanParams(solmanParamsAO.getSolmanUrl(), solmanParamsAO.getUsername(),
                    getPass ? EncryptionUtils.decrypt(solmanParamsAO.getPassword(), Configuration.pass) : "*****", solmanParamsAO.getCustomerGiud(),
                    solmanParamsAO.getCustomerDescription(), solmanParamsAO.getSapClient(), solmanParamsAO.getAuthenticationType() != null ? AuthenticationTypeEnum.getValueByKey(solmanParamsAO.getAuthenticationType()) : "-",
                    solmanParamsAO.getTokenHcpAccountUrl()));
        }
        return result;
    }

    public List<SolmanParams> getSolmanParamsByGuid(final String customerGuid, boolean getPass) {
        SolmanParamsAO[] solmanParamsAOs = EntityManager.getEntitiesAsArray(ao, SolmanParamsAO.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(SolmanParamsAO.class, "SM")
                        .where("SM.CUSTOMER_GIUD = ?", customerGuid);
            }
        });
        return convertSolmanParams(solmanParamsAOs, getPass);
    }

    public List<SolmanParamsAO> getSolmanParamsByGuid(final String customerGuid) {
        List<SolmanParamsAO> solmanParamsAOs = EntityManager.getEntities(ao, SolmanParamsAO.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_URL, USERNAME, PASSWORD, CUSTOMER_GIUD, CUSTOMER_DESCRIPTION, SAP_CLIENT, AUTHENTICATION_TYPE, TOKEN_HCP_ACCOUNT_URL")
                        .alias(SolmanParamsAO.class, "SM")
                        .where("SM.CUSTOMER_GIUD = ?", customerGuid);
            }
        });
        return solmanParamsAOs;
    }

    public List<SolmanParams> getSolmanParamsAll(boolean getPass) {
        return convertSolmanParams(EntityManager.getEntities(ao, SolmanParamsAO.class), getPass);
    }

    public boolean deleteSolmanParam(final String customerGuid) {
        List<SolmanParamsAO> solmanParamsAOs = EntityManager.getEntities(ao, SolmanParamsAO.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_URL, USERNAME, PASSWORD, CUSTOMER_GIUD, CUSTOMER_DESCRIPTION, SAP_CLIENT, AUTHENTICATION_TYPE, TOKEN_HCP_ACCOUNT_URL")
                        .alias(SolmanParamsAO.class, "SM")
                        .where("SM.CUSTOMER_GIUD = ?", customerGuid);
            }
        });

        return ao.executeInTransaction(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction() {
                if (solmanParamsAOs.isEmpty()) {
                    return false;
                } else {
                    //we remove all related entities
                    appointmentMappingDAO.deleteEntities(customerGuid);
                    fieldMappingDAO.deleteFieldMappings(customerGuid);
                    issueStatusMappingDAO.deleteIssueStatusMappings(customerGuid);
                    projectMappingDAO.deleteProjectMappings(customerGuid);
                    textFieldMappingDAO.deleteFieldMappings(customerGuid);
                    issueTypeMappingDAO.deleteIssueTypeMappings(customerGuid);
                    partnerFieldMappingDAO.deleteFieldMappings(customerGuid);
                    fileInfoDAO.deleteFileInfos(customerGuid);
                    jumpUrlSettingsDAO.deleteJumpUrlSettings(customerGuid);
                    proxySettingsDAO.deleteProxySettings(customerGuid);
                    priorityMappingDAO.deletePriorityMappings(customerGuid);
                    issueTransitionDAO.deleteIssueTransitions(customerGuid);
                    unsynchronizedIssueDAO.deleteUnsynchronizedIssues(customerGuid);

                    //removing the related schedule
                    solmanPluginSchedulerManager.unscheduleSolmanIssueSynchronizationJobSchedule(convertSolmanParams(new SolmanParamsAO[]{solmanParamsAOs.get(0)}, false).get(0));
                    //removing the solman customerU
                    EntityManager.deleteEntity(ao, SolmanParamsAO.class, new Function0<Query>() {
                        @Override
                        public Query apply() {
                            return Query.select("ID")
                                    .alias(SolmanParamsAO.class, "SM")
                                    .where("SM.CUSTOMER_GIUD = ?", customerGuid);
                        }
                    });
                    return true;
                }
            }
        });
    }
}
