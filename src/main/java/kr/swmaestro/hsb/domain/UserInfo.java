package kr.swmaestro.hsb.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author 심영재
 */
@RooJavaBean
@RooToString
@RooEntity
public class UserInfo implements UserDetails, DomainModel {
	private static final long serialVersionUID = 1L;

	@NotEmpty
	@Size(min = 4, max = 20)
	@Pattern(regexp = "[_a-z0-9-]*")
	@Column(length = 20, unique = true)
	private String username;

	@NotEmpty
	@Size(min = 4, max = 20)
	@Column(length = 40)
	// 암호화 하면 암호의 길이 증가
	private String password;

	@NotEmpty
	@Size(min = 4, max = 20)
	@Transient
	// 비밀번호 확인은 저장하지 않음
	private String passwordConfirm;

	@NotEmpty
	@Size(min = 4, max = 20)
	@Column(length = 20, unique = true)
	private String nickname;

	@NotEmpty
	@Size(max = 320)
	@Email
	@Column(length = 320)
	private String email;

	@Column(nullable = false)
	private Date joinDate;

	private int loginCount;
	
	public void increaseLoginCount() {
		loginCount++;
	};

	private int writeCount;
	
	// 소셜 유저를 위한 설정
	private boolean socialUser = false;
	private String socialDisplayName;
	private String socialImageUrl;
	private String socialProfileUrl;
	private String socialProviderId;
	private String socialProviderUserId;

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		HashSet<GrantedAuthority> hs = new HashSet<GrantedAuthority>();
		if (isSocialUser()) {
			hs.add(new GrantedAuthority() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getAuthority() {
					return "ROLE_SOCIAL";
				}
			});
		}
		if (username != null && username.equals("admin")) {
			hs.add(new GrantedAuthority() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public String getAuthority() {
					return "ROLE_ADMIN";
				}
			});
		}
		hs.add(new GrantedAuthority() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return "ROLE_USER";
			}
		});
		return hs;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public static UserInfo findUserInfoByUsername(String username) {
		return entityManager().createQuery("SELECT o FROM UserInfo o WHERE username = :username", UserInfo.class).setParameter("username", username).getSingleResult();
	}

	public static boolean existsUser(String username) {
		return entityManager().createQuery("SELECT COUNT(o) FROM UserInfo o WHERE username = :username", Long.class).setParameter("username", username).getSingleResult() > 0l;
	}
	
	public static boolean existsNickname(String nickname) {
		return entityManager().createQuery("SELECT COUNT(o) FROM UserInfo o WHERE nickname = :nickname", Long.class).setParameter("nickname", nickname).getSingleResult() > 0l;
    }

}
