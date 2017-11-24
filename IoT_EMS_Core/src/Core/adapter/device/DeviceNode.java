package Core.adapter.device;

import java.util.Vector;

import Core.adapter.manager.CycleJob;
import Core.data.impl.DeviceManagerImpl;
import Core.main.Main;

public class DeviceNode {
	// BasicDevice (EX:滑鼠)
	private int node_id;
	private	String device_ip;
	private String manage_type;
	private String device_info;
	private String device_status;
	private String deivce_account;
	private String deivce_passwd;
	private Vector<DeviceInterface> device_interfaces = null;
	
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	public DeviceNode( String device_ip_ , String info , String account , String password ) {
		setDevice_ip(device_ip_);
		device_interfaces = new Vector<DeviceInterface>();
		setDevice_info(info);
		device_status = "";
		manage_type = "";
		setDeivce_account(account);
		setDeivce_passwd(password);
	}
	
	// need to overwrite 初始化裝置內容
	public void initDeviceName() {
		
	}
	
	// need to overwrite 初始化裝置內容
	public void initDeviceConfiguration() {
		// plan to do
	}
	
	//--------------------------------------------------------------------------------------------------------
	// get / set
	//--------------------------------------------------------------------------------------------------------
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	public String getDevice_ip() {
		return device_ip;
	}
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}
	public String getManage_type() {
		return manage_type;
	}
	public void setManage_type(String device_type) {
		this.manage_type = device_type;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getDevice_status() {
		return device_status;
	}
	public void setDevice_status(String device_status) {
		this.device_status = device_status;
	}

	public String getDeivce_account() {
		return deivce_account;
	}

	public void setDeivce_account(String deivce_account) {
		this.deivce_account = deivce_account;
	}

	public String getDeivce_passwd() {
		return deivce_passwd;
	}

	public void setDeivce_passwd(String deivce_passwd) {
		this.deivce_passwd = deivce_passwd;
	}
	
	public void setDeviceInterfaceList( Vector<DeviceInterface> device_interfaces_list ) {
		device_interfaces = device_interfaces_list;
	}
	
	public  Vector<DeviceInterface> getDeviceInterfaceList() {
		return device_interfaces;
	}
	
	//--------------------------------------------------------------------------------------------------------
	// add
	//--------------------------------------------------------------------------------------------------------
	public void addDeviceInterface( DeviceInterface dinterface ) {
		DeviceManagerImpl dev_db = new DeviceManagerImpl();
		if( device_interfaces != null && dinterface.getInterface_id() == -1 ) {
			dinterface.setNode_id(node_id);
			if( device_interfaces.size() != 0 ) {
				// set new id number ( 找到目前的+1)
				dinterface.setInterface_id( device_interfaces.get( device_interfaces.size() - 1 ).getInterface_id() + 1 );
			} else {
				dinterface.setInterface_id( 0 );
			}
			device_interfaces.add(dinterface);	// EX:加入一個滑鼠左鍵介面
			dev_db.addInterfaceToDB( dinterface.getNode_id() , dinterface.getInterface_id() , dinterface.getInterface_info() , dinterface.getInterfaceItem_type() , dinterface.getInterfaceItem_name() );
		} else if( device_interfaces != null && dinterface.getInterface_id() != -1 ) {
			dinterface.setNode_id(node_id);
			device_interfaces.add(dinterface);	// EX:加入一個滑鼠左鍵介面
			dev_db.addInterfaceToDB( dinterface.getNode_id() , dinterface.getInterface_id() , dinterface.getInterface_info() , dinterface.getInterfaceItem_type() , dinterface.getInterfaceItem_name() );
		}
	}
	
	public int addCycleJob( String name , int interval_millisecond ) {
		// 在這裡 addCycleJob 指列出工作清單的意思   準備給   AdapterManager 的 addCycleJob 此才是真正執行工作的地方
		int result = 0;

		try {
			CycleJob cjob = new CycleJob();
			cjob.setNode_id(node_id);
			cjob.setObj( this );
			cjob.setMethodName( name );
			//cjob.setPeriodTime( interval * 1000 * 60 );
			cjob.setPeriodTime( interval_millisecond );
			Main.adapter_manager.addCycleJob( cjob );
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog("BasicDevice has one execption for addCycleJob(" + e.toString() + ")");
			result = -1;
		}
		return result;
	}
}
