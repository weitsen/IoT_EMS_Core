package Core.main;

import Core.MySQL.MySQLConnManager;
import Core.adapter.devices.MyTestDevice.MyTestDevice;
import Core.adapter.manager.AdapterManager;
import Core.cassandra.CassandraConnManager;
import Core.error.ErrorTable;
import Core.log.LogManager;
import Core.nativing.northbound.NorthboundServer;
import Core.task.TaskManager;

public class Main {
	public static String system_version = "0.1";
	
	public static LogManager log_handler = null;
	public static TaskManager task_manager = null;
	public static ErrorTable etable = null;
	public static AdapterManager adapter_manager = null;
	public static CassandraConnManager cassandra_manager = null;
	public static MySQLConnManager mysql_manager = null;
	public static NorthboundServer native_north_bound = null;
	
	public Main() {
		// plan to do
	}
	
	public void init_all_manager() {
		etable = new ErrorTable();
		// 此 init 有順序  [1] task_manager [2] log_handler ( task_manager 一定要先 )
		task_manager = new TaskManager();
		log_handler = new LogManager();
		log_handler.writeSystemLog("System(Ver " + system_version + ") init...");
		cassandra_manager = new CassandraConnManager();
		mysql_manager = new MySQLConnManager();
		adapter_manager = new AdapterManager();
		
		// 載入DB中所記錄的設備
		adapter_manager.loadAllDeviceInfoFromDB();
		
		// 啟動Native北向介面
		log_handler.writeSystemLog("Start native northbound server...");
		native_north_bound = new NorthboundServer();
		task_manager.addSystemTaskToScheduler( native_north_bound );
	}
	
	public void run() {
		init_all_manager();
		log_handler.writeSystemLog("Start watchdog agent...");
		
		/*
		// ========================================================== //
		// test
		{
			MyTestDevice device_test = new MyTestDevice( "192.168.1.1" , "Use to demo device..." , "test" , "test123" );
			// 加入納管
			adapter_manager.addDevice( device_test );
			System.out.println("device(" + device_test.getDevice_ip() + ") : " + device_test.getNode_id() );
		}
		{
			MyTestDevice device_test = new MyTestDevice( "192.168.1.2" , "Use to demo device..." , "test" , "test123" );
			// 加入納管
			adapter_manager.addDevice( device_test );
			System.out.println("device(" + device_test.getDevice_ip() + ") : " + device_test.getNode_id() );
		}
		*/
		/*
		// 移除納管
		try {
			Thread.sleep(10000);
			adapter_manager.remodeDevice("192.168.1.1");
		} catch( Exception e ) {
			e.printStackTrace();
		}
		*/
		// ========================================================== //
		
		while( true ) {
			log_handler.writeSystemLog("This is watch dog...");
			try {
				Thread.sleep(1000);
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main( String argv[] ) {
		Main obj = new Main();
		obj.run();
	}
}
