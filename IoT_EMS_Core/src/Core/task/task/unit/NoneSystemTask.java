package Core.task.task.unit;

import java.util.Date;
import java.util.Timer;

public class NoneSystemTask extends Task {
	
	private String name;
	private String description;
	
	public Timer scheduler = null;
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	public NoneSystemTask() {
		this.setPriority( TaskPriority.NONE_SYSTEM );	
		if( scheduler == null ) {
			scheduler = new Timer();
		}
	}
	
	//--------------------------------------------------------------------------------------------------------
	// get / set
	//--------------------------------------------------------------------------------------------------------
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
		Thread.currentThread().setPriority( Thread.MIN_PRIORITY );
		
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
		synchronized( TaskQueue.tasks_queue_1 ) {
			TaskQueue.tasks_queue_1.add( this.getIteam() );
		}
	}
	
	public void TaskRun(){
		// need to overwrite it
	}
	
	
}
