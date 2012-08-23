package kr.swmaestro.hsb.model;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
/**
 * @author 이한솔  
 */
@RooJavaBean
@RooToString
@RooEntity
public class Follower extends SecureKeyModel{


	private Long userId;
	
	private Long followerId;
	
	@Column(nullable = false)
	private Date followDate;
	
	public static boolean isFollowing(Long user_id, Long follower_id) {
		return entityManager().createQuery("SELECT COUNT(f) FROM Follower f WHERE user_id = :user_id AND follower_id=:follower_id", Long.class).setParameter("user_id", user_id).setParameter("follower_id", follower_id).getSingleResult() > 0l;
	}
	public static Follower findFollowInfoByFollower(Long user_id,Long follower_id){
		return entityManager().createQuery("SELECT f FROM Follower f WHERE user_id = :user_id AND follower_id=:follower_id", Follower.class).setParameter("user_id", user_id).setParameter("follower_id", follower_id).getSingleResult();
	}

}

