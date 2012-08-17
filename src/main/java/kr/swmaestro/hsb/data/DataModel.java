package kr.swmaestro.hsb.data;

import java.util.List;

public interface DataModel {
	
	public String createCacheKey();
	
	public DataModel get(Long id);
	
	// RDBMS 데이터베이스에 없으면 생성하고, 있으면 업데이트
	public Object merge();
	
	public void setEnable(boolean enable);
	
	public boolean isEnable();
	
	public List<?> list(Long afterId, int count);

}
