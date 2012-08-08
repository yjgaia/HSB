package kr.swmaestro.hsb.controller.user;

import java.util.Date;

import javax.validation.Valid;

import kr.swmaestro.hsb.data.DataCenter;
import kr.swmaestro.hsb.domain.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 심영재
 */
@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SaltSource saltSource;
	
	@Autowired
	private DataCenter dataCenter;

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public void join(UserInfo userInfo) {
		// just view
	}

	// 회원가입 처리
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(@Valid UserInfo userInfo, BindingResult bindingResult, Model model) {

		// other validations
		if (!bindingResult.hasFieldErrors("password") && !userInfo.getPassword().equals(userInfo.getPasswordConfirm())) {
			bindingResult.rejectValue("password", "Equals.userInfo.passwordConfirm");
		}
		if (!bindingResult.hasFieldErrors("username") && UserInfo.existsUser(userInfo.getUsername())) {
			bindingResult.rejectValue("username", "Exists.userInfo.username");
		}
		if (!bindingResult.hasFieldErrors("nickname") && UserInfo.existsNickname(userInfo.getNickname())) {
			bindingResult.rejectValue("nickname", "Exists.userInfo.nickname");
		}
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("userInfo", userInfo);
			return "user/join";
		} else {
			// password encoding
			userInfo.setPassword(passwordEncoder.encodePassword(userInfo.getPassword(), saltSource.getSalt(userInfo)));
			userInfo.setJoinDate(new Date());
			
			dataCenter.save(userInfo);
			
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void logn(UserInfo userInfo) {
		// just view
	}

}
