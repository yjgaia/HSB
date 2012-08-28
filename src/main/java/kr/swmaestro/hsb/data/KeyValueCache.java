package kr.swmaestro.hsb.data;

import java.io.IOException;

import net.spy.memcached.CASValue;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.couchbase.client.CouchbaseClient;

/**
 * key-value만을 담당하는 캐시
 * 여기서는 Couchbase를 사용
 * 
 * @author 심영재
 */
@Component
public class KeyValueCache {
	
	@Autowired
	private CouchbaseClient client;
	
	private final static int COMMON_EXPIRE_SECOND = 60*5; // 테스트용 5분
	//private final static int COMMON_EXPIRE_SECOND = 30 * 60; // request 세션과 같이 일반적으로 30분 유지
	
	public void set(String key, Object object) {
		
		ObjectMapper om = new ObjectMapper();
		try {
			// 필요없는 property 제외
			om.getSerializationConfig().addMixInAnnotations(object.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
			
			// 깔끔하게 캐시에 저장!!
			//System.out.println(om.writeValueAsString(object));
			// 저장하는 순간 expire 시간 재생성
			client.set(key, COMMON_EXPIRE_SECOND, om.writeValueAsString(object));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> Object get(String key, Class<T> classOfT) {
		if (key == null) {
			return null;
		}
		
		CASValue<?> v = client.getAndTouch(key, COMMON_EXPIRE_SECOND);
		if (v == null) {
			return null;
		}
		
		ObjectMapper om = new ObjectMapper();
		try {
			return om.readValue(v.getValue().toString(), classOfT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void del(String key) {
		client.delete(key);
	}

}
