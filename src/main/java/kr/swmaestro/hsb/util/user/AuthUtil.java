package kr.swmaestro.hsb.util.user;


import kr.swmaestro.hsb.model.UserInfo;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 심영재
 */
public class AuthUtil {

	private AuthUtil() {
		// 임의 생성 금지
	}

	public static UserInfo getUserInfo() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext == null) {
			return null;
		}
		Authentication authentication = securityContext.getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object princiapl = authentication.getPrincipal();
		if (!(princiapl instanceof UserInfo)) {
			return null;
		}
		return (UserInfo) princiapl;
	}
	
	public static String getUsername() {
		UserInfo userInfo = getUserInfo();
		return userInfo == null ? null : userInfo.getUsername();
	}

	public static boolean isAnonymous() {
		return getUserInfo() == null;
	}

	public static boolean isAuthenticated() {
		return getUserInfo() != null;
	}

}
