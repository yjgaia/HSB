package kr.swmaestro.hsb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.Article;
import kr.swmaestro.hsb.model.Comment;
import kr.swmaestro.hsb.model.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
	@Autowired
	private KeyValueListCache cache;
	
	@Autowired
	private ArticleService articleService;
	
	public String getCommentKey(Long id){
		return "comment:"+id;
	}
	
	// 캐시에 저장
	private String cacheComment(Comment comment) {
		String commentKey = getCommentKey(comment.getId());
		cache.set(commentKey, comment);
		return commentKey;
	
	}
	
	public String getCommentListKey(Long targetArticleId){
		return "article:"+targetArticleId+":comments";
	}

	public void saveComment(Comment comment) {
		// RDBMS에 저장
		comment.persist();
		
		//Article의 댓글 count 올려줌
		Article targetArticle=Article.findArticle(comment.getTargetArticleId());
		targetArticle.addcommentCount(1);
		targetArticle.merge();
		
		// 캐시에 저장
		String commentKey=cacheComment(comment);
		cache.addIndex(getCommentListKey(comment.getTargetArticleId()), comment.getId(), commentKey);
	}

	public List<Comment> getCommentList(Long articleId) {
		String commentListKey=getCommentListKey(articleId);
		Set<String> commentSet=cache.getIndexes(commentListKey);
		Map<String, Integer> emptyValueIndexMap = new HashMap<>();
		List<Comment> cachedCommentList=cache.getCachedList(commentSet,Comment.class, emptyValueIndexMap);
		Article targetArticle =Article.findArticle(articleId);
		if (emptyValueIndexMap.size() > 0) {
			
			List<Long> commentList = new ArrayList<>();
			for (String key : emptyValueIndexMap.keySet()) {
			// comment:{id}
				Long id = Long.parseLong(key.substring(8));
				commentList.add(id);
			}
			
			List<Comment> addUserInfoList = Comment.findCommentByIds(commentList);
			// 가져온 값들도 캐시에 넣어줍니다.
			for (Comment comment : addUserInfoList) {
				String key = cacheComment(comment);
				// 그리고 원래 글 목록에 삽입.
				cachedCommentList.set(emptyValueIndexMap.get(key), comment);
			}
		}
		
		//캐싱된 값과 followingCount가 다름 
		if(targetArticle.getCommentCount()>cachedCommentList.size()){
			System.out.println("댓글 캐싱 된것 없음");
			List<Comment> commentListInDB= Comment.getCommentListById(targetArticle.getId());
			for(Comment comment: commentListInDB){
				String commentKey=cacheComment(comment);
				cache.addIndex(getCommentListKey(comment.getTargetArticleId()), comment.getId(), commentKey);
			}
					
			return commentListInDB;
		}
		
		return cachedCommentList;
	}
}
