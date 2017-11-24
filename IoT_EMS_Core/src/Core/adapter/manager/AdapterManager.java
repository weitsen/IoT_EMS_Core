package Core.adapter.manager;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Vector;

import Core.adapter.device.DeviceInterface;
import Core.adapter.device.DeviceNode;
import Core.data.impl.DeviceManagerImpl;
import Core.main.Main;

public class AdapterManager {

	private Vector<DeviceNode> manage_device_list = null;
	private Vector<CycleJob> device_cycle_job_list = null;
	
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	public AdapterManager() {
		manage_device_list = new Vector<DeviceNode>(); 
		device_cycle_job_list = new Vector<CycleJob>();
	}
	
	// �[�J�S�w�˸m��Cycle Job
	public int addCycleJob( CycleJob job_task ) {
		int result = 0;
		try {
			// �p��Z���̱���5��
			Date time_ = new Date();
			long current_datetime = time_.getTime();			
			current_datetime = current_datetime % job_task.getPeriodTime();
			long diff_time = job_task.getPeriodTime() - current_datetime;
			job_task.setStartTime( diff_time );
			
			synchronized( device_cycle_job_list ) {
				device_cycle_job_list.add( job_task );
			}
			
			Main.task_manager.addPeriodicSystemTaskToScheduler(job_task);
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return result;
	}
	
	// �����S�w�˸m���Ҧ�Cycle Job
	public int removeDeviceAllCycleJob( int node_id ) {
		int result = 0;
		synchronized( device_cycle_job_list ) {
			// ��M�ۦP���˸mnode_id
			for( int i = 0 ; i < device_cycle_job_list.size() ; i++ ) {
				if( device_cycle_job_list.get(i).getNode_id() == node_id ) {
					// �����Ƶ{��(�R���Ʃw���P���ʤu�@)
					device_cycle_job_list.get(i).scheduler.cancel();
					device_cycle_job_list.get(i).scheduler = null;
					device_cycle_job_list.remove(i);
					i--;
				}
			}
		}
		return result;
	}
	
	// �[�J�Ǻ޸˸m
	public int addDevice( DeviceNode device ) {
		device.initDeviceName();
		
		DeviceManagerImpl dev_db = new DeviceManagerImpl();
		
		// �`�I��ƶ�J��Ʈw
		int node_id = dev_db.addDeviceToDB( device.getDevice_ip() , 
		                                    device.getManage_type() ,
		                                    device.getDevice_info() ,
		                                    device.getDevice_status() ,
		                                    device.getDeivce_account() ,
		                                    device.getDeivce_passwd() 
		);
		
		if( node_id != -1 ) {
			device.setNode_id(node_id);
			
			// ��JList��(memory)
			synchronized( manage_device_list ) {
				this.manage_device_list.add(device);
				device.initDeviceConfiguration();
			}

			/*
			// ������ƶ�J��Ʈw
			for( int i = 0 ; i < device.getDeviceInterfaceList().size() ; i++ ) {
				DeviceInterface temp = device.getDeviceInterfaceList().get(i);
				dev_db.addInterfaceToDB( node_id , temp.getInterface_id() , temp.getInterface_info() );
			}
			*/
		}
		
		return node_id;
	}
	
	// �����Ǻ޸˸m
	public int remodeDevice( String ipaddr ) {
		DeviceNode node = null;
		// �qList�������˸m(memory)
		for( int i = 0 ; i < manage_device_list.size(); i++ ){
			if( manage_device_list.get(i).getDevice_ip().equals( ipaddr ) ){
				synchronized( manage_device_list ) {
					node = this.manage_device_list.remove( i );
				}
				break;
			}
		}
		if( node != null ) {
			removeDeviceAllCycleJob( node.getNode_id() );
		
			// �q��Ʈw�������˸m
			DeviceManagerImpl dev_db = new DeviceManagerImpl();
			dev_db.removeDeviceFromDB( node.getDevice_ip() );
		}
		
		return 0;
	}
	
	// ����Ǻ޸˸m
	public DeviceNode getDeviceByIpaddr( String ipaddr ) {
		DeviceNode ret = null;
		
		for( int i = 0 ; i < manage_device_list.size(); i++ ){
			if( manage_device_list.get(i).getDevice_ip().equals( ipaddr ) ){
				ret = manage_device_list.get( i );
			}
		}
		return ret;
	}
	
	// ��������Ǻ޳]�ƪ�IP�M��
	public Vector<String> getAllDeviceIPList() {
		Vector<String> ret = new Vector<String>();
		
		for( int i = 0 ; i < manage_device_list.size(); i++ ){
			ret.add( manage_device_list.get(i).getDevice_ip() );
		}
		
		return ret;
	}

	// �t�Ϊ�l�ƻݰ��檺���J�ʧ@(���JDB���w�O���s�b���Ҧ��˸m)
	public void loadAllDeviceInfoFromDB() {
		Main.log_handler.writeSystemLog("Load all device info from database...");
		DeviceManagerImpl dev_db = new DeviceManagerImpl();
		Vector<String> device_ip_list = dev_db.getAllDeviceIPList();
		for( int i = 0 ; i < device_ip_list.size() ; i++ ) {
			String device_ip = device_ip_list.get(i);
			Vector device_info = dev_db.getDeviceInfo(device_ip);
			
			if( device_info.size() != 0 ) {
				int node_id = (int)device_info.get(0);
				String device_ipx = (String)device_info.get(1);
				String class_name = (String)device_info.get(2);
				String device_infox = (String)device_info.get(3);
				String device_status = (String)device_info.get(4);
				String device_account = (String)device_info.get(5);
				String device_password = (String)device_info.get(6);
				try {
					Class<?> clazz = Class.forName(class_name);
					Constructor<?> constructor = clazz.getConstructor(String.class, String.class, String.class, String.class);
					Object instance = constructor.newInstance( device_ipx , device_infox , device_account , device_password );
					DeviceNode device = (DeviceNode)instance;
					device.setNode_id( node_id );
					device.setDevice_status( device_status );
					device.initDeviceConfiguration();
					
					manage_device_list.add( device );
					Main.log_handler.writeSystemLog( "Load device(" + device.getDevice_ip() + ") to system...." );
				} catch( Exception e ) {
					e.printStackTrace();
					Main.log_handler.writeSystemLog( e.toString() );
				}
			}
		}
	}
}
