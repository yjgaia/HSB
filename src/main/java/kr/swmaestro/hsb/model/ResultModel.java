package kr.swmaestro.hsb.model;

import java.util.Date;
import java.util.Set;

import kr.swmaestro.hsb.data.KeyValueListCache;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

@RooJavaBean
public abstract class ResultModel {
	
	// json과 xml로 반환하지 않는다.
	@JsonIgnore
	@XStreamOmitField
	@Autowired
	protected KeyValueListCache cache;

	private String url;
	private boolean success = true;
	private Set<ErrorInfo> errors;
	private Date returnDate;
	private String secureKey;
	
}
