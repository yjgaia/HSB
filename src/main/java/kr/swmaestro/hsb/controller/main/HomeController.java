package kr.swmaestro.hsb.controller.main;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import kr.swmaestro.hsb.auth.Auth;
import kr.swmaestro.hsb.data.CouchbaseCacheManager;
import kr.swmaestro.hsb.util.CookieBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.couchbase.client.CouchbaseClient;

/**
 * @author 심영재
 */
@Controller
@RequestMapping
public class HomeController {
	
	@Autowired
	private CouchbaseCacheManager keyValueCacheManager;

	@Resource(name="couchbaseClient")
	CouchbaseClient client;
	@RequestMapping("/")
	public String home(HttpServletRequest request) throws IOException {
		// just view
		String key = new CookieBox(request).getValue(Auth.COOKIE_KEY);
		System.out.println(keyValueCacheManager.get(key));
		return "home";
	}

}
