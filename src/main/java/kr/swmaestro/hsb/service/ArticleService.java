package kr.swmaestro.hsb.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.swmaestro.hsb.data.JsonIgnoreResultModelPropertyesMixIn;
import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.Article;
import kr.swmaestro.hsb.model.Follow;
import kr.swmaestro.hsb.util.JsonXmlUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

	@Autowired
	private KeyValueListCache cache;
	
	@Autowired
	private FollowService followService;
	
	public static String getArticleKey(Long id) {
		return "article:" + id;
	}
	
	// 캐시에 저장
	private String cacheArticle(Article article) {
		String articleKey = getArticleKey(article.getId());
		cache.set(articleKey, article);
		return articleKey;
	}
	
	private String getUserIndexKey(Long userId) {
		return "user:" + userId + ":articles";
	}
	
	public String getTimelineIndexKey(Long userId) {
		return "user:" + userId + ":timeline";
	}
	
	private Long getArticleIdFromKey(String key) {
		// article:{id}
		return Long.parseLong(key.substring(8));
	}
	
	public List<String> findArticleXmlsByWriterId(Long writerId, Long beforeArticleId, int count) {
		return JsonXmlUtil.jsonListToXmlList(findArticleJsonsByWriterId(writerId, beforeArticleId, count));
	}
	
	public List<String> findArticleJsonsByWriterId(Long writerId, Long beforeArticleId, int count) {
		
		Map<String, Integer> emptyValueIndexMap = new HashMap<>();
		
		List<String> articleJsonList = cache.list(getUserIndexKey(writerId), beforeArticleId, count, emptyValueIndexMap);
		
		// 비어있는 값들이 있을때...
		if (emptyValueIndexMap.size() > 0) {
			
			List<Long> articleIdList = new ArrayList<>();
			for (String key : emptyValueIndexMap.keySet()) {
				// article:{id}
				articleIdList.add(getArticleIdFromKey(key));
			}
			
			List<Article> addArticleList = Article.findArticlesByIds(articleIdList);
			
			// 가져온 값들도 캐시에 넣어줍니다.
			for (Article article : addArticleList) {
				String key = cacheArticle(article);
				
				ObjectMapper om = new ObjectMapper();
				try {
					// 필요없는 property 제외
					om.getSerializationConfig().addMixInAnnotations(article.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
					// 그리고 원래 글 목록에 삽입.
					articleJsonList.set(emptyValueIndexMap.get(key), om.writeValueAsString(article));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// 캐시에서 불러온 글 목록 수가 예상보다 적을때...
		if (articleJsonList.size() < count) {
			
			Long addBeforeArticleId = null;
			if (articleJsonList.size() > 0) {
				
				ObjectMapper om = new ObjectMapper();
				try {
					addBeforeArticleId = om.readValue(articleJsonList.get(articleJsonList.size() - 1), Article.class).getId();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (addBeforeArticleId == null || addBeforeArticleId > 1) { // 마지막 글 id가 1보다 클때만 가져옴. 그 이하는 의미없음.
				List<Article> addArticleList = Article.findArticlesByWriterId(writerId, addBeforeArticleId, count - articleJsonList.size());
				
				// 가져온 값들도 캐시에 넣어줍니다.
				for (Article article : addArticleList) {
					cacheArticle(article);
					
					ObjectMapper om = new ObjectMapper();
					try {
						// 필요없는 property 제외
						om.getSerializationConfig().addMixInAnnotations(article.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
						// 그리고 원래 글 목록에 삽입.
						articleJsonList.add(om.writeValueAsString(article));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return articleJsonList;
	}

	public List<Long> findArticleIdsByWriterId(Long writerId) {
		
		Set<String> keySet = cache.getIndexes(getUserIndexKey(writerId));
		
		List<Long> ids = new ArrayList<Long>();
		if(keySet!=null){
			for (String key : keySet) {
				ids.add(getArticleIdFromKey(key));
			}
		}
		return ids;
	}

	public List<String> timelineXmlByWriterId(Long writerId, Long beforeArticleId, int count) {
		return JsonXmlUtil.jsonListToXmlList(timelineJsonByWriterId(writerId, beforeArticleId, count));
	}
	
	public List<String> timelineJsonByWriterId(Long writerId, Long beforeArticleId, int count) {
		
		Map<String, Integer> emptyValueIndexMap = new HashMap<>();
		
		List<String> articleJsonList = cache.list(getTimelineIndexKey(writerId), beforeArticleId, count, emptyValueIndexMap);
		
		// 비어있는 값들이 있을때...
		if (emptyValueIndexMap.size() > 0) {
			
			List<Long> articleIdList = new ArrayList<>();
			for (String key : emptyValueIndexMap.keySet()) {
				// article:{id}
				Long id = Long.parseLong(key.substring(8));
				articleIdList.add(id);
			}
			
			List<Article> addArticleList = Article.findArticlesByIds(articleIdList);
			
			// 가져온 값들도 캐시에 넣어줍니다.
			for (Article article : addArticleList) {
				String key = cacheArticle(article);
				
				ObjectMapper om = new ObjectMapper();
				try {
					// 필요없는 property 제외
					om.getSerializationConfig().addMixInAnnotations(article.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
					// 그리고 원래 글 목록에 삽입.
					articleJsonList.set(emptyValueIndexMap.get(key), om.writeValueAsString(article));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// 캐시에서 불러온 글 목록 수가 예상보다 적을때...
		if (articleJsonList.size() < count) {
			
			Long addBeforeArticleId = null;
			if (articleJsonList.size() > 0) {
				
				ObjectMapper om = new ObjectMapper();
				try {
					addBeforeArticleId = om.readValue(articleJsonList.get(articleJsonList.size() - 1), Article.class).getId();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (addBeforeArticleId == null || addBeforeArticleId > 1) { // 마지막 글 id가 1보다 클때만 가져옴. 그 이하는 의미없음.
				List<Article> addArticleList = Article.findArticlesByWriterId(writerId, addBeforeArticleId, count - articleJsonList.size());
				
				// 가져온 값들도 캐시에 넣어줍니다.
				for (Article article : addArticleList) {
					cacheArticle(article);
					
					ObjectMapper om = new ObjectMapper();
					try {
						// 필요없는 property 제외
						om.getSerializationConfig().addMixInAnnotations(article.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
						// 그리고 원래 글 목록에 삽입.
						articleJsonList.add(om.writeValueAsString(article));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return articleJsonList;
	}
	
	public void saveArticle(Article article) {
		// RDBMS에 저장
		article.persist();
		
		// 캐시에 저장
		String key = cacheArticle(article);
		cache.addIndex(getUserIndexKey(article.getWriterId()), article.getId(), key);
		
		// 작성자를 팔로우하는 목록을 가져와 캐싱
		List<Follow> followList = followService.getFollowListByTargetUserId(article.getWriterId());
		for (Follow follow : followList) {
			// 팔로어들의 타임라인에도 뜨게
			cache.addIndex(getTimelineIndexKey(follow.getFollowerId()), article.getId(), key);
		}
	};
	
	public void deleteArticle(Article article) {
		// RDBMS에서 제거
		article.delete();
		
		//TODO 댓글 목록도 전부 제거해줌 + 캐싱된 것도 제거 
		
		// 캐시에서 제거
		String key = getArticleKey(article.getId());
		cache.delete(key);
		cache.removeIndex(getUserIndexKey(article.getWriterId()), key);
		
		// 작성자를 팔로우하는 목록에서도 제거
		List<Follow> followList = followService.getFollowListByTargetUserId(article.getWriterId());
		for (Follow follow : followList) {
			// 팔로어들의 타임라인에서 제거
			cache.removeIndex(getTimelineIndexKey(follow.getFollowerId()), key);
		}
	}
	
}
