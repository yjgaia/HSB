package kr.swmaestro.hsb.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.swmaestro.hsb.data.KeyValueCacheManager;
import kr.swmaestro.hsb.util.CookieBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 심영재
 */
@Controller
public class AuthController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SaltSource saltSource;
	
	@Autowired
	private KeyValueCacheManager keyValueCacheManager;

	@RequestMapping(value = "/auth/signin", method = RequestMethod.POST)
	public String signin(@RequestParam("j_username") String username, @RequestParam("j_password") String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(username);
		System.out.println(password);
		
		if (true) { // 인증되면 쿠키를 생성후 삽입
			
			String uid = keyValueCacheManager.put("test");
			response.addCookie(CookieBox.createCookie(Auth.COOKIE_KEY, uid, "/"));
			
			// 만약 remember me 기능이 추가된다면 cookie 유지 시간만 늘이면 됩니다.
		}
		return "redirect:/";
	}
	
	@RequestMapping("/auth/signout")
	public String signout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String uid = new CookieBox(request).getValue(Auth.COOKIE_KEY);
		keyValueCacheManager.remove(uid);
		// 쿠키 제거
		response.addCookie(CookieBox.createCookie(Auth.COOKIE_KEY, uid, "/", 0));
		
		return "redirect:/";
	}

}
