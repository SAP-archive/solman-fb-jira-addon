package com.sap.mango.jiraintegration.core;


import com.sap.mango.jiraintegration.utils.JsonUtil;
import org.apache.log4j.LogManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;


@SuppressWarnings("serial")
public abstract class JsonServlet extends HttpServlet {

    public static final String NOT_AUTHORIZED = "not.authorized";

    public static final String NOT_LOGGED = "not.logged";
    private static final org.apache.log4j.Logger log = LogManager.getLogger(JsonServlet.class);
    /**
     * if you want to restrict this servlet to specific user roles,
     * please override this method to return the required roles and split them with ","
     */

    public final String nobody = "nobody";
    private static String[] _requiredRoles = null;

    public static String getString(HttpServletRequest r, String name) {
        return r.getParameter(name);
    }

    public static int getInt(HttpServletRequest r, String name) {
        return Integer.parseInt(getString(r, name));
    }

    public static <A extends Enum<A>> A getEnum(HttpServletRequest r, String name, Class<A> clazz) {
        String val = getString(r, name);
        for (A a : clazz.getEnumConstants()) {
            if (a.name().equals(val)) {
                return a;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("enum {0} of {1}", val, clazz.getSimpleName()));
    }

    public static void sendError_WrongParams(HttpServletResponse resp, Object object) {
        sendError(400, resp, object);
    }

    public static void sendError(HttpServletResponse resp, Object object) {
        sendError(500, resp, object);
    }

    public static void sendError(int code, HttpServletResponse resp, Object object) {
        resp.setStatus(code);
        JsonUtil.send(resp, object);
    }

    public static void sendOK(HttpServletResponse resp, Object object) {
        sendOK(200, resp, object);
    }

    public static void sendOK(int code, HttpServletResponse resp, Object object) {
        resp.setStatus(code);
        JsonUtil.send(resp, object);
    }

    public abstract void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    public abstract String requiredRoles();

    private String[] getRequiredRoles() {
        if (_requiredRoles == null) {

            _requiredRoles = requiredRoles().split(",");

            for (int i = 0; i < _requiredRoles.length; i++) {
                _requiredRoles[i] = _requiredRoles[i].trim();
            }
        }


        return _requiredRoles;
    }

    protected boolean isLogged(HttpServletRequest request) {
        return JiraServlets.isLogged(request);
    }

    protected String getUserName(HttpServletRequest request) {
        return JiraServlets.getUserName(request);
    }

    protected String[] getUserRoles(HttpServletRequest request) {
        return JiraServlets.getUserRoles(request);
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!nobody.equals(requiredRoles())) {

                if (!isLogged(req)) {
                    sendError(401, resp, NOT_LOGGED);
                    return;
                }


                final String[] userRoles = getUserRoles(req);

                if (userRoles == null || userRoles.length == 0) {
                    sendError(403, resp, NOT_AUTHORIZED);
                    return;
                }


                boolean match = false;

                outer:
                for (String userRole : userRoles) {
                    for (String requiredRole : getRequiredRoles()) {
                        if (userRole.equals(requiredRole)) {
                            match = true;
                            break outer;
                        }
                    }

                }

                if (!match) {
                    sendError(403, resp, NOT_AUTHORIZED);
                    return;
                }
            }

            process(req, resp);
            return;
        } catch (Throwable t) {
            sendError(resp, "Internal Error");
            log.error("Error in Json servlet. ", t);
            return;
        }
    }

}