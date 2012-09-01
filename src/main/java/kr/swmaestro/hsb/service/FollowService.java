package kr.swmaestro.hsb.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.swmaestro.hsb.data.JsonIgnoreResultModelPropertyesMixIn;
import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.Follow;
import kr.swmaestro.hsb.model.UserInfo;
import kr.swmaestro.hsb.util.JsonXmlUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
	@Autowired
	protected KeyValueListCache cache;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	private ArticleService articleService;
	
	public static String getFollowerListKey(Long followedUserId){
		return generateKeyByUser(followedUserId)+":followers";
	}
	public static String getFollowingListKey(Long followingUserId){
		return generateKeyByUser(followingUserId)+":following";
	}
	public static String generateKeyByUser(Long id){
		return "user:"+id;
	}
	public void saveFollower(Follow follow) {
		// RDBMS에 저장
		follow.persist();
		// 캐시에 저장
		UserInfo followedUser=UserInfo.findUserInfo(follow.getTargetUserId());
		UserInfo followingUser=UserInfo.findUserInfo(follow.getFollowerId());
		//follower  리스트 
		String followerListKey = getFollowerListKey(follow.getTargetUserId());
		//following 리스트
		String followingListKey = getFollowingListKey(follow.getFollowerId());
		
		addFollowerFollowingCount(followedUser, followingUser, 1);
		//following 하는 유저의  following 리스트에 추가
		cache.addSetElement(followingListKey, generateKeyByUser(follow.getTargetUserId()));
		//follow 대상이 되는 유저의 리스트에 follower추가  
		cache.addSetElement(followerListKey, generateKeyByUser(follow.getFollowerId()));
		
		// 캐시에 저장된 id를 타임라인 캐시에 삽입
		List<Long> articleIds = articleService.findArticleIdsByWriterId(follow.getTargetUserId());
		for (Long articleId : articleIds) {
			cache.addIndex(articleService.getTimelineIndexKey(follow.getFollowerId()), articleId, articleService.getArticleKey(articleId));
		}
	}
	
	//넘어온 count만큼 팔로우,팔로잉 숫자를 더해주는 함수 
	public void addFollowerFollowingCount(UserInfo followedUser,UserInfo followingUser,int count){
				
		followedUser.addFollowerCount(count);
		userService.saveUserInfo(followedUser);
				
		followingUser.addFollowingCount(count);
		userService.saveUserInfo(followingUser);
	}

	public void removeFollow(Follow follow) {
		//RDBMS에서 찾아오기 
		Follow foundFollowInfo=Follow.findFollowInfoByFollower(follow.getTargetUserId(), follow.getFollowerId());
		
		//삭제 
		foundFollowInfo.remove();
		
		UserInfo followedUser=UserInfo.findUserInfo(follow.getTargetUserId());
		UserInfo followingUser=UserInfo.findUserInfo(follow.getFollowerId());
		
		addFollowerFollowingCount(followedUser, followingUser, -1);
		
		//follower  리스트 
		String followerListKey = getFollowerListKey(follow.getTargetUserId());
		//following 리스트
		String followingListKey = getFollowingListKey(follow.getFollowerId());
		
		//following 하는 유저의  following 리스트에서 follower 제거 
		cache.removeSetElement(followingListKey, generateKeyByUser(follow.getTargetUserId()));
		//follow 대상이 되는 유저의 리스트에서 follower 제거 
		cache.removeSetElement(followerListKey, generateKeyByUser(follow.getFollowerId()));
		
		// 캐시에 저장된 id를 타임라인 캐시에서 제거
		List<Long> articleIds = articleService.findArticleIdsByWriterId(follow.getTargetUserId());
		for (Long articleId : articleIds) {
			cache.removeOrderedSetElement(articleService.getTimelineIndexKey(follow.getFollowerId()), articleService.getArticleKey(articleId));
		}
	};
	
	public List<Follow> getFollowListByTargetUserId(Long targetUserId) {
		return Follow.findFollowsByTargetUserId(targetUserId);
	}
	
	public List<String> getFollowingXmlListByUserInfo(UserInfo userInfo) {
		return JsonXmlUtil.jsonListToXmlList(getFollowingJsonListByUserInfo(userInfo));
	}
	public List<String> getFollowingJsonListByUserInfo(UserInfo userInfo) {
		String followingListKey=getFollowingListKey(userInfo.getId());
		Set<String> followingSet=cache.getSetByKey(followingListKey);
		Map<String, Integer> emptyValueIndexMap = new HashMap<>();
		List<String> cachedUserJsonList=cache.getCachedList(followingSet, emptyValueIndexMap);
		
		// 비어있는 값들이 있을때...
		if (emptyValueIndexMap.size() > 0) {
					
			List<Long> userIdList = new ArrayList<>();
			for (String key : emptyValueIndexMap.keySet()) {
			// user:{id}
				Long id = Long.parseLong(key.substring(5));
				userIdList.add(id);
			}
			
			List<UserInfo> addUserInfoList = UserInfo.findUsersByIds(userIdList);
			// 가져온 값들도 캐시에 넣어줍니다.
			for (UserInfo user : addUserInfoList) {
				String key = cacheUserInfo(user);
								
				ObjectMapper om = new ObjectMapper();
				try {
					// 필요없는 property 제외
					om.getSerializationConfig().addMixInAnnotations(user.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
					// 그리고 원래 글 목록에 삽입.
					cachedUserJsonList.set(emptyValueIndexMap.get(key), om.writeValueAsString(user));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//캐싱된 값과 followingCount가 다름 
		if(userInfo.getFollowingCount()>cachedUserJsonList.size()){
			System.out.println("캐싱 된것 없음");
			List<UserInfo> followingListInDB= UserInfo.getFollowingListById(userInfo.getId());
			List<String> followerJsonListInDB = new ArrayList<>();
			for(UserInfo user: followingListInDB){
				String key=cacheUserInfo(user);
				cache.addSetElement(followingListKey, key);
				
				ObjectMapper om = new ObjectMapper();
				try {
					// 필요없는 property 제외
					om.getSerializationConfig().addMixInAnnotations(user.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
					// 그리고 원래 글 목록에 삽입.
					followerJsonListInDB.add(om.writeValueAsString(user));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return followerJsonListInDB;
		}
		
		return cachedUserJsonList;
	}
	private String cacheUserInfo(UserInfo user) {
		String key=generateKeyByUser(user.getId());
		cache.set(key,user);
		return key;
	}
	
	public List<String> getFollowerXmlListByUserInfo(UserInfo userInfo) {
		return JsonXmlUtil.jsonListToXmlList(getFollowerJsonListByUserInfo(userInfo));
	}
	
	public List<String> getFollowerJsonListByUserInfo(UserInfo userInfo) {
		String followerListKey=getFollowerListKey(userInfo.getId());
		Set<String> followerSet=cache.getSetByKey(followerListKey);
		Map<String, Integer> emptyValueIndexMap = new HashMap<>();
		List<String> cachedUserJsonList = cache.getCachedList(followerSet, emptyValueIndexMap);
		
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
				
				ObjectMapper om = new ObjectMapper();
				try {
					// 필요없는 property 제외
					om.getSerializationConfig().addMixInAnnotations(user.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
					// 그리고 원래 글 목록에 삽입.
					cachedUserJsonList.set(emptyValueIndexMap.get(key), om.writeValueAsString(user));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//캐싱된 값과 followingCount가 다름 
		if(userInfo.getFollowerCount()>cachedUserJsonList.size()){
			System.out.println("캐싱 된것 없음");
			List<UserInfo> followerListInDB= UserInfo.getFollowerListById(userInfo.getId());
			List<String> followerJsonListInDB = new ArrayList<>();
			for(UserInfo user: followerListInDB){
				String key=cacheUserInfo(user);
				cache.addSetElement(followerListKey, key);
				
				ObjectMapper om = new ObjectMapper();
				try {
					// 필요없는 property 제외
					om.getSerializationConfig().addMixInAnnotations(user.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
					// 그리고 원래 글 목록에 삽입.
					followerJsonListInDB.add(om.writeValueAsString(user));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return followerJsonListInDB;
		}
		
		return cachedUserJsonList;
	}
}
