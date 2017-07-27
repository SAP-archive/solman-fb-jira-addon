package com.sap.mango.jiraintegration.solman.entities.fileinfo;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.data.function.Function0;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.utils.EntityManager;
import net.java.ao.DBParam;
import net.java.ao.Query;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Class, that executes dao operations over FileInfo entity.
 */
public class FileInfoDAO {

    private ActiveObjects ao;

    public FileInfoDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public void saveFileInfo(String issuekey, String filename, String technFilename, String extension, String url, Date creationDate, Integer attachmentType,
                             SolmanParamsAO solmanParamsAO) {
        FileInfo fileInfo = ao.create(FileInfo.class,
                new DBParam("ISSUE_KEY", issuekey),
                new DBParam("FILENAME", filename),
                new DBParam("TECHN_FILENAME", technFilename),
                new DBParam("EXTENSION", extension),
                new DBParam("URL", url),
                new DBParam("ATTACHMENT_TYPE", attachmentType),
                new DBParam("CREATION_DATE", creationDate),
                new DBParam("ATTACHED", false),
                new DBParam("SOLMAN_PARAMS_ID", solmanParamsAO.getID()));
        //fileInfo.setSolmanParams(solmanParamsAO);
        EntityManager.saveEntity(ao, fileInfo);
    }

    public List<FileInfo> getFilesInfo(final String issueKey) {
        Map<Integer, SolmanParamsAO> solmanParamsAOs = EntityManager.getEntitiesAsMap(ao, SolmanParamsAO.class,
                new Function0<Query>() {
                    @Override
                    public Query apply() {
                        return Query.select("ID")
                                .alias(SolmanParamsAO.class, "SM");
        }});
        FileInfo [] filesInfos = EntityManager.getEntitiesAsArray(ao, FileInfo.class, new Function0<Query>() {
            @Override
            public Query apply() {
                Query query = Query.select("ID, ISSUE_KEY, FILENAME, TECHN_FILENAME, EXTENSION, URL, CREATION_DATE, ATTACHED, ATTACHMENT_TYPE, SOLMAN_PARAMS_ID")
                        .alias(FileInfo.class, "FI")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FI.SOLMAN_PARAMS_ID = SM.ID")
                        .where("FI.ISSUE_KEY = ? and ATTACHED = ?", issueKey, false);
                return query;
            }
        });
        for (FileInfo fileInfo : filesInfos) {
            fileInfo.setSolmanParams(solmanParamsAOs.get(fileInfo.getSolmanParams().getID()));
        }

        return Arrays.asList(filesInfos);
    }

    public void updateFilesInfo(final int id, final Boolean isAttached) {
        FileInfo fileInfo = EntityManager.getEntity(ao, FileInfo.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID").where("ID = ?", id);
            }
        });
        fileInfo.setAttached(isAttached);
        EntityManager.saveUpdateEntity(ao, fileInfo, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .where("ID = ?", id);
            }
        });
    }

    public Boolean deleteFileInfos(final String solManCustGuid) {
        return EntityManager.deleteEntity(ao, FileInfo.class, new Function0<Query>() {
            @Override
            public Query apply() {
                return Query.select("ID")
                        .alias(FileInfo.class, "FI")
                        .alias(SolmanParamsAO.class, "SM")
                        .join(SolmanParamsAO.class, "FI.SOLMAN_PARAMS_ID = SM.ID")
                        .where("SM.CUSTOMER_GIUD = ?", solManCustGuid);
            }
        });
    }
}
