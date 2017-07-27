package com.sap.mango.jiraintegration.solman.entities.projecttype;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.project.Project;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.List;

/**
 * DAO class, that executes crud operations over ProjectMapping entity
 */
public class ProjectMappingDAO {

    private ActiveObjects ao;

    public ProjectMappingDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public Integer saveUpdateProjectTypeMapping(final SolmanParamsAO solmanParams, final Project project, final String solManProjectID) {
        ProjectMapping projectMapping = ao.create(ProjectMapping.class, new DBParam("JIRA_PROJECT_ID", "-"), new DBParam("SOLMAN_PROJECT_ID", "-"));
        projectMapping.setSolmanParams(solmanParams);
        projectMapping.setJiraProjectID(project.getKey());
        projectMapping.setSolmanProjectID(solManProjectID);
        return EntityManager.saveUpdateEntity(ao, projectMapping, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(ProjectMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? and ( PM.JIRA_PROJECT_ID = ? or SOLMAN_PROJECT_ID = ?)", solmanParams.getCustomerGiud(), project.getKey(), solManProjectID);
            }
        }).getID();
    }

    public List<ProjectMapping> getProjectTypeMappings() {
        return Arrays.asList(EntityManager.getEntities(ao, ProjectMapping.class));
    }

    public ProjectMapping getProjectTypeMapping(final Integer id) {
        return EntityManager.getReadOnlyEntity(ao, ProjectMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public ProjectMapping getProjectTypeMapping(final String solManCustGuiD, final String solManProjectID) {
        return EntityManager.getReadOnlyEntity(ao, ProjectMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PROJECT_ID, JIRA_PROJECT_ID")
                        .alias(ProjectMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? and PM.SOLMAN_PROJECT_ID = ?", solManCustGuiD, solManProjectID);
            }
        });
    }

    public boolean deleteProjectTypeMapping(final Integer id) {
        return EntityManager.deleteEntity(ao, ProjectMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", id);
            }
        });
    }

    public void updateProjectTypeMapping(final ProjectMapping projectMapping) {
        EntityManager.saveUpdateEntity(ao, projectMapping, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select().where("ID = ?", projectMapping.getID());
            }
        });
    }

    public List<ProjectMapping> getProjectMappings(final String solManCustGuid) {
        return EntityManager.getEntities(ao, ProjectMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID, SOLMAN_PROJECT_ID, JIRA_PROJECT_ID")
                        .alias(ProjectMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public Boolean deleteProjectMappings(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, ProjectMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(ProjectMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }

    public List<ProjectMapping> getProjectMappings(final String solManCustGuid, final String solManProjectID) {
        return EntityManager.getEntities(ao, ProjectMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(ProjectMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ? and PM.SOLMAN_PROJECT_ID = ?", solManCustGuid, solManProjectID);
            }
        });
    }

    public boolean deleteProjectMapping(final String id, final String solManCustGuiD) {
        return EntityManager.deleteEntity(ao, ProjectMapping.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(ProjectMapping.class, "PM")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "PM.SOLMAN_PARAMS_ID = SM.ID")
                        .where("PM.ID = ? and SM.CUSTOMER_GIUD = ?", id, solManCustGuiD);
            }
        });
    }
}
