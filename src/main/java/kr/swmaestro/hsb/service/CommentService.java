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
import kr.swmaestro.hsb.model.Comment;
import kr.swmaestro.hsb.util.JsonXmlUtil;

import org.codehaus.jackson.map.ObjectMapper;
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
		
		//Article의 댓글 count 올려줌
		Article targetArticle=Article.findArticle(comment.getTargetArticleId());
		targetArticle.addcommentCount(1);
		targetArticle.merge();
		
		// 캐시에 저장
		String commentKey=cacheComment(comment);
		cache.addIndex(getCommentListKey(comment.getTargetArticleId()), comment.getId(), commentKey);
	}
	
	public List<String> getCommentXmlList(Long articleId) {
		return JsonXmlUtil.jsonListToXmlList(getCommentJsonList(articleId));
	}

	public List<String> getCommentJsonList(Long articleId) {
		String commentListKey=getCommentListKey(articleId);
		Set<String> commentSet=cache.getIndexes(commentListKey);
		Map<String, Integer> emptyValueIndexMap = new HashMap<>();
		List<String> cachedCommentJsonList = cache.getCachedList(commentSet, emptyValueIndexMap);
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
				
				ObjectMapper om = new ObjectMapper();
				try {
					// 필요없는 property 제외
					om.getSerializationConfig().addMixInAnnotations(comment.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
					// 그리고 원래 글 목록에 삽입.
					cachedCommentJsonList.set(emptyValueIndexMap.get(key), om.writeValueAsString(comment));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//캐싱된 값과 followingCount가 다름 
		if(targetArticle.getCommentCount()>cachedCommentJsonList.size()){
			System.out.println("댓글 캐싱 된것 없음");
			List<Comment> commentListInDB= Comment.getCommentListById(targetArticle.getId());
			List<String> commentJsonListInDB = new ArrayList<>();
			for(Comment comment: commentListInDB){
				String commentKey=cacheComment(comment);
				cache.addIndex(getCommentListKey(comment.getTargetArticleId()), comment.getId(), commentKey);
				
				ObjectMapper om = new ObjectMapper();
				try {
					// 필요없는 property 제외
					om.getSerializationConfig().addMixInAnnotations(comment.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
					// 그리고 원래 글 목록에 삽입.
					commentJsonListInDB.add(om.writeValueAsString(comment));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
					
			return commentJsonListInDB;
		}
		
		return cachedCommentJsonList;
	}

	public void deleteComment(Comment comment) {
		// RDBMS에서 제거
		comment.delete();
				
		// 캐시에서 제거
		String key = getCommentKey(comment.getId());
		cache.delete(key);
		cache.removeIndex(getCommentListKey(comment.getTargetArticleId()), key);
	}
}
