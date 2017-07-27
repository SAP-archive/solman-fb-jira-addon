package com.sap.mango.jiraintegration.solman.entities.jumpurl;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * Entity, that stores jump url-s configuration for Epic/Story
 */
@Preload("WORK_PACKAGE_APP_JUMP_URL, WORK_ITEM_APP_JUMP_URL")
public interface JumpUrlSettings extends Entity {
    void setSolmanParams(SolmanParamsAO solmanParams);
    SolmanParamsAO getSolmanParams();

    void setWorkPackageAppJumpUrl(String workPackageAppJumpUrl);
    String getWorkPackageAppJumpUrl();

    void setWorkItemAppJumpUrl(String workItemAppJumpUrl);
    String getWorkItemAppJumpUrl();
}
