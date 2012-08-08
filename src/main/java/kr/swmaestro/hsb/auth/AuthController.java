package kr.swmaestro.hsb.auth;

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

	@RequestMapping(value = "/auth/signin", method = RequestMethod.POST)
	public String signin(@RequestParam("j_username") String username, @RequestParam("j_password") String password) {
		System.out.println(username);
		System.out.println(password);
		return "redirect:/";
	}

}
