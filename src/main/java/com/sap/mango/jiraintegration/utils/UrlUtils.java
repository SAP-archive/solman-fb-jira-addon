package com.sap.mango.jiraintegration.utils;

import org.apache.commons.lang.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility class, that generate url from the solman url and the file url
 */
public class UrlUtils {
    public static URL generateUrl(String fileUrl, String solmanUrl) throws MalformedURLException {
        URL url = new URL(fileUrl);
        String authority = url.getAuthority();
        String urlString = url.toString();
        String rightPath = StringUtils.substringAfter(urlString, authority);
        String generatedUrl = solmanUrl.concat(rightPath);
        return new URL(generatedUrl);
    }

    /**
     * Replaces all '%20' symbols with a space
     * @param filename
     * @return
     */
    public static String formatFileName(String filename) {
        return StringUtils.replace(filename, "%20", " ");
    }
}
