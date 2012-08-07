package kr.swmaestro.hsb.controller.main;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import kr.swmaestro.hsb.annotation.NeedAuth;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.couchbase.client.CouchbaseClient;

/**
 * @author 심영재
 */
@Controller
@RequestMapping
public class HomeController {

	@Resource(name="couchbaseClient")
	CouchbaseClient client;
	@RequestMapping("/")
	public String home(HttpServletRequest request) {
		// just view
		System.out.println("home");
		return "home";
	}
	
	@NeedAuth("ROLE_USER")
	@RequestMapping("/test")
	public void test() {
		
	}

}
