package kr.swmaestro.hsb.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author 심영재
 */
@XStreamAlias("secureKeyModel")
@RooJavaBean
@RooToString
public class SecureKeyModel {
	
	// json과 xml로 반환하지 않는다.
	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	private String secureKey;

}
