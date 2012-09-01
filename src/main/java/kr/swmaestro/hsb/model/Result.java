package kr.swmaestro.hsb.model;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author 심영재
 */
@XStreamAlias("result")
@RooJavaBean
@RooToString
public class Result {
	
	private boolean success = false; // 부정적인것이 더 좋다.
	private Set<ErrorInfo> errors;
	
	// 데이터가 1개인지 여부
	private boolean single;
	
	private String data;
	private List<String> list;
	
}
