package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="bgColorServlet", urlPatterns = {"/setcolor"})
public class BgColorServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String color = req.getParameter("color");
        req.getSession().setAttribute("pickedBgColor", color);
        req.getRequestDispatcher("/index.jsp").forward(req, res);
    }
}
