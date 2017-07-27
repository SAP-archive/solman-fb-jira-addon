package com.sap.mango.jiraintegration.solman.web.admin.solmanconnections;

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
 * Servlet, that redirects to the Solman Connection administration page.
 */
public class SolmanConnectionsServlet extends HttpServlet {

    private static final org.apache.log4j.Logger log = LogManager.getLogger(SolmanConnectionsServlet.class);

    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;
    private final TemplateRenderer renderer;

    public SolmanConnectionsServlet(UserManager userManager, LoginUriProvider loginUriProvider, TemplateRenderer renderer) {
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
            renderer.render("templates/com/sap/mango/jiraintegration/administration/solmanconnections/view-solmanConnections.vm", response.getWriter());
        }catch(Throwable t) {
            log.error("error while loading page Solman Connections page", t);
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
