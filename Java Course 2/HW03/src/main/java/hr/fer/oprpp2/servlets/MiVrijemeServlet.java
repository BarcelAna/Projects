package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@WebServlet(name="vrijeme", urlPatterns = {"/mi/vrijeme"})
public class MiVrijemeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();

        req.getServletContext().setAttribute("currentTime", dtf.format(currentTime));
        String[] images = new String[] {"Barca.jpg", "Gitara.png"};
        if(req.getSession().getAttribute("bgImage") == null) {
            int randomInd = new Random().nextInt(2);
            req.getServletContext().setAttribute("bgUrl", "/webapp2/images/" + images[randomInd]);
        }
        req.getRequestDispatcher("/WEB-INF/pages/vrijeme.jsp").forward(req, response);
    }

}
