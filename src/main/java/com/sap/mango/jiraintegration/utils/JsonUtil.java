package com.sap.mango.jiraintegration.utils;

import org.apache.log4j.LogManager;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by vld on 14.12.15.
 *
 */
public class JsonUtil {
    private static final org.apache.log4j.Logger log = LogManager.getLogger(JsonUtil.class);

    public static void send(HttpServletResponse resp, Object json){
        resp.setContentType("application/json; charset=utf-8");

        try {
            PrintWriter out = resp.getWriter();
            out.write(JsonEncoder.toJson(json));
            out.flush();
        } catch (IOException e) {
            log.error("Error while getting servlet out", e);
        }
    }

}
