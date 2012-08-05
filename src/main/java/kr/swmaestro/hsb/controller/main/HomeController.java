package kr.swmaestro.hsb.controller.main;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	public String home(HttpSession session) {
		// just view
		System.out.println("sessionId:"+session.getId());
		client.set("sessionId:"+session.getId(), 60, 1);
		return "home";
	}

}
