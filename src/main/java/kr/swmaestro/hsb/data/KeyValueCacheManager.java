package kr.swmaestro.hsb.data;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.couchbase.client.CouchbaseClient;

@Component
public class KeyValueCacheManager {
	
	private final static int EXP = 30 * 60; // 30분

	@Autowired
	private CouchbaseClient client;

	public Object get(String key) {
		if (key == null) {
			return null;
		}
		return client.get(key);
	}

	public String put(Object o) {
		String uid = UUID.randomUUID().toString();
		client.set(uid, EXP, o);
		return uid;
	}

	public void put(String key, Object o) {
		client.set(key, EXP, o);
	}
	
	public void remove(String key) {
		client.delete(key);
	}

}
