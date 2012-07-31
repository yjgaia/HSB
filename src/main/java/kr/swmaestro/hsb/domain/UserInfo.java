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
public class UserInfo {
	
	@Id
	@NotEmpty
	@Size(max = 20)
	private String username;
	
	@NotEmpty
	@Size(max = 20)
	private String password;
	
	@NotEmpty
	@Size(max = 20)
	private String nickname;
	
	@NotEmpty
	@Size(max = 320)
	private String email;
	
	@NotNull
	private Date joinDate;
	
}
