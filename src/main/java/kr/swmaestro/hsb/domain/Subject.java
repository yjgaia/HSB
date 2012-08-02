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

@RooJavaBean
@RooToString
@RooEntity
public class Subject {

	@NotEmpty
	@Size(max = 300)
	@Column(nullable = false, length = 300)
	private String name;

	@ManyToOne
	@JoinColumn(name = "creatorUsername", nullable = false)
	private UserInfo creatorUser;

	@Column(nullable = false)
	private Date createDate;

	private int memberCount;

	private int articleCount;

}
