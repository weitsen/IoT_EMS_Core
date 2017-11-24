package Core.nativing.northbound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.Vector;

import Core.adapter.device.DeviceInterface;
import Core.adapter.device.DeviceNode;
import Core.data.impl.DeviceManagerImpl;
import Core.data.impl.DeviceReportImpl;
import Core.main.Main;
import Core.task.task.unit.SystemTask;

public class NorthboundActionThread extends SystemTask {
	public Socket connection_p = null;
	
	public NorthboundActionThread( Socket connection ) {
		connection_p = connection;
	}
	
	// ==============================================================================
	// Command 說明 :
	// ==============================================================================
	// 第一種類(納管使用, command type - type1 ) : 
	public int do_type1( String data[] ) {
		int ret = -1;
		
		if( data.length == 7  ) {
			String action = data[1].trim().toLowerCase();
			String ip = data[2].trim();
			String device_type = data[3].trim();
			String account = data[4].trim();
			String password = data[5].trim();
			String description = data[6].trim();
			
			// 加入納管
			// command type :# action :# ip :# device type :# account :# password :# description  
			//   [0]             [1]     [2]        [3]        [4]         [5]          [6]
			if( action.equals("add") ) {
				try {
					Class<?> clazz = Class.forName( "Core.adapter.devices." + device_type + "." + device_type );
					Constructor<?> constructor = clazz.getConstructor(String.class, String.class, String.class, String.class);
					Object instance = constructor.newInstance( ip , description , account , password );
					DeviceNode device = (DeviceNode)instance;
					
					ret = Main.adapter_manager.addDevice( device );
					if( ret != -1 ) {
						ret = 0;
					}
				} catch( Exception e ) {
					ret = -2;
					Main.log_handler.writeSystemLog( "NorthboundActionThread : Managed device(" + ip + ") is fail, because unknown device type!" );
					//System.out.println( "NorthboundActionThread : Managed device(" + ip + ") is fail, because unknown device type!" );
				}					
			}
		} else if( data.length == 3 ) {
			String action = data[1].trim().toLowerCase();
			String ip = data[2].trim();
			// 移除納管
			// command type :# action :# ip 
			//   [0]             [1]     [2]
			if( action.equals("del") ) {
				ret = Main.adapter_manager.remodeDevice( ip );
				if( ret != -1 ) {
					ret = 0;
				}
			}
		}
		return ret;
	}

	// ==============================================================================
	// Command 說明 :
	// ==============================================================================
	// 第二種類(組構抓取, command type - type2 ) :
	public String do_type2( String data[] ) {
		String ret = "";
		
		if( data.length == 2 ) {
			String action = data[1].trim().toLowerCase();
			// 抓取所有納管設備清單
			// command type :# action 
			//   [0]             [1] 
			if( action.equals("get_device_list") ) {
				ret = "list:";
				DeviceManagerImpl db = new DeviceManagerImpl();
				Vector<String> list = db.getAllDeviceIPList();
				for( int i = 0 ; i < list.size() ; i++ ) {
					if( i < list.size() - 1 ) {
						ret += list.get(i) + ":#";
					} else {
						ret += list.get(i);
					}
				}
			}
		} else if( data.length == 3 ) {
			String action = data[1].trim().toLowerCase();
			String ip = data[2].trim();
			// 抓取設備節點詳細資訊
			// command type :# action :# ip 
			//   [0]             [1]     [2]
			if( action.equals("get_device_info") ) {
				ret = "device_info:";
				DeviceNode device = Main.adapter_manager.getDeviceByIpaddr(ip);
				if( device != null ) {
					ret += device.getNode_id() + ":#";
					ret += device.getDevice_ip() + ":#";
					ret += device.getManage_type() + ":#";
					ret += device.getDeivce_account() + ":#";
					ret += device.getDeivce_passwd() + ":#";
					ret += device.getDevice_info();
				}
			// 抓取設備所有介面清單
			// command type :# action :# ip 
			//   [0]             [1]     [2]
			} else if( action.equals("get_device_all_interface_list") ) {
				ret = "device_interface_list:";
				DeviceNode device = Main.adapter_manager.getDeviceByIpaddr(ip);
				Vector<DeviceInterface> list = device.getDeviceInterfaceList();
				for( int i = 0 ; i < list.size() ; i++ ) {
					if( i < list.size() - 1 ) {
						ret += list.get(i).getInterface_id() + ":#";
					} else {
						ret += list.get(i).getInterface_id();
					}
				}
			}
		} else if( data.length == 4 ) {
			String action = data[1].trim().toLowerCase();
			String ip = data[2].trim();
			int interface_id = -1;
			try {
				interface_id = Integer.parseInt( data[3].trim() );
			} catch( Exception e ) { e.printStackTrace(); }
			// 抓取設備介面詳細資訊
			// command type :# action :# ip :# interface id 
			//   [0]             [1]     [2]       [3]
			if( action.equals("get_device_interface_info") ) {
				ret = "deivce_interface_info:";
				DeviceNode device = Main.adapter_manager.getDeviceByIpaddr(ip);
				Vector<DeviceInterface> list = device.getDeviceInterfaceList();
				for( int i = 0 ; i < list.size() ; i++ ) {
					if( list.get(i).getInterface_id() == interface_id ) {
						ret += list.get(i).getNode_id() + ":#";
						ret += list.get(i).getInterface_id() + ":#";
						ret += list.get(i).getInterfaceItem_name() + ":#";
						ret += list.get(i).getInterfaceItem_type() + ":#";
						ret += list.get(i).getInterface_info();
						break;
					}
				}
			}
		}
		
		return ret;
	}
	
	// ==============================================================================
	// Command 說明 :
	// ==============================================================================
	// 第三種類(報表抓取, command type - type3 ) :
	public String do_type3( String data[] ) {
		String ret = "";
		
		if( data.length == 6 ) {
			String action = data[1].trim().toLowerCase();
			int node_id = -1;
			try {
				node_id = Integer.parseInt( data[2].trim() );
			} catch( Exception e ) { e.printStackTrace(); }
			int interface_id = -1;
			try {
				interface_id = Integer.parseInt( data[3].trim() );
			} catch( Exception e ) { e.printStackTrace(); }
			String start_datetime = data[4].trim();
			String end_datetime = data[5].trim();
			
			// 抓取設備介面的報表監控內容(依照範圍時間)
			// command type :# action :# node_id :# interface id :# start_datetime :# end_datetime 
			//   [0]             [1]      [2]           [3]             [4]                [5]       
			if( action.equals("get_device_interface_report") ) {
				ret = "report:";
				DeviceReportImpl db = new DeviceReportImpl();
				Vector<String> buf = db.getTextReportData(interface_id, node_id, start_datetime, end_datetime);
				for( int i = 0 ; i < buf.size() ; i++ ) {
					String temp = buf.get(i);
					if( i < buf.size() -1 ) {
						ret += temp + ":#";
					} else {
						ret += temp;
					}
				}
			}
		}
		return ret;
	}
	
	public void TaskRun() {
		DataInputStream  input = null;
		DataOutputStream output = null;
		try {
			input = new DataInputStream( connection_p.getInputStream() );
			output = new DataOutputStream( connection_p.getOutputStream() );
			
			//System.out.println("My debug-0002");
			// 讀取封包內容
			byte[] b = new byte[2048];
			String data = "";
			int length;
			while( (length = input.read(b) ) > 0 ) {
				data += new String(b, 0, length);
				if( length < 2048 ) {
					break;
				}
			}
			
			//System.out.println("My debug-0003 : " + data );
			// 切割訊息內容
			String sp_data[] = data.split(":#");
			
			// 確認並執行工作
			if( sp_data != null ) {
				// type1 的 command
				if( sp_data[0].trim().toLowerCase().equals("type1") ) {
					int ret = do_type1( sp_data );
					
					// 處理結果訊息
					String result_message = "";
					switch( ret ) {
					case -2 : result_message = ret + ", Managed device is fail, because unknown device type!"; break;
					case -1 : result_message = ret + ", Action fail!"; break;
					case 0 : result_message = ret + ", Successful!"; break;
					}
					
					// 回送處理結果訊息
					output.write( result_message.getBytes() );
					output.flush();
				// type2 的 command
				} else if( sp_data[0].trim().toLowerCase().equals("type2") ) {
					String result = do_type2( sp_data );
					
					// 回送處理結果訊息
					output.write( result.getBytes() );
					output.flush();					
				// type3 的 command
				} else if( sp_data[0].trim().toLowerCase().equals("type3") ) {
					String result = do_type3( sp_data );
				
					// 回送處理結果訊息
					output.write( result.getBytes() );
					output.flush();					
				}
			}
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( "NorthboundActionThread - Exception : " + e.toString() );
		} finally {
			if( input != null ) {
				try { input.close(); } catch( Exception e ) { }
			}
			if( output != null ) {
				try { output.close(); } catch( Exception e ) { }
			}
			if( connection_p != null ) {
				try { connection_p.close(); } catch( Exception e ) { }
			}
			synchronized( NorthboundServer.current_connection ) {
				NorthboundServer.current_connection--;
			}
		}
	}
}
