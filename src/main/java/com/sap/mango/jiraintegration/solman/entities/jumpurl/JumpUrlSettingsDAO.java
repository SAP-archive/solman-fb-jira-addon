package com.sap.mango.jiraintegration.solman.entities.jumpurl;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.List;

/**
 * DAO, responsible for JumpURLSettings CRUD operations
 */
public class JumpUrlSettingsDAO {

    private ActiveObjects ao;

    public JumpUrlSettingsDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer saveJumpUrlSettings(SolmanParamsAO solmanParams, String workPackageAppJumpUrl, String workItemAppJumpUrl) {
        JumpUrlSettings jumpUrlSettings = ao.create(JumpUrlSettings.class, new DBParam("WORK_PACKAGE_APP_JUMP_URL", "-"), new DBParam("WORK_ITEM_APP_JUMP_URL", "-"));
        jumpUrlSettings.setWorkPackageAppJumpUrl(workPackageAppJumpUrl);
        jumpUrlSettings.setWorkItemAppJumpUrl(workItemAppJumpUrl);
        jumpUrlSettings.setSolmanParams(solmanParams);

        return EntityManager.saveUpdateEntity(ao, jumpUrlSettings, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(JumpUrlSettings.class, "JS")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "JS.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solmanParams.getCustomerGiud());
            }
        }).getID();
    }

    public List<JumpUrlSettings> getJumpUrlSettings() {
        return EntityManager.getEntities(ao, JumpUrlSettings.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PARAMS_ID, WORK_PACKAGE_APP_JUMP_URL, WORK_ITEM_APP_JUMP_URL")
                        .alias(JumpUrlSettings.class, "JS")
                        .alias(SolmanParamsAO.class, "SM");
            }
        });
    }

    public JumpUrlSettings getJumpUrlSettings(String solManCustGuiD) {
        return EntityManager.getEntity(ao, JumpUrlSettings.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(JumpUrlSettings.class, "JS")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "JS.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuiD);
            }
        });
    }

    public boolean deleteJumpUrlSettings(Integer id) {
        return EntityManager.deleteEntity(ao, JumpUrlSettings.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(JumpUrlSettings.class, "JS")
                        .where("JS.ID = ?", id);
            }
        });
    }

    public Boolean deleteJumpUrlSettings(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, JumpUrlSettings.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(JumpUrlSettings.class, "JUS")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "JUS.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }
}
