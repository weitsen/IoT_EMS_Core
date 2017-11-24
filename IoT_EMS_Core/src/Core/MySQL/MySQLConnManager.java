package Core.MySQL;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import Core.MySQL.agent.MySQLConnAgent;
import Core.main.Main;
import Core.util.Configuration;

public class MySQLConnManager {
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Private members
	// -------------------------------------------------------------------------------------------------------------------------- //
	private String SQL_SERVER_IP = "127.0.0.1";
	private String SQL_SERVER_ACCOUNT = "root";
	private String SQL_SERVER_PWD = "1qaz@WSX";
	private int SQL_SERVER_PORT = 3306;
	private String DATA_BASE_NAME = "iot";
	private int MAX_CONNECTION = 20;
	private MySQLConnAgent agnet = null;
	
	private void loadConnConfig() {
		try {
			String config_path = ".." + File.separator + "conf" + File.separator + "System.ini";
			String path = URLDecoder.decode( MySQLConn.class.getClassLoader().getResource("").getPath() , "UTF-8" );
			if( new File( path + config_path ).exists() ) {
				Configuration cf = new Configuration( path + config_path );
				SQL_SERVER_IP = cf.getPropertyAsString("MYSQL_SERVER_IP", SQL_SERVER_IP );
				SQL_SERVER_ACCOUNT = cf.getPropertyAsString("SQL_SERVER_ACCOUNT", SQL_SERVER_ACCOUNT );
				SQL_SERVER_PWD = cf.getPropertyAsString("SQL_SERVER_PWD", SQL_SERVER_PWD );
				SQL_SERVER_PORT = cf.getPropertyAsInt("SQL_SERVER_PORT", SQL_SERVER_PORT );
				DATA_BASE_NAME = cf.getPropertyAsString("DATA_BASE_NAME", DATA_BASE_NAME );
				MAX_CONNECTION = cf.getPropertyAsInt("MAX_CONNECTION", MAX_CONNECTION );
			} else {
				Main.log_handler.writeSystemLog("file not found! : " + MySQLConn.class.getProtectionDomain().getCodeSource().getLocation().getPath() );
			}
			
			Main.log_handler.writeSystemLog( "MySQL.loadConnConfig() : SQL_SERVER_IP - " +  SQL_SERVER_IP );
			Main.log_handler.writeSystemLog( "MySQL.loadConnConfig() : SQL_SERVER_ACCOUNT - " +  SQL_SERVER_ACCOUNT );
			Main.log_handler.writeSystemLog( "MySQL.loadConnConfig() : SQL_SERVER_PWD - " +  SQL_SERVER_PWD );
			Main.log_handler.writeSystemLog( "MySQL.loadConnConfig() : SQL_SERVER_PORT - " +  SQL_SERVER_PORT );
			Main.log_handler.writeSystemLog( "MySQL.loadConnConfig() : DATA_BASE_NAME - " +  DATA_BASE_NAME );
			Main.log_handler.writeSystemLog( "MySQL.loadConnConfig() : MAX_CONNECTION - " +  MAX_CONNECTION );
		} catch( IOException e ) {
			 e.printStackTrace();
			 Main.log_handler.writeSystemLog( "MySQL.loadConnConfig():" + e.toString() );
		}
	}
	
	public MySQLConnManager() {
		loadConnConfig();
		agnet = new MySQLConnAgent( MAX_CONNECTION );
		for( int i = 0 ; i < MAX_CONNECTION ; i++ ) {
			MySQLConn buf = new MySQLConn( SQL_SERVER_IP , SQL_SERVER_ACCOUNT , SQL_SERVER_PWD , DATA_BASE_NAME , SQL_SERVER_PORT );
			buf.setConnection_state( MySQLConn.CONNECTION_UNUSED );
			agnet.addNewConnection( buf );
		}
		agnet.EnableAgentKernel();
	}
	
	public MySQLConn getConnection() {
		synchronized( agnet ) {
			for( int i = 0 ; i < MAX_CONNECTION ; i++ ) {
				MySQLConn temp = agnet.getConnection(i);
				if( temp.getState() == MySQLConn.CONNECTION_UNUSED ) {
					temp.setConnection_state( MySQLConn.CONNECTION_USED );
					return temp;
				}
			}
		}
		return null;
	}
	
	public void releaseConnection( MySQLConn con ) {
		synchronized( agnet ) {
			con.setConnection_state(  MySQLConn.CONNECTION_UNUSED );
		}
	}
}
