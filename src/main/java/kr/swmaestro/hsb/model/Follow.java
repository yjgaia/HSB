package kr.swmaestro.hsb.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import kr.swmaestro.hsb.XmlDateToLongConverter;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.thoughtworks.xstream.annotations.XStreamConverter;
/**
 * @author 이한솔  
 */
@RooJavaBean
@RooToString
@RooEntity
public class Follow extends SecureKeyModel{

	@Column(nullable = false)
	private Long targetUserId;
	
	@Column(nullable = false)
	private String targetUserUsername;
	
	@Column(nullable = false)
	private String targetUserNickname;
	
	@Column(nullable = false)
	private Long followerId;
	
	@Column(nullable = false)
	private String followerUsername;
	
	@Column(nullable = false)
	private String followerNickname;
	
	@XStreamConverter(XmlDateToLongConverter.class)
	@Column(nullable = false)
	private Date followDate;
	
	public static boolean isFollowing(Long targetUserId, Long followerId) {
		return entityManager().createQuery("SELECT COUNT(o) FROM Follow o WHERE o.targetUserId = :targetUserId AND o.followerId = :followerId", Long.class).setParameter("targetUserId", targetUserId).setParameter("followerId", followerId).getSingleResult() > 0l;
	}
	
	public static Follow findFollowInfoByFollower(Long targetUserId, Long followerId){
		return entityManager().createQuery("SELECT o FROM Follow o WHERE o.targetUserId = :targetUserId AND o.followerId = :followerId", Follow.class).setParameter("targetUserId", targetUserId).setParameter("followerId", followerId).getSingleResult();
	}
	
	public static List<Follow> findFollowsByTargetUserId(Long targetUserId) {
		return entityManager().createQuery("SELECT o FROM Follow o WHERE o.targetUserId = :targetUserId", Follow.class).setParameter("targetUserId", targetUserId).getResultList();
	}

}

