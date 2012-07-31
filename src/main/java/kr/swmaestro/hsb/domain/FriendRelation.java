package kr.swmaestro.hsb.domain;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class FriendRelation {
	
	@Id
	@NotNull
	private Long id;
	
	@NotEmpty
	@Size(max = 20)
	private String username1;
	
	@NotEmpty
	@Size(max = 20)
	private String username2;
	
	@NotNull
	private Date relateDate;
	
}
