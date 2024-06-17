package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.beans.Band;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.*;

@WebServlet(name="glasanje", urlPatterns = {"/glasanje"})
public class GlasanjeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        List<String> lines = Files.readAllLines(Path.of(fileName));
        List<Band> bands = new ArrayList<>();
        for(String l : lines) {
            String[] elems = l.split(",");
            bands.add(new Band(elems[0], elems[1], elems[2]));
        }
        req.setAttribute("bands", bands);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }
}
