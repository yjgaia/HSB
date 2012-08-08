package kr.swmaestro.hsb.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kr.swmaestro.hsb.data.DataModel;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * @author 심영재
 */
@RooJavaBean
@RooToString
@RooEntity
public class FriendRequest implements DataModel {

	@ManyToOne
	@JoinColumn(name = "requestUserId", nullable = false)
	private UserInfo requestUser;

	@ManyToOne
	@JoinColumn(name = "targetUserId", nullable = false)
	private UserInfo targetUser;

	@Column(nullable = false)
	private Date requestDate;

}
