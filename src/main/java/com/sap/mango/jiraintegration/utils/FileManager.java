package com.sap.mango.jiraintegration.utils;


import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.util.JiraHome;
import com.atlassian.jira.issue.Issue;
import com.sap.mango.jiraintegration.core.httpclient.SolmanClient;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettings;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Utility class that support the file attachment to jira Epic/Story
 */
public class FileManager {

    public static final int BUFFER_SIZE = 4096;

    private static final Logger LOG = LogManager.getLogger(FileManager.class);

    public static File saveFile(ProxySettings proxySettings, SolmanParams solmanParams, Issue issue, String fileUrl, String fileName, Integer attachmentType) throws IOException {

        String issueKey = issue.getKey();
        String projectKey = issue.getProjectObject().getKey();

        //we store the file in temp directory
        File downloadDirectory = new File(ComponentAccessor.getComponent(JiraHome.class).getDataDirectory(), "temp" + File.separator + projectKey + File.separator + issueKey);
        File fileSaveDir = downloadFile(proxySettings, solmanParams, downloadDirectory, fileUrl, fileName, attachmentType);
        return fileSaveDir;
    }

    public static File downloadFile(ProxySettings proxySettings, SolmanParams solmanParamsAO, File saveDir, String fileUrl, String fileName, Integer attachmentType) throws IOException {
        return SolmanClient.downloadFile(proxySettings, solmanParamsAO, saveDir, fileUrl, fileName, attachmentType);
    }

    public static void deleteIssueTempDirectory(Issue issue) throws IOException {
        String issueKey = issue.getKey();
        String projectKey = issue.getProjectObject().getKey();

        FileUtils.deleteDirectory(new File(ComponentAccessor.getComponent(JiraHome.class).getDataDirectory(), "temp" + File.separator + projectKey + File.separator + issueKey));
    }

}
