package kr.swmaestro.hsb.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * @author 심영재
 */
@RooJavaBean
@RooToString
@RooEntity
public class Article extends SecureKeyModel {

	@NotEmpty
	@Size(max = 3000)
	@Column(length = 3000, nullable = false)
	private String content;

	@Column(nullable = false)
	private Long writerId;
	
	@Column(nullable = false)
	private String writerUsername;
	
	@Column(nullable = false)
	private String writerNickname;

	@Column(nullable = false)
	private Date writeDate;
	
	private int commentCount;
	
	public static List<Article> findArticlesByWriterId(Long writerId, Long beforeArticleId, int count) {
		
		String query = "SELECT o FROM Article o WHERE 1=1";
		
		if (writerId != null)		query += " AND o.writerId = :writerId";
		if (beforeArticleId != null)	query += " AND o.id < :beforeArticleId";
		
		query += " ORDER BY o.id DESC";
		TypedQuery<Article> q = entityManager().createQuery(query, Article.class);
		
		if (writerId != null)		q.setParameter("writerId", writerId);
		if (beforeArticleId != null)	q.setParameter("beforeArticleId", beforeArticleId);
		
		return q.setMaxResults(count).getResultList();
    }

	public static List<Article> findArticlesByWriterIds(List<Long> writerIds, Long beforeArticleId, int count) {
		
		String query = "SELECT o FROM Article o WHERE (1!=1";
		
		for (Long writerId : writerIds) {
			query += " OR o.writerId = " + writerId;
		}
		
		query += ")";
		
		if (beforeArticleId != null)	query += " AND o.id < :beforeArticleId";
		
		query += " ORDER BY o.id DESC";
		TypedQuery<Article> q = entityManager().createQuery(query, Article.class);
		
		if (beforeArticleId != null)	q.setParameter("beforeArticleId", beforeArticleId);
		
		return q.setMaxResults(count).getResultList();
	}

	public static List<Article> findArticlesByIds(List<Long> ids) {
		
		String query = "SELECT o FROM Article o WHERE 1!=1";
		
		for (Long id : ids) {
			query += " OR o.id = " + id;
		}
		
		query += " ORDER BY o.id DESC";
		TypedQuery<Article> q = entityManager().createQuery(query, Article.class);
		
		return q.getResultList();
	}

}
