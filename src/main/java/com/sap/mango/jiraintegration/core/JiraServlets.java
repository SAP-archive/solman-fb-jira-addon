package com.sap.mango.jiraintegration.core;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.SortedSet;

/**
 * Created by fmap on 25.11.15.
 *
 */
public class JiraServlets
{
    public static boolean isLogged(HttpServletRequest request)
    {
        return getAuthCtx().isLoggedInUser();
    }

    private static JiraAuthenticationContext getAuthCtx()
    {
        return ComponentAccessor.getJiraAuthenticationContext();
    }

    private static UserUtil getUserUtil()
    {
        return ComponentAccessor.getUserUtil();
    }


    public static String getUserName(HttpServletRequest request)
    {
        return getAuthCtx().getUser().getUsername();
    }

    public static String[] getUserRoles(HttpServletRequest request) {
        final SortedSet<String> groups = getUserUtil().getGroupNamesForUser(getUserName(request));
        return groups.toArray(new String[groups.size()]);
    }
}
