package hr.fer.oprpp2.servlets;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.model.BlogEntry;
import hr.fer.oprpp2.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="entries", urlPatterns = "/servleti/author/*")
public class AuthorEntriesServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String pathInfo = req.getPathInfo().substring(1);

		if(pathInfo.contains("/")) { //ako je nick/new ili nick/edit ili nick/EID
			String nick = pathInfo.substring(0, pathInfo.indexOf('/'));
			BlogUser user = DAOProvider.getDAO().findUser(nick);
			if(user==null) {
				req.getRequestDispatcher("/WEB-INF/pages/NotFound.jsp").forward(req, res);
			} else {
				req.setAttribute("user", user);

				String operation = pathInfo.substring(pathInfo.indexOf('/') + 1);
				if (operation.equals("new")) {
					if (user.getNick().equals(req.getSession().getAttribute("current.user.nick"))) {
						req.getRequestDispatcher("/WEB-INF/pages/createEntry.jsp").forward(req, res);
					} else {
						req.setAttribute("path", pathInfo);
						req.getRequestDispatcher("/WEB-INF/pages/Forbidden.jsp").forward(req, res);
					}
				} else if (operation.equals("edit")) {
					if (user.getNick().equals(req.getSession().getAttribute("current.user.nick"))) {
						Long entryId = null;
						try {
							entryId = Long.parseLong(req.getParameter("id"));
						} catch (NumberFormatException e) {
							req.getRequestDispatcher("/WEB-INF/pages/NotFound.jsp").forward(req, res);
							return;
						}
						BlogEntry entry = DAOProvider.getDAO().getEntry(entryId);
						if (entry == null) {
							req.getRequestDispatcher("/WEB-INF/pages/NotFound.jsp").forward(req, res);
						} else {
							req.setAttribute("entry", entry);
							req.getRequestDispatcher("/WEB-INF/pages/editEntry.jsp").forward(req, res);
						}
					} else {
						req.setAttribute("path", pathInfo);
						req.getRequestDispatcher("/WEB-INF/pages/Forbidden.jsp").forward(req, res);
					}
				} else {
					Long entryId = null;
					try {
						entryId = Long.parseLong(operation);
					} catch (NumberFormatException e) {
						req.getRequestDispatcher("/WEB-INF/pages/NotFound.jsp").forward(req, res);
						return;
					}
					BlogEntry entry = DAOProvider.getDAO().getEntry(entryId);
					if (entry == null) {
						req.getRequestDispatcher("/WEB-INF/pages/NotFound.jsp").forward(req, res);
					} else {
						req.setAttribute("entry", entry);
						req.getRequestDispatcher("/WEB-INF/pages/showEntry.jsp").forward(req, res);
					}
				}
			}
		} else {
			String nick = pathInfo;
			BlogUser user = DAOProvider.getDAO().findUser(nick);
			if(user==null) {
				req.getRequestDispatcher("/WEB-INF/pages/NotFound.jsp").forward(req, res);
			} else {
				req.setAttribute("user", user);
				req.getRequestDispatcher("/WEB-INF/pages/entries.jsp").forward(req, res);
			}
		}
	}

}
