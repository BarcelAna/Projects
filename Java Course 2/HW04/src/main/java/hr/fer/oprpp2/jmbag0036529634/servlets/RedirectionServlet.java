package hr.fer.oprpp2.jmbag0036529634.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="redirect_to_index", urlPatterns = {"/index.html"})
public class RedirectionServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.sendRedirect(req.getContextPath()+"/servleti/index.html"); //PROVJERI OVO
	}
}
