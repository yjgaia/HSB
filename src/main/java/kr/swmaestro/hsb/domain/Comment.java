package kr.swmaestro.hsb.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Comment {

	@ManyToOne
	@JoinColumn(name = "articleId", nullable = false)
	private Article article;

	@NotEmpty
	@Size(max = 500)
	@Column(length = 500, nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "writerUsername", nullable = false)
	private UserInfo writerUser;

	@Column(nullable = false)
	private Date writeDate;

}
