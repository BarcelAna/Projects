package hr.fer.oprpp2.jmbag0036529634.servlets.glasanje;

import hr.fer.oprpp2.jmbag0036529634.dao.DAOProvider;
import hr.fer.oprpp2.jmbag0036529634.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="glasanje-rez", urlPatterns = {"/servleti/glasanje-rezultati"})
public class GlasanjeRezultatiServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long pollID = Long.parseLong(req.getParameter("pollID"));

        List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollID);

        req.setAttribute("results", pollOptions);
        req.setAttribute("pollID", pollID);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
