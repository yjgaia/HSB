package kr.swmaestro.hsb.data;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

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
	
	public void set(String key, Object object) {
		
		ObjectMapper om = new ObjectMapper();
		try {
			// 필요없는 property 제외
			om.getSerializationConfig().addMixInAnnotations(object.getClass(), JsonIgnoreResultModelPropertyesMixIn.class);
			
			// 깔끔하게 캐시에 저장!!
			//System.out.println(om.writeValueAsString(object));
			jedis.set(key, om.writeValueAsString(object));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> Object get(String key, Class<T> classOfT) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.readValue(jedis.get(key), classOfT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
