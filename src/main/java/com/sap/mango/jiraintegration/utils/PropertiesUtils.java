package com.sap.mango.jiraintegration.utils;

import com.atlassian.core.util.ClassLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class, that returns a record from the project properties file
 */
public class PropertiesUtils {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PropertiesUtils.class);

    public static String getValue(String key) {
        InputStream inputStream = null;
        Properties properties = new Properties();
        try {
            inputStream = ClassLoaderUtils.getResourceAsStream("solman_integration.properties", PropertiesUtils.class);
            properties.load(inputStream);
            String value = properties.getProperty(key);
            return value;
        } catch (IOException e) {
            safeClose(inputStream);
            return null;
        } finally {
            safeClose(inputStream);
        }
    }

    private static void safeClose(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOG.error("Problem while reading from properties file solman_integration.properties", e.getCause());
            }
        }
    }
}
