package Core.nativing.northbound;

import java.net.ServerSocket;
import java.net.Socket;

import Core.main.Main;
import Core.task.task.unit.SystemTask;

public class NorthboundServer extends SystemTask {
	private int socket_port = 12345;
	private int MAX_CONNECTION_COUNT = 20;
	
	public static Integer current_connection;
	
	public NorthboundServer() {
		current_connection = 0;
	}
	
	public void TaskRun() {	
		ServerSocket serverSocket = null;
		try {
			// 監聽PORT
			serverSocket = new ServerSocket( socket_port );
			while( true ) {
				// 等待連線進入(None-blocking)
				Socket socket = serverSocket.accept();
				
				// 連線已確認接上
				Main.log_handler.writeSystemLog("NorthboundServer received a new message...");
				
				// 檢查目前最大連線數是否已達上限
				while( current_connection >= MAX_CONNECTION_COUNT ) {
					Thread.sleep(1000);
				}
				synchronized( current_connection ) {
					current_connection++;
				}
				
				// Fork另外一個Thread, 去處理該連線的工作
				NorthboundActionThread thread = new NorthboundActionThread( socket );
				Main.task_manager.addSystemTaskToScheduler(thread);
			}
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( "NorthboundServer - Exception : " + e.toString() );
		} finally {
			if( serverSocket != null ) {
				try {
					serverSocket.close();
				} catch( Exception e ) { }
			}
		}
	}
}
