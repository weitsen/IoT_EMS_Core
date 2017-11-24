package Core.task.task.unit.test;

import java.util.Date;

import Core.task.task.unit.SystemTask;

public class Test_SystemTask_1 extends SystemTask {
	
	public void TaskRun() {
		
		try {
			Thread.sleep(1000);
		} catch( Exception e ) {
			e.printStackTrace();
		}
		
		Date date = new Date();
		System.out.println("SystemTask now time : " + date );
		
	}
	
}
