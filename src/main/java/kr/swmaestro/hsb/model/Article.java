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

	private Long writerId;
	
	private String writerUsername;
	
	private String writerNickname;

	@Column(nullable = false)
	private Date writeDate;
	
	private int commentCount;
	
	public static List<Article> findArticlesByWriterId(Long writerId, Long afterArticleId, int count) {
		
		String query = "SELECT o FROM Article o WHERE 1=1";
		
		if (writerId != null)		query += " AND o.writerId = :writerId";
		if (afterArticleId != null)	query += " AND o.id < :afterArticleId";
		
		query += " ORDER BY o.id DESC";
		TypedQuery<Article> q = entityManager().createQuery(query, Article.class);
		
		if (writerId != null)		q.setParameter("writerId", writerId);
		if (afterArticleId != null)	q.setParameter("afterArticleId", afterArticleId);
		
		return q.setMaxResults(count).getResultList();
    }

}
