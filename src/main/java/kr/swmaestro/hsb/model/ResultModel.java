package kr.swmaestro.hsb.model;

import java.util.Date;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public abstract class ResultModel {

	private String url;
	private boolean success = true;
	private Set<ErrorInfo> errors;
	private Date returnDate;
	private String secureKey;
	
}
