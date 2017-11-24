package Core.task;

import java.util.Timer;
import java.util.Vector;

import Core.task.task.unit.NonePeriodicSystemTask;
import Core.task.task.unit.NoneSystemTask;
import Core.task.task.unit.PeriodicSystemTask;
import Core.task.task.unit.SystemTask;
import Core.task.task.unit.TaskQueue;
import Core.task.task.unit.TaskQueueItem;

public class TaskManager {
	//private Timer scheduler[] = null;
	
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	public TaskManager() {
		System.out.println("TaskManager init ...");
		TaskQueue.initialTaskQueue();
		// Init scheduler & task_queue
		/*
		scheduler = new Timer[TaskQueue.SCHEDULER_SIZE];
		for(int i = 0 ; i < TaskQueue.SCHEDULER_SIZE ; i++ ){
			scheduler[i] = new Timer();
		}
		*/
		System.out.println("TaskManager init finished!");
	}
	
	
	//--------------------------------------------------------------------------------------------------------
	// add function (4種) 1.系統   2.非系統   3.週期   4.非週期
	//--------------------------------------------------------------------------------------------------------
	public int addSystemTaskToScheduler( SystemTask task ) {
		//System.out.println("add system task to queue...");
		int ret = 0;
		synchronized( TaskQueue.tasks_queue_0 ) {
			// Queue 滿了嗎??[號碼牌抽完了]
			if( TaskQueue.tasks_queue_0.size() <= 0 ) {
				ret = -1;
				
			} else {
				// 抽一個號碼[重後面開始抽]
				TaskQueueItem obj = TaskQueue.tasks_queue_0.remove( TaskQueue.tasks_queue_0.size() - 1 ); 
				task.setId( obj.getTaskid() );
				task.setIteam( obj );
				// 排工作  延遲0秒(馬上執行)
				//scheduler[TaskQueue.SYSTEM_SCHEDULER_INDEX].schedule( task , 0 );
				task.scheduler.schedule( task , 0 );
			}
		}
		return ret;
	}
	
	public int addNoneSystemTaskToScheduler( NoneSystemTask task ) {
		int ret = 0;
		synchronized( TaskQueue.tasks_queue_1 ) {
			// Queue 滿了嗎??
			if( TaskQueue.tasks_queue_1.size() <= 0 ) {
				ret = -1;
			} else {
				// 抽一個號碼
				TaskQueueItem obj = TaskQueue.tasks_queue_1.remove( TaskQueue.tasks_queue_1.size() - 1 ); 
				task.setId( obj.getTaskid() );
				task.setIteam( obj );
				// 排工作  延遲0秒(馬上執行)
				//scheduler[TaskQueue.NONE_SYSTEM_SCHEDULER_INDEX].schedule( task , 0 );
				task.scheduler.schedule( task , 0 );
			}
		}
		return ret;
	}
	
	public int addPeriodicSystemTaskToScheduler( PeriodicSystemTask task ) {
		int ret = 0;
		synchronized( TaskQueue.tasks_queue_2 ){
			// Queue 滿了嗎??
			if( TaskQueue.tasks_queue_2.size() <= 0 ) {
				ret = -1;
			} else {
				// 抽一個號碼
				TaskQueueItem obj = TaskQueue.tasks_queue_2.remove( TaskQueue.tasks_queue_2.size() - 1 );
				task.setId( obj.getTaskid() );
				task.setIteam( obj );
				// 週期工作 (延遲0秒,每??秒重複  //---還需調整---//task.getPeriodTime() )
				//scheduler[TaskQueue.PERDIC_SCHEDULER_INDEX].schedule( task , task.getStartTime() , task.getPeriodTime() );
				//System.out.println( "Delay : " + task.getStartTime() + ", Period : " + task.getPeriodTime() );
				task.scheduler.schedule( task , task.getStartTime() , task.getPeriodTime() );
			}	
		}
		return ret;
	}
	
	public int addNonePeriodicSystemTaskToScheduler( NonePeriodicSystemTask task ){
		int ret = 0;
		synchronized( TaskQueue.tasks_queue_3 ){
			if( TaskQueue.tasks_queue_3.size() <= 0 ) {
				ret = -1;
			} else {
				TaskQueueItem obj = TaskQueue.tasks_queue_3.remove( TaskQueue.tasks_queue_3.size() - 1 ); 
				task.setId( obj.getTaskid() );
				task.setIteam( obj );
				//scheduler[TaskQueue.NONE_PERDIC_SCHEDULER_INDEX].schedule( task , 0 );
				task.scheduler.schedule( task , 0 );
			}
		}
		return ret;
	}
}
