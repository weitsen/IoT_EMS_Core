package Core.task.task.unit;

import java.util.Vector;

public class TaskQueue {
	public final static int SCHEDULER_SIZE = 4;
	public final static int SYSTEM_SCHEDULER_INDEX = 0;
	public final static int NONE_SYSTEM_SCHEDULER_INDEX = 1;
	public final static int PERDIC_SCHEDULER_INDEX = 2;
	public final static int NONE_PERDIC_SCHEDULER_INDEX = 3;
	
	public static final Vector<TaskQueueItem> tasks_queue_0 = new Vector();  // SYSTEM_SCHEDULER
	public static final Vector<TaskQueueItem> tasks_queue_1 = new Vector();	 // NONE_SYSTEM_SCHEDULER
	public static final Vector<TaskQueueItem> tasks_queue_2 = new Vector();	 // PERDIC_SCHEDULER
	public static final Vector<TaskQueueItem> tasks_queue_3 = new Vector();	 // NONE_PERDIC_SCHEDULER
	
	public static final int MAX_TASK_QUEUE_SIZE[] = { 200 , 200 , 200 , 200 };
	
	public static void initialTaskQueue() {
		for( int i = 0 ; i < SCHEDULER_SIZE ; i++ ) {
			for( int j = 0 ; j < MAX_TASK_QUEUE_SIZE[i] ; j++ ) {
				TaskQueueItem obj = new TaskQueueItem( j , TaskQueueItem.STATE_UNUSE );
				switch( i ) {
				case SYSTEM_SCHEDULER_INDEX :
					tasks_queue_0.add( obj );
					break;
				case NONE_SYSTEM_SCHEDULER_INDEX :
					tasks_queue_1.add( obj );
					break;
				case PERDIC_SCHEDULER_INDEX :
					tasks_queue_2.add( obj );
					break;
				case NONE_PERDIC_SCHEDULER_INDEX :
					tasks_queue_3.add( obj );
					break;
				}
			}
		}
	}
}
