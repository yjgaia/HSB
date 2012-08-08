package kr.swmaestro.hsb.data;

import kr.swmaestro.hsb.domain.DomainModel;

import org.springframework.stereotype.Component;

@Component
public class DataCenter {

	public void save(DomainModel domainModel) {
		// 캐시에 저장하는 프로세스 작성
		// TODO
		
		// 백업 쓰레드를 실행
		new DataBackupThread(domainModel).run();
	}

}
