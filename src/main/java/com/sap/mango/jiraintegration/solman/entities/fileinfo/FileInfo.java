package com.sap.mango.jiraintegration.solman.entities.fileinfo;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.StringLength;

import java.util.Date;

/**
 * Entity, that stores all information, needed to retrieve attachment when creating jira
 */
@Preload("ISSUE_KEY, FILENAME, TECHN_FILENAME, EXTENSION, URL, CREATION_DATE, ATTACHED, ATTACHMENT_TYPE")
public interface FileInfo extends Entity{

    String getIssueKey();
    void setIssueKey(String issueKey);

    String getFilename();
    void setFilename(String filename);

    String getTechnFilename();
    void setTechnFilename(String technFilename);

    String getExtension();
    void setExtension(String extension);

    @StringLength(StringLength.UNLIMITED)
    String getUrl();
    void setUrl(String url);

    Date getCreationDate();
    void setCreationDate(Date creationDate);

    Boolean isAttached();
    void setAttached(Boolean attached);

    Integer getAttachmentType();
    void setAttachmentType(Integer attachmentType);

    public SolmanParamsAO getSolmanParams();
    public void setSolmanParams(SolmanParamsAO solmanParams);
}
