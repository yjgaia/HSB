package kr.swmaestro.hsb.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * key-value 혹은 key-list 캐시
 * 여기서는 Redis를 사용 (Jedis)
 * 
 * 데이터 형태는 json을 사용합니다. (Gson)
 * 
 * @author 심영재
 */
public interface KeyValueListCache {
	
	public void set(String key, Object object);
	public Set<String> getSetByKey(String key);
	public void addSetElement(String key,String targetKey);
	public void removeSetElement(String key,String targetKey);
	public void removeOrderedSetElement(String key,String targetKey);
	public void addIndex(String key, Long score, String targetKey);
	public void removeIndex(String key, String targetKey);
	public Set<String> getIndexes(String key);
	public <T> List<T> list(String key, Long beforeScore, int count, Class<T> classOfT, Map<String, Integer> emptyValueIndexMap);
	public <T> List<T> getCachedList(Set<String> keySet,Class<T> classOfT, Map<String, Integer> emptyValueIndexMap);
	public <T> Object get(String key, Class<T> classOfT);
	public void delete(String key);
	
}
