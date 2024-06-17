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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="glasanje-rez", urlPatterns = {"/glasanje-rezultati"})
public class GlasanjeRezultatiServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        Path p = Path.of(fileName);

        List<String> lines = Files.readAllLines(p);
        Map<String, Integer> results = new HashMap<>();
        for(String l : lines) {
            System.out.println(l);
            String[] elems = l.split(",");
            results.put(elems[0], Integer.parseInt(elems[1]));
        }

        String fileName2 = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        List<String> lines2 = Files.readAllLines(Path.of(fileName2));
        Map<String, Band> bands = new HashMap<>();
        for(String l : lines2) {
            String[] elems = l.split(",");
            Band newBand = new Band(elems[0], elems[1], elems[2]);
            newBand.setVotes(results.get(elems[0])==null ? 0 : results.get(elems[0]));
            bands.put(elems[0], newBand);
        }

        req.getServletContext().setAttribute("results", bands);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
