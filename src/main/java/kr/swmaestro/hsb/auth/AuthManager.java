package kr.swmaestro.hsb.auth;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.swmaestro.hsb.data.KeyValueCache;
import kr.swmaestro.hsb.model.UserInfo;
import kr.swmaestro.hsb.util.CookieBox;
import kr.swmaestro.hsb.util.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthManager {
	
	@Autowired
	private KeyValueCache keyValueCache;
	
	public UserInfo getUserInfo(HttpServletRequest request) {
		String key = null;
		try {
			key = new CookieBox(request).getValue(Auth.COOKIE_KEY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (UserInfo) keyValueCache.get(key);
	}
	
	public Long getUserId(HttpServletRequest request) {
		UserInfo userInfo = getUserInfo(request);
		return userInfo == null ? null : userInfo.getId();
	}
	
	public void auth(UserInfo userInfo, HttpServletResponse response) {
		String key = PasswordEncoder.encodePassword(UUID.randomUUID().toString());
		keyValueCache.set(key, userInfo);
		try {
			response.addCookie(CookieBox.createCookie(Auth.COOKIE_KEY, key, "/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isAnonymous(HttpServletRequest request) {
		return getUserInfo(request) == null;
	}

	public boolean isAuthenticated(HttpServletRequest request) {
		return getUserInfo(request) != null;
	}

}
