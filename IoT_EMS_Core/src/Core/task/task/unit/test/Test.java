package Core.task.task.unit.test;

import Core.log.LogQueue;
import Core.task.TaskManager;
import Core.task.task.unit.TaskQueue;

public class Test {
	
	public static void main( String argv[] ) {
		
		TaskQueue.initialTaskQueue();
		
		
		Test myTest = new Test();
		
		
		for( int i = 0 ; i < 2 ; i++ ){
			
			//myTest.addST();
			//myTest.addNST();
			myTest.addPST();
			//myTest.addNPST();
			
			try {
				Thread.sleep(10);
			} catch( Exception e ) {
				e.printStackTrace();
			}
			
			System.out.println( "addPST i =  : "+ i + " size : " + TaskQueue.tasks_queue_2.size() );
		}
		System.out.println( "addPST size (before) : " + TaskQueue.tasks_queue_2.size() );
		
		//myTest.addST();
		//System.out.println( "addST size (before) : " + TaskQueue.tasks_queue_0.size() );
		
		//myTest.addNST();
		//myTest.addPST();
		//myTest.addNPST();
		
		try {
			Thread.sleep( 10000 );
			System.out.println( "addST size (after) : " + TaskQueue.tasks_queue_0.size() );
			
		} catch( Exception e ) {
			e.printStackTrace();
		}
		
	}
	
	public void addST(){
		
		TaskManager work = new TaskManager();
		
		Test_SystemTask_1 STwork = new Test_SystemTask_1();
		
		int ret_full = work.addSystemTaskToScheduler(STwork);
		
		//System.out.println( "addST ret_full value : " + ret_full );
		
		
	}
	
	public void addNST(){
		TaskManager work_1 = new TaskManager();
		
		Test_NoneSystemTask NSTwork = new Test_NoneSystemTask();
		
		int ret_full = work_1.addNoneSystemTaskToScheduler( NSTwork );
		
		
	}
	
	public void addPST(){
		TaskManager work_2 = new TaskManager();
		
		Test_PeriodicSystemTask PSTwork = new Test_PeriodicSystemTask();
		
		int ret_full = work_2.addPeriodicSystemTaskToScheduler( PSTwork );
		
	}
	
	public void addNPST(){
		TaskManager work_3 = new TaskManager();
		
		Test_NonePeriodicSystemTask NPSTwork = new Test_NonePeriodicSystemTask();
		
		int ret_full = work_3.addNonePeriodicSystemTaskToScheduler( NPSTwork );
		
	}
	
	
}
