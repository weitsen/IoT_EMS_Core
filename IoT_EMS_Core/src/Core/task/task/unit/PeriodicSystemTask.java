package Core.task.task.unit;

import java.util.Date;
import java.util.Timer;

public class PeriodicSystemTask extends SystemTask {
	private long startTime;
	private long periodTime;
	
	public Timer scheduler = null;
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	public PeriodicSystemTask(){
		startTime = 0;
		periodTime = 0;
		if( scheduler == null ) {
			scheduler = new Timer();
		}
	}
	
	//--------------------------------------------------------------------------------------------------------
	// get / set
	//--------------------------------------------------------------------------------------------------------
	public long getStartTime() {
		return startTime;
	}
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getPeriodTime() {
		return periodTime;
	}
	
	public void setPeriodTime(long periodTime) {
		this.periodTime = periodTime;
	}
	
	//--------------------------------------------------------------------------------------------------------
	// run function
	//--------------------------------------------------------------------------------------------------------
	// Overwirte run method
	public void run() {
		
		// 01 記錄開始執行的時間
		Date time_ = new Date();
		this.setTime( time_.getTime() );
		
		// 02 設定此程式的優先權
		Thread.currentThread().setPriority( Thread.NORM_PRIORITY );
		
		// 03 設定狀態_run
		this.setStatus( Task.STATE_RUN );
		try {
			TaskRun();
			this.setStatus( Task.STATE_FINISHED );
		} catch( Exception e ) {
			this.setStatus( Task.STATE_TERMINATE );
			e.printStackTrace();
		}
		
		// 04 執行後  歸還這個號碼牌
		synchronized( TaskQueue.tasks_queue_2 ) {
			TaskQueue.tasks_queue_2.add( this.getIteam() );
		}
		
	}
	
	public void TaskRun() {
		// need to overwrite it
	}
}
