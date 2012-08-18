package kr.swmaestro.hsb.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

@Deprecated
@Component
public class DataCenter {
	
	@Autowired
	private Jedis jedis;

	// instance 불가
	private DataCenter() {};

	// 하나의 객체를 가져옴
	public DataModel get(Class<?> clazz, Long id) {
		if (clazz == DataModel.class) {
			//jedis.get(key);
			try {
				return ((DataModel) clazz.newInstance()).get(id);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 목록화
	public List<?> list(Class<?> clazz, Long afterId, int count) {
		if (clazz == DataModel.class) {
			try {
				return ((DataModel) clazz.newInstance()).list(afterId, count);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 저장
	public void save(DataModel dataModel) {
		dataModel.merge();
		jedis.set(dataModel.createCacheKey(), new Gson().toJson(dataModel));
	}

	// 삭제 (말이 삭제지 숨기는것)
	public void delete(DataModel dataModel) {
		dataModel.setEnable(false);
		save(dataModel);
	}

}
