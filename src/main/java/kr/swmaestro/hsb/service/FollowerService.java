package kr.swmaestro.hsb.service;

import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.Follower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowerService {
	@Autowired
	protected KeyValueListCache cache;
	
	public void saveFollower(Follower follower) {
		// RDBMS에 저장
		follower.persist();
		// 캐시에 저장
		
		//follow 대상이 되는 유저의 리스트 
		String followedUserFollowerListKey = "user:" + follower.getUserId() +":followers";
		
		//follow 대상이 되는 유저의 리스트에 follower추가  
		cache.addSet(followedUserFollowerListKey, follower.getFollowerId().toString());
		
		
	};
}
