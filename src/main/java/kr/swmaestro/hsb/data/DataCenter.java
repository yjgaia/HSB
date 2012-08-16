package kr.swmaestro.hsb.data;

import java.util.List;


public class DataCenter {

	// instance 불가
	private DataCenter() {};

	// 하나의 객체를 가져옴
	public static DataModel get(Class<?> clazz, Long id) {
		if (clazz == DataModel.class) {
			try {
				return ((DataModel) clazz.newInstance()).get(id);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 목록화
	public static List<?> list(Class<?> clazz, Long afterId, int count) {
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
	public static void save(DataModel dataModel) {
		dataModel.merge();
	}

	// 삭제 (말이 삭제지 숨기는것)
	public static void delete(DataModel dataModel) {
		dataModel.setEnable(false);
		save(dataModel);
	}

}
