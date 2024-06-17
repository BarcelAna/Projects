package hr.fer.oprpp2.jmbag0036529634.servlets.glasanje;

import hr.fer.oprpp2.jmbag0036529634.dao.DAOProvider;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="dodajglas", urlPatterns = {"/servleti/dodaj_glas"})
public class DodajGlasServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("optID"));
        DAOProvider.getDao().upadateVotes(id);
        long pollID = DAOProvider.getDao().getPollID(id);
        resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID="+pollID);
    }
}
