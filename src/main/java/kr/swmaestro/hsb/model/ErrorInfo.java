package kr.swmaestro.hsb.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@RooJavaBean
@RooToString
@XmlRootElement
@XStreamAlias("error")
public class ErrorInfo {
	
	private String code;
	private Set<Object> arguments;
	private String defaultMessage;

}
