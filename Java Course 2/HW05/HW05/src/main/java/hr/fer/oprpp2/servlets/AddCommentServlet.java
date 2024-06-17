package hr.fer.oprpp2.servlets;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.dao.jpa.JPAEMProvider;
import hr.fer.oprpp2.model.BlogComment;
import hr.fer.oprpp2.model.BlogEntry;

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

@WebServlet(name="addComment", urlPatterns = "/servleti/addComment")
public class AddCommentServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		Long id = null;
		try {
			id = Long.parseLong(req.getParameter("id"));
		} catch (NumberFormatException e) {
			req.getRequestDispatcher("/WEB-INF/pages/NotFound.jsp").forward(req, res);
			return;
		}
		BlogEntry entry = DAOProvider.getDAO().getEntry(id);
		addComment(entry, req);
		res.sendRedirect(req.getContextPath()+"/servleti/author/"+entry.getCreator().getNick()+"/"+entry.getId());
	}

	private void addComment(BlogEntry entry, HttpServletRequest req) throws UnsupportedEncodingException {
		String message = URLDecoder.decode(req.getParameter("new_comment"), "UTF-8");
		String email = URLDecoder.decode(req.getParameter("userEmail"), "UTF-8");
		/*if(email.equals("null")) {
			email="anonymous";
		}*/
		BlogComment blogComment = new BlogComment();
		blogComment.setMessage(message);
		blogComment.setPostedOn(new Date());
		blogComment.setBlogEntry(entry);
		blogComment.setUsersEMail(email);

		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(blogComment);
	}
}
