package kr.swmaestro.hsb.data;

import org.springframework.beans.factory.annotation.Autowired;

import com.couchbase.client.CouchbaseClient;

/**
 * key-value만을 담당하는 캐시
 * 여기서는 Couchbase를 사용
 * 
 * @author 심영재
 */
public class KeyValueCache {
	
	@Autowired
	private CouchbaseClient client;

}
