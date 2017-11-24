package Core.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Core.main.Main;

public class MySQLConn {
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Private members
	// -------------------------------------------------------------------------------------------------------------------------- //
	private String SQL_SERVER_ACCOUNT = "N/A";
	private String SQL_SERVER_PWD = "N/A";
	private String SQL_SERVER_IP = "N/A";
	private int SQL_PORT = 3306;
	private String DATA_BASE_NAME = "N/A"; 
	private Connection con_session = null;
	
	private int connection_state;

	private void buildConnection() { 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con_session = DriverManager.getConnection( "jdbc:mysql://" + SQL_SERVER_IP + ":" + SQL_PORT + "/" + DATA_BASE_NAME , SQL_SERVER_ACCOUNT ,SQL_SERVER_PWD );
			//con_session.setCatalog( DATA_BASE_NAME );
		} catch( ClassNotFoundException cnf ) {
			cnf.printStackTrace();
			Main.log_handler.writeSystemLog( cnf.toString() );
		} catch( SQLException e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( e.toString() );
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Public members
	// -------------------------------------------------------------------------------------------------------------------------- //
	public static final int CONNECTION_UNUSED = 0;
	public static final int CONNECTION_USED   = 1;
	
	public MySQLConn( String ip , String acc , String pwd , String db_name , int port_num ) {
		SQL_SERVER_IP = ip;
		SQL_SERVER_ACCOUNT = acc;
		SQL_SERVER_PWD = pwd;
		DATA_BASE_NAME = db_name;
		SQL_PORT = port_num;
		buildConnection();
	}
	
	public int getConnection_state() {
		return connection_state;
	}

	public void setConnection_state( int connection_state ) {
		this.connection_state = connection_state;
	}
	
	public Connection getConnection() {
		return con_session;
	}
	
	public void closeConnection() {
		if( con_session != null ) {
			synchronized( con_session ) {
				try {
					if( !con_session.isClosed() ) {
						con_session.close();
					}
				} catch( SQLException e ) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void reInitConnection() {
		if( con_session != null ) {
			synchronized( con_session ) {
				Statement stmt = null;
				try {
					stmt = con_session.createStatement();
					// 嘗試去下SQL語法, 保持與Server的連線不會timeout
					String sql = "SELECT @@VERSION;";
					ResultSet rs = stmt.executeQuery(sql);
					// 如果發現timeout 斷線了, 則重新連線
					if( !rs.next() ){
						con_session.close();
						con_session = DriverManager.getConnection( "jdbc:mysql://" + SQL_SERVER_IP + ":" + SQL_PORT + "/" + DATA_BASE_NAME , SQL_SERVER_ACCOUNT ,SQL_SERVER_PWD );
					}
				} catch( SQLException e ) {
					e.printStackTrace();
					Main.log_handler.writeSystemLog( e.toString() );
				}
			}
		}
	}
	
	public int getState() {
		return connection_state;
	}
}
