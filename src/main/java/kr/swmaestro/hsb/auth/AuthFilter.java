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

import kr.swmaestro.hsb.data.KeyValueCacheManager;
import kr.swmaestro.hsb.util.CookieBox;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AuthFilter implements Filter {
	
	private KeyValueCacheManager keyValueCacheManager;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		keyValueCacheManager = (KeyValueCacheManager) applicationContext.getBeansOfType(KeyValueCacheManager.class).values().toArray()[0];
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String uid = new CookieBox(request).getValue(Auth.COOKIE_KEY);
		
		// 여기서 인증값이 있으면 인증처리
		System.out.println("!!~" + keyValueCacheManager.get(uid));
		request.setAttribute("authUser", "TEST_SIGN_IN_USER");
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}

}
