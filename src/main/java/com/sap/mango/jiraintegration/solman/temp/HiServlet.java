package com.sap.mango.jiraintegration.solman.temp;


import com.sap.mango.jiraintegration.core.JsonServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fmap on 23.10.15.
 *
 */
public class HiServlet extends JsonServlet
{

//    private final static String thisPluginSegment = "solman_integration";
//    private final static String database = "database";
//    private final UserManager um;
//
//    private PluginSettingsFactory sf;
//    private JiraHome jh;
//    private PluginSettings globalSettings;
//
//    public HiServlet(PluginSettingsFactory sf, JiraHome jh, UserManager um) {
//        super();
//        this.sf = sf;
//        this.jh = jh;
//        this.um = um;
//    }
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        globalSettings = sf.createGlobalSettings();
//        globalSettings.put("my.last.started", new Date().toString());
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        final ServletOutputStream out = resp.getOutputStream();
//        try {
//            //throw new Exception("ooops");
//            final String url = req.getParameter("url");
//            final String s = HTTPUtils.sendGet(url);
//            out.println(s + "<br>");
//        }catch (Exception e){
//            out.println(e.getMessage()+"<br>");
//            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
//                out.println(stackTraceElement.toString() +"<br>");
//            }
//        }
//    }
//    private static File getDbFolder(JiraHome jh) {
//        //this is the shared jira home, should work in cluster too
//        File parent = jh.getHome();
//        String pluginsSegment = jh.getPluginsDirectory().getName();
//
//
//        File result = new File(parent, pluginsSegment);
//        result = new File(result, thisPluginSegment);
//        result = new File(result, database);
//
//        return result;
//    }


    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        if(isLogged(req))
        {
            sendOK(resp, "hi, " + getUserName(req));
            return;
        }
        sendOK(resp, "hi, anon" );
    }




    @Override
    public String requiredRoles()
    {
        return nobody;
    }



}
