package hr.fer.oprpp2.servlets;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.model.BlogEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@WebServlet(name="editEntry", urlPatterns = "/servleti/editEntry")
public class EditEntryServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		Long entryId = null;
		try {
			entryId = Long.parseLong(req.getParameter("id"));
		} catch (NumberFormatException e) {
			req.getRequestDispatcher("/WEB-INF/pages/NotFound.jsp").forward(req, res);
			return;
		}
		BlogEntry entry = DAOProvider.getDAO().getEntry(entryId);
		String nick = entry.getCreator().getNick();

		updateEntry(entry, req);

		res.sendRedirect(req.getContextPath() + "/servleti/author/"+nick);
	}

	private void updateEntry(BlogEntry entry, HttpServletRequest req) throws UnsupportedEncodingException {
		boolean updated = false;

		if(!entry.getTitle().equals(req.getParameter("title"))) {
			entry.setTitle(req.getParameter("title"));
			updated = true;
		}
		if(!entry.getText().equals(req.getParameter("text"))) {
			entry.setText(req.getParameter("text"));
			updated = true;
		}

		if(updated) {
			entry.setLastModifiedAt(new Date());
		}

	}
}
