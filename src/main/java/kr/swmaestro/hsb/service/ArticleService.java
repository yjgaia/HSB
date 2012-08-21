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
	
	public List<Article> findArticlesByWriterId(Long writerId, int start, int end) {
		return cache.list("user:" + writerId + ":articles", start, end, Article.class);
	}
	
	public void saveArticle(Article article) {
		// RDBMS에 저장
		article.persist();
		// 캐시에 저장
		String articleKey = "article:" + article.getId();
		
		cache.set(articleKey, this);
		cache.addIndex("user:" + article.getWriterId() + ":articles", article.getId(), articleKey);
		// follower 목록을 가져와 그들에게도 넣어줘야함.
	};
	
}
