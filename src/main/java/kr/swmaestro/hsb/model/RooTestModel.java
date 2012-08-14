package kr.swmaestro.hsb.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias("result")
@RooJavaBean
@RooToString
public class RooTestModel extends ResultModel {
	
	private String msg;
	
}
