package com.sap.mango.jiraintegration.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by fmap on 03.12.15.
 *
 */
public class Configuration
{
    public static final Config CONFIG = ConfigFactory.load(Configuration.class.getClassLoader());

    public static final String pass = CONFIG.getString("app.pass");
    public static final String trustStorePass = CONFIG.getString("app.trustStorePass");
}
