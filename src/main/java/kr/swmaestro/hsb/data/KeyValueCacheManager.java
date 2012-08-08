package kr.swmaestro.hsb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.couchbase.client.CouchbaseClient;

@Component
public class KeyValueCacheManager {
	
	@Autowired
	private CouchbaseClient client;
	
	public Object get(String key) {
		if (key == null) {
			return null;
		}
		return client.get(key);
	}
	
	public void set(String key, Object o) {
		client.set(key, Integer.MAX_VALUE, o);
	}

}
