package Core.MySQL.agent;

import java.util.Vector;

import Core.MySQL.MySQLConn;
import Core.main.Main;

public class MySQLConnAgent extends Thread {
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Private members
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Agent ��s�g��(300s)
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
		Main.log_handler.writeSystemLog( "MySQLConnAgent �Ұʧ���!" );
	}
	
	public void run() {		
		// �}�l�ʱ�����Session
		try {			
			while( true ) {

				synchronized( conn_pool ) {
					// ��s�s�u���A(�w�良�ϥΪ��s�u)
					for( int i = 0 ; i < conn_pool.size() ; i++ ) {
						MySQLConn temp = conn_pool.get(i);
						// ���s�s�u
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
	
	// �Ұ�Agent Thread
	public void EnableAgentKernel() {
		Main.log_handler.writeSystemLog( "MySQLConnAgent Kernel enable..." );
		if( mysql_agent_thread == null ) {
			mysql_agent_thread = this;
			mysql_agent_thread.setPriority( Thread.MAX_PRIORITY );
			mysql_agent_thread.start();
		}
	}
	
	// ����Agent Thread
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
