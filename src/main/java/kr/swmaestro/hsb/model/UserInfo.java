package kr.swmaestro.hsb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import kr.swmaestro.hsb.DateToLongConverter;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author 심영재
 */
@RooJavaBean
@RooToString
@RooEntity
@XmlRootElement
@XStreamAlias("result")
public class UserInfo extends SecureKeyModel {

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

	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	@NotEmpty(message = "비밀번호 확인을 입력해주세요.")
	@Size(min = 4, max = 20, message = "비밀번호 확인은 {2}글자 이상, {1}글자 이하로 입력해주세요.")
	@Transient
	// 비밀번호 확인은 저장하지 않음
	private String passwordConfirm;

	@NotEmpty(message = "닉네임을 입력해주세요.")
	@Size(min = 4, max = 20, message = "닉네임은 {2}글자 이상, {1}글자 이하로 입력해주세요.")
	@Column(length = 20, unique = true)
	private String nickname;
	
	private int followerCount;
	public void increaseFollowerCount(){
		followerCount++;
	}
	
	private int followingCount;
	public void increaseFollowingCount(){
		followingCount++;
	}

	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	@NotEmpty(message = "이메일을 입력해주세요.")
	@Size(max = 320, message = "이메일은 {1}글자 이하로 입력해주세요.")
	@Email(message = "이메일은 이메일 형식에 맞추어 주세요.")
	@Column(length = 320, nullable = false)
	private String email;

	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	@XStreamConverter(DateToLongConverter.class)
	@Column(nullable = false)
	private Date joinDate;

	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	private int loginCount;
	
	public void increaseLoginCount() {
		loginCount++;
	};
	
	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	private Date lastLoginDate;

	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	private int writeCount;
	
	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	private Date lastWriteDate;
	
	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	private boolean enable;
	
	public static UserInfo findUserInfoByUsername(String username) {
		return entityManager().createQuery("SELECT o FROM UserInfo o WHERE username = :username", UserInfo.class).setParameter("username", username).getSingleResult();
	}

	public static boolean existsUser(String username) {
		return entityManager().createQuery("SELECT COUNT(o) FROM UserInfo o WHERE username = :username", Long.class).setParameter("username", username).getSingleResult() > 0l;
	}
	
	public static boolean existsNickname(String nickname) {
		return entityManager().createQuery("SELECT COUNT(o) FROM UserInfo o WHERE nickname = :nickname", Long.class).setParameter("nickname", nickname).getSingleResult() > 0l;
    }

	public static boolean isFollowing(Long user_id, Long follower_id) {
		return entityManager().createQuery("SELECT COUNT(f) FROM Follower f WHERE user_id = :user_id AND follower_id=:follower_id", Long.class).setParameter("user_id", user_id).setParameter("follower_id", follower_id).getSingleResult() > 0l;
	}
	
}
