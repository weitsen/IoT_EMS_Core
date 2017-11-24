package Core.cassandra.agent;

import java.util.Vector;
import Core.cassandra.CassandraConn;
import Core.cassandra.CassandraSession;
import Core.main.Main;

public class CassandraConnAgent extends Thread {
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Private members
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Agent 更新週期(300s)
	private long POLLING_TIME_INTAVAL = 300*1000;
	
	private int MAX_CONNECTION = 0;
	
	// Thread kernel
	private Thread cassandra_agent_thread = null;
	
	private Vector<CassandraSession> conn_pool = null;
	
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Public members
	// -------------------------------------------------------------------------------------------------------------------------- //
	public CassandraConnAgent() {
		
		if( conn_pool == null ) {				
			conn_pool = new Vector<CassandraSession>();
			MAX_CONNECTION = CassandraConn.getInstance().getMaxConnectionNum();
			Main.log_handler.writeSystemLog( "MAX_CONNECTION : " + MAX_CONNECTION );
		}
		
		Main.log_handler.writeSystemLog( "Cassandra Connection Agent!" );
	}
	
	public void run() {		
		// 開始監控全部Session
		try {			
			while( true ) {

				synchronized( conn_pool ) {
					// 更新連線狀態(針對未使用的連線)
					for( int i = 0 ; i < conn_pool.size() ; i++ ) {
						CassandraSession temp = conn_pool.get(i);
						if( temp.getState() == CassandraSession.CONNECTION_UNUSED ) {
							// 重新連線
							temp.getSession().init();
						}
					}
				}
				
				Thread.sleep(POLLING_TIME_INTAVAL);
			}
		} catch( InterruptedException e ) {
			e.printStackTrace();
			
			Main.log_handler.writeSystemLog( "CassandraConnAgent Agent was interrupted..." );
		} catch( Exception e ) {
			e.printStackTrace();
		
			Main.log_handler.writeDebugLog( "CassandraConnAgent Agent Exception : " + e.toString() );
		}
	}
	
	// 啟動Agent Thread
	public void EnableAgentKernel() {
		Main.log_handler.writeSystemLog( "CassandraConnAgent Kernel enable..." );
		if( cassandra_agent_thread == null ) {
			cassandra_agent_thread = this;
			cassandra_agent_thread.setPriority( Thread.MAX_PRIORITY );
			cassandra_agent_thread.start();
		}
	}
	
	// 關閉Agent Thread
	public void DisableAgentKernel() {
		Main.log_handler.writeSystemLog( "CassandraConnAgent Kernel disable..." );
		if( cassandra_agent_thread != null ) {
			if( cassandra_agent_thread.isAlive() ) {
				cassandra_agent_thread.interrupt();
			}
			cassandra_agent_thread = null;
		}
	}
	
	public int getCurrentSessionSize() {
		return conn_pool.size();
	}
	
	public int getMaxSessionSize() {
		return MAX_CONNECTION;
	}
	
	public CassandraSession addNewSession() {
		CassandraSession buff = null;
		
		Main.log_handler.writeSystemLog( "CassandraConnAgent conn_pool.size() : " + conn_pool.size() );
		
		synchronized( conn_pool ) {
			if( conn_pool.size() < MAX_CONNECTION ) {
				buff = new CassandraSession( CassandraConn.getInstance().getSession() , CassandraSession.CONNECTION_UNUSED );
				conn_pool.add(buff);
			}
			
			return buff;
		}
	}
	
	public CassandraSession getSession( int i ) {
		
		synchronized( conn_pool ) {
			CassandraSession temp = conn_pool.get(i);
			return temp;
		}
	}
}
