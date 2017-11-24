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
		// 設定優先權
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
	// 前置處理
	public void run() {
		// 01 記錄開始執行的時間
		Date time_ = new Date();
		this.setTime( time_.getTime() );
		//System.out.println( "time: " + this.getTime() );
		
		// =============================================================
		// 補充
		// Thread.setPriority(int)可以設定Thread的優先權,數字越大優先權越高
		// Thread 定義了3個相關的static final variable
		// public static final int MAX_PRIORITY 10
		// public static final int MIN_PRIORITY 1
		// public static final int NORM_PRIORITY 5 
		// =============================================================
		
		// 02 設定此程式的優先權
		Thread.currentThread().setPriority( Thread.MAX_PRIORITY );
		
		// 03 設定狀態_run
		this.setStatus( Task.STATE_RUN );
		try {
			TaskRun();
			// 設定狀態_完成 
			this.setStatus( Task.STATE_FINISHED );
		} catch( Exception e ) {
			// 設定狀態_中斷
			this.setStatus( Task.STATE_TERMINATE );
			e.printStackTrace();
		}
		
		// 04 執行後  歸還這個號碼牌
		synchronized( TaskQueue.tasks_queue_0 ) {
			TaskQueue.tasks_queue_0.add( this.getIteam() );
		}
	}
	
	public void TaskRun() {
		// need to overwrite it
	}
	
}
