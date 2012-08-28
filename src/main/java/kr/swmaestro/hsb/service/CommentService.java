package kr.swmaestro.hsb.service;

import kr.swmaestro.hsb.data.KeyValueListCache;
import kr.swmaestro.hsb.model.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
	@Autowired
	private KeyValueListCache cache;
	
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
		
		// 캐시에 저장
		String commentKey=cacheComment(comment);
		cache.addIndex(getCommentListKey(comment.getTargetArticleId()), comment.getId(), commentKey);
	}
}
