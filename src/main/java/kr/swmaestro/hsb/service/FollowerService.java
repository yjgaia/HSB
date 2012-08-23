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
	public static String getFollowerListKey(Long followedUserId){
		return "user:"+followedUserId+"followers";
	}
	public static String getFollowingListKey(Long followingUserId){
		return "user:"+followingUserId+"following";
	}
	public void saveFollower(Follower follower) {
		// RDBMS에 저장
		follower.persist();
		// 캐시에 저장
		UserInfo followedUser=UserInfo.findUserInfo(follower.getUserId());
		UserInfo followingUser=UserInfo.findUserInfo(follower.getFollowerId());
		//follower  리스트 
		String followerListKey = getFollowerListKey(follower.getUserId());
		//following 리스트
		String followingListKey = getFollowingListKey(follower.getFollowerId());
		
		addFollowerFollowingCount(followedUser, followingUser, 1);
		//following 하는 유저의  following 리스트에 추가
		cache.addSetElement(followingListKey, follower.getUserId().toString());
		//follow 대상이 되는 유저의 리스트에 follower추가  
		cache.addSetElement(followerListKey, follower.getFollowerId().toString());
		
	}
	
	//넘어온 count만큼 팔로우,팔로잉 숫자를 더해주는 함수 
	public void addFollowerFollowingCount(UserInfo followedUser,UserInfo followingUser,int count){
		
				
		followedUser.addFollowerCount(count);
		userService.saveUserInfo(followedUser);
				
		followingUser.addFollowingCount(count);
		userService.saveUserInfo(followingUser);
	}

	public void removeFollow(Follower follower) {
		//RDBMS에서 찾아오기 
		Follower foundFollowInfo=Follower.findFollowInfoByFollower(follower.getUserId(), follower.getFollowerId());
		
		//삭제 
		foundFollowInfo.remove();
		
		UserInfo followedUser=UserInfo.findUserInfo(follower.getUserId());
		UserInfo followingUser=UserInfo.findUserInfo(follower.getFollowerId());
		
		addFollowerFollowingCount(followedUser, followingUser, -1);
		
		//follower  리스트 
		String followerListKey = getFollowerListKey(follower.getUserId());
		//following 리스트
		String followingListKey = getFollowingListKey(follower.getFollowerId());
		
		//following 하는 유저의  following 리스트에서 follower 제거 
		cache.removeSetElement(followingListKey, follower.getUserId().toString());
		//follow 대상이 되는 유저의 리스트에서 follower 제거 
		cache.removeSetElement(followerListKey, follower.getFollowerId().toString());
	};
}
