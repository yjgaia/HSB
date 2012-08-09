package kr.swmaestro.hsb.data;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;

@Component
public class RedisCacheManager {
	
	private final static int EXP = 24 * 60 * 60; // 하루
	
	@Autowired
	private Jedis jedis;
	
	public void get() {
		
	}
	
	public void put(String key, Object object) {
		Gson gson = new Gson();
		jedis.set(key, gson.toJson(object));
	}

	public void list(HashMap<String, Object> filter,
			HashMap<String, Object> order, int firstResult, int maxResults) {

	}

}
