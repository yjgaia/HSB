package kr.swmaestro.hsb.data;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * key-value 혹은 key-list 캐시
 * 여기서는 Redis를 사용 (Jedis)
 * 
 * 데이터 형태는 json을 사용합니다. (Gson)
 * 
 * @author 심영재
 */
@Component
public class KeyValueListCache {
	
	@Autowired
	private Jedis jedis;
	
	private final static int COMMON_EXPIRE_SECOND = 5 * 60; // 5분
	
	public void set(String key, Object object) {
		
		ObjectMapper om = new ObjectMapper();
		try {
			// 필요없는 property 제외
			om.getSerializationConfig().addMixInAnnotations(object.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
			
			// 깔끔하게 캐시에 저장!!
			//System.out.println(om.writeValueAsString(object));
			// 저장하는 순간 expire 시간 재생성
			jedis.setex(key, COMMON_EXPIRE_SECOND, om.writeValueAsString(object));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addIndex(String key, Long score, String targetKey) {
		jedis.zadd(key, score, targetKey);
	}
	
	public void getIndex(String key, int start, int end) {
		
		//class java.util.LinkedHashSet 이기 때문에 순서대로 출력된다.
		//System.out.println(jedis.zrange(key, start, end).getClass());
		
		for (String value : jedis.zrange(key, start, end)) {
			System.out.println(value);
		}
	}
	
	public <T> Object get(String key, Class<T> classOfT) {
		ObjectMapper om = new ObjectMapper();
		try {
			// 읽어오는 순간 expire 시간 재생성
			jedis.expire(key, COMMON_EXPIRE_SECOND);
			return om.readValue(jedis.get(key), classOfT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
