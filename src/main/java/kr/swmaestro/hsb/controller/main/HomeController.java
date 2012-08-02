package kr.swmaestro.hsb.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 심영재
 */
@Controller
@RequestMapping
public class HomeController {

	@RequestMapping("/")
	public String home() {
		// just view
		return "home";
	}

}
