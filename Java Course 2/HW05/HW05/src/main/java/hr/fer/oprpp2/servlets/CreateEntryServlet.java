package hr.fer.oprpp2.servlets;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.dao.jpa.JPAEMProvider;
import hr.fer.oprpp2.model.BlogEntry;
import hr.fer.oprpp2.model.BlogUser;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

@WebServlet(name="create_entry", urlPatterns = "/servleti/createEntry/*")
public class CreateEntryServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String nick = req.getPathInfo().substring(1);
		BlogUser user = DAOProvider.getDAO().findUser(nick);

		req.setCharacterEncoding("UTF-8");

		BlogEntry entry = readBody(req);
		entry.setCreator(user);

		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(entry);
		res.sendRedirect(req.getContextPath() + "/servleti/author/"+nick);

	}

	private BlogEntry readBody(HttpServletRequest req) throws UnsupportedEncodingException {
		BlogEntry entry = new BlogEntry();

		String title = URLDecoder.decode(req.getParameter("title"), "UTF-8");
		String text = URLDecoder.decode(req.getParameter("text"), "UTF-8");

		entry.setTitle(title);
		entry.setText(text);
		entry.setCreatedAt(new Date());

		return entry;
	}
}
