package kr.swmaestro.hsb.data;

import kr.swmaestro.hsb.domain.DomainModel;

/**
 * 데이터 백업 기능을 수행하는 쓰레드
 * 
 * @author 심영재
 */
public class DataBackupThread extends Thread {

	private DomainModel domainModel;

	public DataBackupThread(DomainModel domainModel) {
		this.domainModel = domainModel;
	}

	@Override
	public void run() {
		// RDBMS 데이터베이스에 없으면 생성하고, 있으면 업데이트
		domainModel.merge();
	}

}
