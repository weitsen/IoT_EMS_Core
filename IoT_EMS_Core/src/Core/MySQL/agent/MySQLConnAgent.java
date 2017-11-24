package Core.MySQL.agent;

import java.util.Vector;

import Core.MySQL.MySQLConn;
import Core.main.Main;

public class MySQLConnAgent extends Thread {
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Private members
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Agent 更新週期(300s)
	private long POLLING_TIME_INTAVAL = 300*1000;
	
	private int MAX_CONNECTION = 0;
	
	// Thread kernel
	private Thread mysql_agent_thread = null;
	
	private Vector<MySQLConn> conn_pool = null;
	
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Public members
	// -------------------------------------------------------------------------------------------------------------------------- //
	public MySQLConnAgent( int MAX_CON ) {
		if( conn_pool == null ) {				
			conn_pool = new Vector<MySQLConn>();
			Main.log_handler.writeSystemLog( "MAX_CONNECTION : " + MAX_CONNECTION );
		}
		MAX_CONNECTION = MAX_CON;
		Main.log_handler.writeSystemLog( "MySQLConnAgent 啟動完成!" );
	}
	
	public void run() {		
		// 開始監控全部Session
		try {			
			while( true ) {

				synchronized( conn_pool ) {
					// 更新連線狀態(針對未使用的連線)
					for( int i = 0 ; i < conn_pool.size() ; i++ ) {
						MySQLConn temp = conn_pool.get(i);
						// 重新連線
						temp.reInitConnection();
					}
				}
				
				Thread.sleep(POLLING_TIME_INTAVAL);
			}
		} catch( InterruptedException e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( "MySQLConnAgent Agent was interrupted..." );
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( "MySQLConnAgent Agent Exception : " + e.toString() );
		}
	}
	
	// 啟動Agent Thread
	public void EnableAgentKernel() {
		Main.log_handler.writeSystemLog( "MySQLConnAgent Kernel enable..." );
		if( mysql_agent_thread == null ) {
			mysql_agent_thread = this;
			mysql_agent_thread.setPriority( Thread.MAX_PRIORITY );
			mysql_agent_thread.start();
		}
	}
	
	// 關閉Agent Thread
	public void DisableAgentKernel() {
		Main.log_handler.writeSystemLog( "MySQLConnAgent Kernel disable..." );
		if( mysql_agent_thread != null ) {
			if( mysql_agent_thread.isAlive() ) {
				mysql_agent_thread.interrupt();
			}
			mysql_agent_thread = null;
		}
	}
	
	public int getMaxSessionSize() {
		return conn_pool.size();
	}
	
	public void addNewConnection( MySQLConn buff ) {
		Main.log_handler.writeSystemLog( "MySQLConnAgent conn_pool.size() : " + conn_pool.size() );
		synchronized( conn_pool ) {
			if( conn_pool.size() < MAX_CONNECTION ) {
				conn_pool.add(buff);
			}
		}
	}
	
	public MySQLConn getConnection( int i ) {
		synchronized( conn_pool ) {
			MySQLConn temp = conn_pool.get(i);
			return temp;
		}
	}
}
