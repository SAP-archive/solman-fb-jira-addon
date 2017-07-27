package com.sap.mango.jiraintegration.core;

import com.sap.mango.jiraintegration.utils.XSSUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Wrapper, that executes anti cross site scripting checks
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public XSSRequestWrapper(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    @Override
    public String[] getParameterValues(String name) {
        String [] parameterValues =  super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        int count = parameterValues.length;
        String [] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = XSSUtils.stripXSS(encodedValues[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String name) {
        return XSSUtils.stripXSS(super.getParameter(name));
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        return XSSUtils.stripXSS(header);
    }

}
