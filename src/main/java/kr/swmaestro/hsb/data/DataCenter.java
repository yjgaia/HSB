package kr.swmaestro.hsb.data;


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
	public static void list(Long afterId, int count) {

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
