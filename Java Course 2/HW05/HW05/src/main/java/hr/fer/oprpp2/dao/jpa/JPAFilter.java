package hr.fer.oprpp2.dao.jpa;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = "/servleti/*")
public class JPAFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} finally {
			JPAEMProvider.close();
		}
	}

	@Override
	public void destroy() {

	}
}
