package kr.swmaestro.hsb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Article {
	
	@Id
	@NotNull
	private Long id;
	
	@NotEmpty
	@Size(max = 500)
	private String title;
	
	@NotEmpty
	@Size(max = 3000)
	private String content;
	
	@NotEmpty
	@Size(max = 20)
	private String writerUsername;
	
	@NotNull
	private Date writeDate;
	
	@OneToMany(mappedBy = "article", cascade = { CascadeType.ALL }, orphanRemoval = true)
	private List<Comment> commentList = new ArrayList<Comment>();
	
	/**
	 * 댓글 생성
	 * 
	 * @param writerUsername
	 * @param content
	 */
	public void addComment(String writerUsername, String content) {
		Comment comment = new Comment();
		comment.setArticle(this);
		comment.setContent(content);
		comment.setWriterUsername(writerUsername);
		comment.setWriteDate(new Date());
		this.commentList.add(comment);
	}
	
}
