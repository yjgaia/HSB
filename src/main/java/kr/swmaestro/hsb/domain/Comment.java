package kr.swmaestro.hsb.domain;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Comment {
	
	@Id
	@NotNull
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "articleId", nullable = false)
	private Article article;
	
	@NotEmpty
	@Size(max = 500)
	private String content;
	
	@NotEmpty
	@Size(max = 20)
	private String writerUsername;
	
	@NotNull
	private Date writeDate;
	
}
