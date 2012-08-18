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
@Deprecated
@RooJavaBean
@RooToString
//@RooEntity
public class FriendRelation /*implements DataModel*/ {
	
	@ManyToOne
	@JoinColumn(name = "user1Id", nullable = false)
	private UserInfo user1;

	@ManyToOne
	@JoinColumn(name = "user2Id", nullable = false)
	private UserInfo user2;

	@Column(nullable = false)
	private Date relateDate;
	
}
