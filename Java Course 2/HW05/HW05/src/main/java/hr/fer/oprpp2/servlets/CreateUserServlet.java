package hr.fer.oprpp2.servlets;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.dao.jpa.JPAEMProvider;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(name="createUser", urlPatterns = "/servleti/createUser")
public class CreateUserServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		BlogUser user = readBody(req);
		if(DAOProvider.getDAO().findUser(user.getNick())!=null) {
			req.setAttribute("error", "Nickname already exists.");
			req.setAttribute("user", user);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, res);
		} else {
			EntityManager em = JPAEMProvider.getEntityManager();
			em.persist(user);
			res.sendRedirect(req.getContextPath() + "/servleti/main");
		}

	}

	private BlogUser readBody(HttpServletRequest req) throws UnsupportedEncodingException {
		BlogUser user = new BlogUser();

		String fn = URLDecoder.decode(req.getParameter("firstName"), "UTF-8");
		String ln = URLDecoder.decode(req.getParameter("lastName"), "UTF-8");
		String email = URLDecoder.decode(req.getParameter("email"), "UTF-8");
		String nick = URLDecoder.decode(req.getParameter("nick"), "UTF-8");
		String pass = URLDecoder.decode(req.getParameter("pass"), "UTF-8");

		user.setFirstName(fn);
		user.setLastName(ln);
		user.setEmail(email);
		user.setNick(nick);

		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		messageDigest.update(pass.getBytes(StandardCharsets.UTF_8));
		byte[] bytes = messageDigest.digest();
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = String.format("%02X", b);
			hexString.append(hex);
		}
		user.setPasswordHash(hexString.toString());

		return user;
	}
}
