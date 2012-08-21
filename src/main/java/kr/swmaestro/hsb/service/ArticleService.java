package kr.swmaestro.hsb.service;

import java.util.List;

import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

	@Autowired
	protected KeyValueListCache cache;
	
	public List<Article> findArticlesByWriterId(Long writerId, Long afterArticleId, int count) {
		List<Article> articleList = cache.list("user:" + writerId + ":articles", afterArticleId, count, Article.class);
		
		// 캐시에서 불러온 글 목록 수가 예상보다 적을때...
		if (articleList.size() < count) {
			System.out.println("Cached ArticleList Size:" + articleList.size());
			
			Long addAfterArticleId = null;
			if (articleList.size() != 0) {
				addAfterArticleId = articleList.get(articleList.size() - 1).getId();
			}
			
			if (addAfterArticleId == null || addAfterArticleId > 1) { // 마지막 글 id가 1보다 클때만 가져옴. 그 이하는 의미없음.
				List<Article> addArticleList = Article.findArticlesByWriterId(writerId, addAfterArticleId, count - articleList.size());
				articleList.addAll(addArticleList);
				
				// 불러온 목록도 캐시에 넣어줍시다.
			}
		}
		
		return articleList;
	}
	
	public void saveArticle(Article article) {
		// RDBMS에 저장
		article.persist();
		// 캐시에 저장
		String articleKey = "article:" + article.getId();
		
		cache.set(articleKey, article);
		cache.addIndex("user:" + article.getWriterId() + ":articles", article.getId(), articleKey);
		// follower 목록을 가져와 그들에게도 넣어줘야함.
	};
	
}
