package kr.swmaestro.hsb.auth;

import java.io.IOException;
import java.util.UUID;

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
			String uid = UUID.randomUUID().toString();
			response.addCookie(CookieBox.createCookie(Auth.COOKIE_KEY, uid, "/"));
			keyValueCacheManager.set(uid, "test");
		}
		return "redirect:/";
	}

}
