package kr.swmaestro.hsb.model;

import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author 심영재
 */
@RooJavaBean
@RooToString
public class Result {
	
	/*
	서비스 레이어로 옮김
	
	// json과 xml로 반환하지 않는다.
	@JsonIgnore // JSON으로 출력하지 않음
	@XStreamOmitField // XML로 출력하지 않음
	@Autowired
	protected KeyValueListCache cache;
	*/

	// 불필요한 정보를 구지 보내주는건 옳은 일이 아니다.
	//private String url;
	
	private boolean success = false; // 부정적인것이 더 좋다.
	private Set<ErrorInfo> errors;
	
	// 불필요한 정보를 구지 보내주는건 옳은 일이 아니다.
	//@XStreamConverter(DateToLongConverter.class)
	//private Date returnDate;
	
	// 데이터가 1개인지 여부
	private boolean single;
	
	private Object data;
	private List list;
	
}
