package kr.swmaestro.hsb.service;

import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	protected KeyValueListCache cache;
	
	// 저장과 수정을 담당
	public void saveUserInfo(UserInfo userInfo) {
		// RDBMS에 저장
		userInfo.merge();
		// 캐시에 저장
		cache.set("user:" + userInfo.getId(), this);
	}
	
	// 유저 정보 제거
	public void deleteUserInfo(UserInfo userInfo) {
		userInfo.setEnable(false);
		userInfo.merge();
		cache.delete("user:" + userInfo.getId());
	}

}
