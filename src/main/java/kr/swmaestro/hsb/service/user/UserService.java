package kr.swmaestro.hsb.service.user;

import kr.swmaestro.hsb.domain.UserInfo;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author 심영재
 */
@Service
public class UserService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		return UserInfo.findUserInfoByUsername(username);
	}

}
