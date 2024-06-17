package hr.fer.oprpp2.servlets;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="main", urlPatterns = "/servleti/main")
public class MainServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		List<BlogUser> authors = DAOProvider.getDAO().getAuthors();
		req.getServletContext().setAttribute("authors", authors);

		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, res);
	}
}
