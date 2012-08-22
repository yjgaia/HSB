package kr.swmaestro.hsb.service;

import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.Follower;
import kr.swmaestro.hsb.model.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowerService {
	@Autowired
	protected KeyValueListCache cache;
	
	@Autowired
	protected UserService userService;
	
	public void saveFollower(Follower follower) {
		// RDBMS에 저장
		follower.persist();
		// 캐시에 저장
		UserInfo followedUser=UserInfo.findUserInfo(follower.getUserId());
		UserInfo followingUser=UserInfo.findUserInfo(follower.getFollowerId());
		//follower  리스트 
		String followerListKey = "user:" + follower.getUserId() +":followers";
		//following 리스트
		String followingListKey = "user:" + follower.getFollowerId()+":following";
		
		followedUser.increaseFollowerCount();
		userService.saveUserInfo(followedUser);
		//following 하는 유저의  following 리스트에 추가
		cache.addSet(followingListKey, follower.getUserId().toString());
		
		followingUser.increaseFollowingCount();
		userService.saveUserInfo(followingUser);
		//follow 대상이 되는 유저의 리스트에 follower추가  
		cache.addSet(followerListKey, follower.getFollowerId().toString());
		
	};
}
