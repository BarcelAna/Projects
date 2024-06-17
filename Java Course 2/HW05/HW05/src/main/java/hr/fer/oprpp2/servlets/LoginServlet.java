package hr.fer.oprpp2.servlets;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(name="login", urlPatterns = "/servleti/login")
public class LoginServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String nick = URLDecoder.decode(req.getParameter("nick"), "UTF-8");
		String pass = URLDecoder.decode(req.getParameter("pass"), "UTF-8");

		BlogUser user = DAOProvider.getDAO().findUser(nick);

		boolean loginSucc= login(user, pass);
		if(!loginSucc) {
			req.setAttribute("error", "Login failed");
			req.setAttribute("nick", nick);
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, res);
		} else {
			req.getSession().setAttribute("current.user.id", user.getId());
			req.getSession().setAttribute("current.user.fn", user.getFirstName());
			req.getSession().setAttribute("current.user.ln", user.getLastName());
			req.getSession().setAttribute("current.user.nick", user.getNick());
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, res);
		}
	}

	private boolean login(BlogUser user, String pass) {
		boolean success = false;

		if(user==null) {
			return false;
		}

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		digest.update(pass.getBytes(StandardCharsets.UTF_8));
		byte[] bytes = digest.digest();
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = String.format("%02X", b);
			hexString.append(hex);
		}

		if(user.getPasswordHash().equals(hexString.toString())) {
			success=true;
		}

		return success;
	}
}
