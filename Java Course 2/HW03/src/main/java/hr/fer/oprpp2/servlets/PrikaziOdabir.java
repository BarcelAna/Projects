package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@WebServlet(name="odabir", urlPatterns = {"/prikaziOdabir"})
public class PrikaziOdabir extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/OdaberiPozadinu.jsp").forward(req, response);
    }

}