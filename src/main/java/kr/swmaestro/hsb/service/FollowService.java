package kr.swmaestro.hsb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.Article;
import kr.swmaestro.hsb.model.Follow;
import kr.swmaestro.hsb.model.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
	@Autowired
	protected KeyValueListCache cache;
	
	@Autowired
	protected UserService userService;
	public static String getFollowerListKey(Long followedUserId){
		return generateKeyByUser(followedUserId)+":followers";
	}
	public static String getFollowingListKey(Long followingUserId){
		return generateKeyByUser(followingUserId)+":following";
	}
	public static String generateKeyByUser(Long id){
		return "user:"+id;
	}
	public void saveFollower(Follow follower) {
		// RDBMS에 저장
		follower.persist();
		// 캐시에 저장
		UserInfo followedUser=UserInfo.findUserInfo(follower.getTargetUserId());
		UserInfo followingUser=UserInfo.findUserInfo(follower.getFollowerId());
		//follower  리스트 
		String followerListKey = getFollowerListKey(follower.getTargetUserId());
		//following 리스트
		String followingListKey = getFollowingListKey(follower.getFollowerId());
		
		addFollowerFollowingCount(followedUser, followingUser, 1);
		//following 하는 유저의  following 리스트에 추가
		cache.addSetElement(followingListKey, generateKeyByUser(follower.getTargetUserId()));
		//follow 대상이 되는 유저의 리스트에 follower추가  
		cache.addSetElement(followerListKey, generateKeyByUser(follower.getFollowerId()));
		
	}
	
	//넘어온 count만큼 팔로우,팔로잉 숫자를 더해주는 함수 
	public void addFollowerFollowingCount(UserInfo followedUser,UserInfo followingUser,int count){
		
				
		followedUser.addFollowerCount(count);
		userService.saveUserInfo(followedUser);
				
		followingUser.addFollowingCount(count);
		userService.saveUserInfo(followingUser);
	}

	public void removeFollow(Follow follower) {
		//RDBMS에서 찾아오기 
		Follow foundFollowInfo=Follow.findFollowInfoByFollower(follower.getTargetUserId(), follower.getFollowerId());
		
		//삭제 
		foundFollowInfo.remove();
		
		UserInfo followedUser=UserInfo.findUserInfo(follower.getTargetUserId());
		UserInfo followingUser=UserInfo.findUserInfo(follower.getFollowerId());
		
		addFollowerFollowingCount(followedUser, followingUser, -1);
		
		//follower  리스트 
		String followerListKey = getFollowerListKey(follower.getTargetUserId());
		//following 리스트
		String followingListKey = getFollowingListKey(follower.getFollowerId());
		
		//following 하는 유저의  following 리스트에서 follower 제거 
		cache.removeSetElement(followingListKey, generateKeyByUser(follower.getTargetUserId()));
		//follow 대상이 되는 유저의 리스트에서 follower 제거 
		cache.removeSetElement(followerListKey, generateKeyByUser(follower.getFollowerId()));
	};
	
	public List<Follow> getFollowListByTargetUserId(Long targetUserId) {
		return Follow.findFollowsByTargetUserId(targetUserId);
	}
	public List<UserInfo> getFollowingListByUserInfo(UserInfo userInfo) {
		String folliwingListKey=getFollowingListKey(userInfo.getId());
		Set<String> followingSet=cache.getSetByKey(folliwingListKey);
		Map<String, Integer> emptyValueIndexMap = new HashMap<>();
		List<UserInfo> cachedUserList=cache.getCachedList(followingSet,UserInfo.class, emptyValueIndexMap);
		
		// 비어있는 값들이 있을때...
		if (emptyValueIndexMap.size() > 0) {
					
			List<Long> userIdList = new ArrayList<>();
			for (String key : emptyValueIndexMap.keySet()) {
			// article:{id}
				Long id = Long.parseLong(key.substring(5));
				userIdList.add(id);
			}
			
			List<UserInfo> addUserInfoList = UserInfo.findUsersByIds(userIdList);
			// 가져온 값들도 캐시에 넣어줍니다.
			for (UserInfo user : addUserInfoList) {
				String key = cacheUserInfo(user);
				// 그리고 원래 글 목록에 삽입.
				cachedUserList.set(emptyValueIndexMap.get(key), user);
			}
		}
		
		//캐싱된 값과 followingCount가 다름 
		if(userInfo.getFollowingCount()>cachedUserList.size()){
			System.out.println("캐싱 된것 없음");
			List<UserInfo> followingListInDB= UserInfo.getFollowingListById(userInfo.getId());
			for(UserInfo user: followingListInDB){
				String key=cacheUserInfo(user);
				cache.addSetElement(folliwingListKey, key);
			}
			
			return followingListInDB;
		}
		
		return cachedUserList;
	}
	private String cacheUserInfo(UserInfo user) {
		String key=generateKeyByUser(user.getId());
		cache.set(key,user);
		return key;
	}
}
