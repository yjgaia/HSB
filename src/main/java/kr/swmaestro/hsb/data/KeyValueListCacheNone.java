package kr.swmaestro.hsb.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * KeyValueListCacheNone
 * KeyValueListCache를 쓰지 않을 때
 * 
 * @author 심영재
 */
public class KeyValueListCacheNone implements KeyValueListCache {
	
	public void set(String key, Object object){}
	public Set<String> getSetByKey(String key){ return new HashSet<>(); }
	public void addSetElement(String key,String targetKey){}
	public void removeSetElement(String key,String targetKey){}
	public void removeOrderedSetElement(String key,String targetKey){}
	public void addIndex(String key, Long score, String targetKey){}
	public void removeIndex(String key, String targetKey){}
	public Set<String> getIndexes(String key){ return new HashSet<>(); }
	public <T> List<T> list(String key, Long beforeScore, int count, Class<T> classOfT, Map<String, Integer> emptyValueIndexMap){ return new ArrayList<>(); }
	public <T> List<T> getCachedList(Set<String> keySet,Class<T> classOfT, Map<String, Integer> emptyValueIndexMap){ return new ArrayList<>(); }
	public <T> Object get(String key, Class<T> classOfT){ return null; }
	public void delete(String key){}

}
