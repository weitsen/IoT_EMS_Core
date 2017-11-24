package Core.task.task.unit;

public class TaskQueueItem {
	public static int STATE_UNUSE = 0;
	public static int STATE_USED = 1;
	public int taskid;
	public int state;
	
	
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	
	public TaskQueueItem( int taskid , int state ) {
		this.taskid = taskid;
		this.state = state;
	}
	
	
	//--------------------------------------------------------------------------------------------------------
	// get / set
	//--------------------------------------------------------------------------------------------------------
	
	public int getTaskid() {
		return taskid;
	}

	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	
}
