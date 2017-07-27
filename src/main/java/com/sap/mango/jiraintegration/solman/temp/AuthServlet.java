package com.sap.mango.jiraintegration.solman.temp;


import com.sap.mango.jiraintegration.core.JsonServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fmap on 25.11.15.
 *
 */
public class AuthServlet extends JsonServlet
{
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles()
    {
        return "users";
    }
}
