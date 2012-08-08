package kr.swmaestro.hsb.data;


import org.springframework.stereotype.Component;

@Component
public class DataCenter {

	public void save(DataModel dataModel) {
		// 캐시에 저장하는 프로세스 작성
		// TODO
		
		// 백업 쓰레드를 실행
		new DataBackupThread(dataModel).run();
	}

}
