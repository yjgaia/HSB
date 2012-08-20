package kr.swmaestro.hsb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
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
public class Article extends ResultModel {
	
	@NotNull
	private Long targetUserId;
	
	@NotNull
	private String targetUserUsername;
	
	@NotNull
	private String targetUserNickname;

	@NotEmpty
	@Size(max = 3000)
	@Column(length = 3000, nullable = false)
	private String content;

	@NotNull
	private String writerId;
	
	@NotNull
	private Long writerUsername;
	
	@NotNull
	private String writerNickname;

	@Column(nullable = false)
	private Date writeDate;
	
	private int commentCount;
	
	public void save() {
		// RDBMS에 저장
		persist();
		// 캐시에 저장
		cache.set("article:" + getId(), this);
	};

}
