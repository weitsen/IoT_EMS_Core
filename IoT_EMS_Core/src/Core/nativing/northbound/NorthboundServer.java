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
			// ��ťPORT
			serverSocket = new ServerSocket( socket_port );
			while( true ) {
				// ���ݳs�u�i�J(None-blocking)
				Socket socket = serverSocket.accept();
				
				// �s�u�w�T�{���W
				Main.log_handler.writeSystemLog("NorthboundServer received a new message...");
				
				// �ˬd�ثe�̤j�s�u�ƬO�_�w�F�W��
				while( current_connection >= MAX_CONNECTION_COUNT ) {
					Thread.sleep(1000);
				}
				synchronized( current_connection ) {
					current_connection++;
				}
				
				// Fork�t�~�@��Thread, �h�B�z�ӳs�u���u�@
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
