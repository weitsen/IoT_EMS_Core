package Core.task.task.unit;

import java.util.Date;
import java.util.Timer;

public class NonePeriodicSystemTask extends SystemTask {
	private String startTime;
	
	public Timer scheduler = null;
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	public NonePeriodicSystemTask(){
		if( scheduler == null ) {
			scheduler = new Timer();
		}
	}
		
	//--------------------------------------------------------------------------------------------------------
	// get / set
	//--------------------------------------------------------------------------------------------------------
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	//--------------------------------------------------------------------------------------------------------
	// run function
	//--------------------------------------------------------------------------------------------------------
	// Overwirte run method
	public void run() {
		
		// 01 �O���}�l���檺�ɶ�
		Date time_ = new Date();
		this.setTime( time_.getTime() );
		
		// 02 �]�w���{�����u���v
		Thread.currentThread().setPriority( Thread.NORM_PRIORITY );
		
		// 03 �]�w���A_run
		this.setStatus( Task.STATE_RUN );
		
		try {
			TaskRun();
			this.setStatus( Task.STATE_FINISHED );
		} catch( Exception e ) {
			this.setStatus( Task.STATE_TERMINATE );
			e.printStackTrace();
		}
		// 04 �����  �k�ٳo�Ӹ��X�P
		synchronized( TaskQueue.tasks_queue_3 ) {
			TaskQueue.tasks_queue_3.add( this.getIteam() );
		}
	}
	
	public void TaskRun(){
		// need to overwrite it
	}
}
