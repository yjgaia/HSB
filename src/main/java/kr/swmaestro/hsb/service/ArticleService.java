package kr.swmaestro.hsb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.Article;
import kr.swmaestro.hsb.model.Follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

	@Autowired
	private KeyValueListCache cache;
	
	@Autowired
	private FollowService followService;
	
	public String getArticleKey(Long id) {
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
	
	public List<Article> findArticlesByWriterId(Long writerId, Long beforeArticleId, int count) {
		
		Map<String, Integer> emptyValueIndexMap = new HashMap<>();
		
		List<Article> articleList = cache.list(getUserIndexKey(writerId), beforeArticleId, count, Article.class, emptyValueIndexMap);
		
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
				
				// 그리고 원래 글 목록에 삽입.
				articleList.set(emptyValueIndexMap.get(key), article);
			}
		}
		
		// 캐시에서 불러온 글 목록 수가 예상보다 적을때...
		if (articleList.size() < count) {
			
			Long addBeforeArticleId = null;
			if (articleList.size() > 0) {
				addBeforeArticleId = articleList.get(articleList.size() - 1).getId();
			}
			
			if (addBeforeArticleId == null || addBeforeArticleId > 1) { // 마지막 글 id가 1보다 클때만 가져옴. 그 이하는 의미없음.
				List<Article> addArticleList = Article.findArticlesByWriterId(writerId, addBeforeArticleId, count - articleList.size());
				articleList.addAll(addArticleList);
				
				// 가져온 값들도 캐시에 넣어줍니다.
				for (Article article : addArticleList) {
					cacheArticle(article);
				}
			}
		}
		
		return articleList;
	}

	public List<Long> findArticleIdsByWriterId(Long writerId) {
		
		Set<String> keySet = cache.getIndexes(getUserIndexKey(writerId));
		
		List<Long> ids = new ArrayList<Long>();
		
		for (String key : keySet) {
			ids.add(getArticleIdFromKey(key));
		}
		
		return ids;
	}

	public List<Article> timelineByWriterId(Long writerId, Long beforeArticleId, int count) {
		
		Map<String, Integer> emptyValueIndexMap = new HashMap<>();
		
		List<Article> articleList = cache.list(getTimelineIndexKey(writerId), beforeArticleId, count, Article.class, emptyValueIndexMap);
		
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
				
				// 그리고 원래 글 목록에 삽입.
				articleList.set(emptyValueIndexMap.get(key), article);
			}
		}
		
		// 캐시에서 불러온 글 목록 수가 예상보다 적을때...
		if (articleList.size() < count) {
			
			Long addBeforeArticleId = null;
			if (articleList.size() > 0) {
				addBeforeArticleId = articleList.get(articleList.size() - 1).getId();
			}
			
			if (addBeforeArticleId == null || addBeforeArticleId > 1) { // 마지막 글 id가 1보다 클때만 가져옴. 그 이하는 의미없음.
				List<Article> addArticleList = Article.findArticlesByWriterId(writerId, addBeforeArticleId, count - articleList.size());
				articleList.addAll(addArticleList);
				
				// 가져온 값들도 캐시에 넣어줍니다.
				for (Article article : addArticleList) {
					cacheArticle(article);
				}
			}
		}
		
		return articleList;
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
	
}
