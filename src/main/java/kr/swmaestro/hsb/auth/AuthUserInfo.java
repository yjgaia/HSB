package kr.swmaestro.hsb.auth;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import kr.swmaestro.hsb.model.SecureKeyModel;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("authUserInfo")
@RooJavaBean
@RooToString
public class AuthUserInfo extends SecureKeyModel {
	
	@NotEmpty(message = "아이디를 입력해주세요.")
	@Size(min = 4, max = 20, message = "아이디는 4글자 이상, 20글자 이하로 입력해주세요.")
	@Pattern(regexp = "[_a-z0-9-]*", message = "아이디는 영어와 숫자로 입력해주세요.")
	@Column(length = 20, unique = true)
	private String username;
	
	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	@NotEmpty(message = "비밀번호를 입력해 주세요.")
	@Size(min = 4, max = 20, message = "비밀번호는 {2}글자 이상, {1}글자 이하로 입력해주세요.")
	@Column(length = 40)
	// 암호화 하면 암호의 길이 증가
	private String password;
	
	private String generatedSecureKey;

}
