package Core.task.task.unit;

import java.util.Date;
import java.util.Timer;

public class SystemTask extends Task {
	private String name;
	private String description;
	
	public Timer scheduler = null;
	
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	public SystemTask() {
		// �]�w�u���v
		this.setPriority( TaskPriority.SYSTEM_TASK );
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
	// �e�m�B�z
	public void run() {
		// 01 �O���}�l���檺�ɶ�
		Date time_ = new Date();
		this.setTime( time_.getTime() );
		//System.out.println( "time: " + this.getTime() );
		
		// =============================================================
		// �ɥR
		// Thread.setPriority(int)�i�H�]�wThread���u���v,�Ʀr�V�j�u���v�V��
		// Thread �w�q�F3�Ӭ�����static final variable
		// public static final int MAX_PRIORITY 10
		// public static final int MIN_PRIORITY 1
		// public static final int NORM_PRIORITY 5 
		// =============================================================
		
		// 02 �]�w���{�����u���v
		Thread.currentThread().setPriority( Thread.MAX_PRIORITY );
		
		// 03 �]�w���A_run
		this.setStatus( Task.STATE_RUN );
		try {
			TaskRun();
			// �]�w���A_���� 
			this.setStatus( Task.STATE_FINISHED );
		} catch( Exception e ) {
			// �]�w���A_���_
			this.setStatus( Task.STATE_TERMINATE );
			e.printStackTrace();
		}
		
		// 04 �����  �k�ٳo�Ӹ��X�P
		synchronized( TaskQueue.tasks_queue_0 ) {
			TaskQueue.tasks_queue_0.add( this.getIteam() );
		}
	}
	
	public void TaskRun() {
		// need to overwrite it
	}
	
}
