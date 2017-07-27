package com.sap.mango.jiraintegration.solman.web.admin.jumpurl;

import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.apache.log4j.LogManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

/**
 * Servlet, that redirects to the jump url administration page.
 */
public class JumpUrlSettingsServlet extends HttpServlet {

    private static final org.apache.log4j.Logger log = LogManager.getLogger(JumpUrlSettingsServlet.class);

    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;
    private final TemplateRenderer renderer;

    public JumpUrlSettingsServlet(UserManager userManager, LoginUriProvider loginUriProvider, TemplateRenderer renderer) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String username = userManager.getRemoteUsername(request);
            if (username == null || !userManager.isSystemAdmin(username)) {
                redirectToLogin(request, response);
                return;
            }

            response.setContentType("text/html;charset=utf-8");
            renderer.render("templates/com/sap/mango/jiraintegration/administration/solmanjumpurlsettings/view-solmanJumpUrlSettings.vm", response.getWriter());
        } catch (Throwable t) {
            log.error("error while loading Jump Url Settings page", t);
        }
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }

    private URI getUri(HttpServletRequest request) {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }
}
