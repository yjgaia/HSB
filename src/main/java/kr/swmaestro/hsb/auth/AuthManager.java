package kr.swmaestro.hsb.auth;

import java.util.UUID;

import kr.swmaestro.hsb.data.KeyValueCache;
import kr.swmaestro.hsb.model.UserInfo;
import kr.swmaestro.hsb.util.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthManager {
	
	@Autowired
	private KeyValueCache keyValueCache;
	
	public UserInfo getUserInfo(String secureKey) {
		return (UserInfo) keyValueCache.get(secureKey, UserInfo.class);
	}
	
	public void setUserInfo(String secureKey, UserInfo userInfo) {
		keyValueCache.set(secureKey, userInfo);
	}
	
	public Long getUserId(String secureKey) {
		UserInfo userInfo = getUserInfo(secureKey);
		return userInfo == null ? null : userInfo.getId();
	}
	
	public String auth(UserInfo userInfo) {
		String secureKey = PasswordEncoder.encodePassword(UUID.randomUUID().toString());
		keyValueCache.set(secureKey, userInfo);
		return secureKey;
	}
	
	public void unauth(String secureKey) {
		keyValueCache.del(secureKey);
	}
	
	public boolean isAnonymous(String secureKey) {
		return getUserInfo(secureKey) == null;
	}

	public boolean isAuthenticated(String secureKey) {
		return getUserInfo(secureKey) != null;
	}

}
