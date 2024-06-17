package hr.fer.oprpp2.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="logout", urlPatterns = "/servleti/logout")
public class LogOutServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		req.getSession().invalidate();
		res.sendRedirect(req.getContextPath()+"/servleti/main");
	}
}
