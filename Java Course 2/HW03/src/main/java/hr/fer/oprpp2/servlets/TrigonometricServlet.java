package hr.fer.oprpp2.servlets;

import hr.fer.oprpp2.beans.Angles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name="trigServlet", urlPatterns = {"/trigonometric"})
public class TrigonometricServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String aParam = req.getParameter("a");
        String bParam = req.getParameter("b");

        if(aParam == null) {
            aParam = "0";
        }
        if(bParam == null) {
            bParam = "360";
        }

        int a = Integer.parseInt(aParam);
        int b = Integer.parseInt(bParam);

        if(a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        } else if(b > (a + 720)) {
            b = a + 720;
        }

        Map<Integer, Angles> angles = new HashMap<>();

        for(int i = a; i <= b; ++i) {
            angles.put(i, new Angles(Math.sin(Math.toRadians(i)), Math.cos(Math.toRadians(i))));
        }

        req.setAttribute("angles", angles);

        req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, response);

    }
}
