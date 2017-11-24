package Core.task.task.unit;

import java.util.TimerTask;

public class Task extends TimerTask {
	private TaskQueueItem iteam;
	private	int id;
	private	int priority;
	private	long time;
	private	int status;
	
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	public static final int STATE_WAIT = 0;
	public static final int STATE_RUN = 1;
	public static final int STATE_TERMINATE = 2;
	public static final int STATE_FINISHED = 3;
	
	public Task() {
		this.status = STATE_WAIT;
	}
	
	//--------------------------------------------------------------------------------------------------------
	// get / set
	//--------------------------------------------------------------------------------------------------------
	public TaskQueueItem getIteam() {
		return iteam;
	}

	public void setIteam(TaskQueueItem iteam) {
		this.iteam = iteam;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	//--------------------------------------------------------------------------------------------------------
	// run function
	//--------------------------------------------------------------------------------------------------------
	// 一定要 覆寫run() [ 因為繼承了 TimerTask ]
	public void run() {
		TaskRun();
	}
	
	public void TaskRun() {
		// need to overwrite it
		// 使用 [繼承 Task]時   要覆寫 此 TaskRun()
	}
}
