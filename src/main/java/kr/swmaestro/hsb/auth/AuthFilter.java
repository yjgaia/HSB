package kr.swmaestro.hsb.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AuthFilter implements Filter {
	
	private AuthManager authManager;

	private static boolean needAuth;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		authManager = (AuthManager) applicationContext.getBeansOfType(AuthManager.class).values().toArray()[0];
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		//System.out.println(authManager.getUserInfo(request));
		needAuth = false;
		
		chain.doFilter(request, response);
		
		if (needAuth) {
			// response.sendRedirect();
		}
	}

	@Override
	public void destroy() {}

	public static void setNeedAuth(boolean needAuth) {
		AuthFilter.needAuth = needAuth;
	}

}
