package hr.fer.oprpp2.servlets.glasanje;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="dodajglas", urlPatterns = {"/glasanje-glasaj"})
public class DodajGlasServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        File f = new File(fileName);

        Map<String, Integer> results = new HashMap<>();

        List<String> lines = Files.readAllLines(Path.of(fileName));
        for(String l : lines) {
            String[] elems = l.split(",");
            results.put(elems[0], Integer.parseInt(elems[1]));
        }

        String id = req.getParameter("id");
        Integer oldVotes = results.get(id);
        results.put(id, (oldVotes==null ? 1:oldVotes+1));

        FileWriter fileWriter = new FileWriter(f, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for(Map.Entry<String, Integer> e : results.entrySet()) {
            bufferedWriter.write(e.getKey() + "," + e.getValue()+"\n");
        }
        bufferedWriter.close();

        resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
    }
}
