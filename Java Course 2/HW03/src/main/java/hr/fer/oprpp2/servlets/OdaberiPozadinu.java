package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="pozadina", urlPatterns = {"/odaberiPozadinu"})
public class OdaberiPozadinu extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String url = "/webapp2/images/" + req.getParameter("url");
        req.getSession().setAttribute("bgImage", url);
        req.getServletContext().setAttribute("bgUrl", url);
        response.sendRedirect(req.getContextPath() + "/mi/vrijeme");
    }

}