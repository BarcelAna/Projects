package hr.fer.oprpp2.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        long startTime = System.currentTimeMillis();
        sce.getServletContext().setAttribute("startT", startTime);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
