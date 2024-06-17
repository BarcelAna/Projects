package hr.fer.oprpp2.jmbag0036529634.servlets.glasanje;

import hr.fer.oprpp2.jmbag0036529634.dao.DAOProvider;
import hr.fer.oprpp2.jmbag0036529634.model.Poll;
import hr.fer.oprpp2.jmbag0036529634.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="glasanje", urlPatterns = {"/servleti/glasanje"})
public class GlasanjeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long pollID = Long.parseLong(req.getParameter("pollID"));

        Poll poll = DAOProvider.getDao().getPoll(pollID);
        List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollID);

        req.setAttribute("poll", poll);
        req.setAttribute("pollOptions", pollOptions);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }
}
