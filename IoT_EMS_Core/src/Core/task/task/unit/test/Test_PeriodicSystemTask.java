package Core.task.task.unit.test;

import Core.task.task.unit.PeriodicSystemTask;

public class Test_PeriodicSystemTask extends PeriodicSystemTask {
	
	public void TaskRun() {
		try {
			Thread.sleep(1000);
		} catch( Exception e ) {
			e.printStackTrace();
		}
		System.out.println( "Periodic test ~ " );
	}
}
