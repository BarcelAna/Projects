package hr.fer.oprpp2.jmbag0036529634.servlets;

import hr.fer.oprpp2.jmbag0036529634.dao.DAOProvider;
import hr.fer.oprpp2.jmbag0036529634.model.Poll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="list_polls", urlPatterns = {"/servleti/index.html"})
public class ListPollsServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		List<Poll> polls = DAOProvider.getDao().getPolls();
		req.setAttribute("polls", polls);
		req.getRequestDispatcher("/WEB-INF/pages/listPolls.jsp").forward(req, res);
	}
}
