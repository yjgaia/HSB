package kr.swmaestro.hsb.data;

import java.util.Date;
import java.util.Set;

import kr.swmaestro.hsb.model.ErrorInfo;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * result model에서 캐싱할 필요가 없는 자료들은 json에서 제외한다.
 * 
 * @author 심영재
 */
public abstract class JsonIgnoreResultModelPropertyesMixIn {

	@JsonIgnore abstract String getUrl();
	@JsonIgnore abstract boolean isSuccess();
	@JsonIgnore abstract Set<ErrorInfo> getErrors();
	@JsonIgnore abstract Date getReturnDate();
	@JsonIgnore abstract String getSecureKey();
	
}
