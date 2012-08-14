package kr.swmaestro.hsb.data;


/**
 * 데이터 백업 기능을 수행하는 쓰레드
 * 
 * @author 심영재
 */
public class DataBackupThread extends Thread {

	private DataModel dataModel;

	public DataBackupThread(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	@Override
	public void run() {
		// RDBMS 데이터베이스에 없으면 생성하고, 있으면 업데이트
		//dataModel.merge();
		
		System.out.println("!!데이터베이스 저장 성공!");
	}

}
