package Core.log;

import Core.main.Main;

public class LogManager {

	private LogAgen log_agent_task;
	
	public LogManager() {
		if( Main.task_manager != null ) {
			System.out.println( "LogManager init ...." );
			log_agent_task = new LogAgen( 1000 );
			// �⦹�u�@log_agent_task(LogAgen)���Ƶ{��
			Main.task_manager.addSystemTaskToScheduler( log_agent_task );
			System.out.println( "LogManager init finished!" );
		} else {
			System.out.println( "Main.task_manager is null!" );
		}
	}
	
	public void writeSystemLog( String message ) {
		
		synchronized( LogQueue.log_queue_0 ){
			
			// �p�G  queue�e�q  �W�L�w�]���j�p(200) �N�n���g�J�ɮ�  
			if( LogQueue.log_queue_0.size() == LogQueue.MAX_LOG_QUEUE_SIZE[0] ) {
				log_agent_task.FlushSystemLogToFile();
			}
			
			// ��T���ܦ� LogMessage ������
			LogMessage temp = new LogMessage( LogMessage.LOG_SYSTEM , message );
			
			//System.out.println( temp );
			
			// �⦹���� ���  LOG_SYSTEM (log_queue_0) ��queue
			LogQueue.log_queue_0.add( temp ); 
						
		}
	}

	public void writeAccessLog( String message ) {
		
		synchronized( LogQueue.log_queue_1 ){
			
			if( LogQueue.log_queue_1.size() == LogQueue.MAX_LOG_QUEUE_SIZE[1] ){
				log_agent_task.FlushAccessLogToFile();
			}
			
			// ��T���ܦ� LogMessage ������
			LogMessage temp = new LogMessage( LogMessage.LOG_ACCESS , message );
					
			// �⦹���� ���  LOG_SYSTEM (log_queue_1) ��queue
			LogQueue.log_queue_1.add( temp ); 	
		}
	}
	
	public void writeDatabaseLog( String message ) {
		
		synchronized( LogQueue.log_queue_2 ){
			
			if( LogQueue.log_queue_2.size() == LogQueue.MAX_LOG_QUEUE_SIZE[2] ){
				log_agent_task.FlushDatabaseLogToFile();
			}
			
			LogMessage temp = new LogMessage( LogMessage.LOG_DB , message );
			
			LogQueue.log_queue_2.add( temp );
		}
	}
	
	public void writeAdapterLog( String message ) {
		
		synchronized ( LogQueue.log_queue_3 ){
			
			if( LogQueue.log_queue_3.size() == LogQueue.MAX_LOG_QUEUE_SIZE[3] ){
				log_agent_task.FlushAdapterLogToFile();
			}
			
			LogMessage temp = new LogMessage( LogMessage.LOG_ADAPTER , message );
			
			//System.out.println( temp );
			
			LogQueue.log_queue_3.add( temp );
		}
	}
	
	public void writeDebugLog( String message ) {
		synchronized ( LogQueue.log_queue_4 ){
			
			if( LogQueue.log_queue_4.size() == LogQueue.MAX_LOG_QUEUE_SIZE[4] ){
				log_agent_task.FlushDebugLogToFile();
			}
			
			LogMessage temp = new LogMessage( LogMessage.LOG_DEBUG , message );
			
			LogQueue.log_queue_4.add( temp );
		}
	}
}
