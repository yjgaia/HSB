package kr.swmaestro.hsb.data;

import net.spy.memcached.CASValue;

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
	
	private final static int COMMON_EXPIRE_SECOND = 5 * 60; // 5분
	
	public void set(String key, Object object) {
		client.set(key, COMMON_EXPIRE_SECOND, object);
	}
	
	public Object get(String key) {
		if (key == null) {
			return null;
		}
		CASValue<?> v = client.getAndTouch(key, COMMON_EXPIRE_SECOND);
		if (v == null) {
			return null;
		}
		return v.getValue();
	}

}
