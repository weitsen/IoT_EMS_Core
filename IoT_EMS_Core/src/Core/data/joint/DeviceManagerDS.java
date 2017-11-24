package Core.data.joint;

import java.util.Vector;

import Core.adapter.device.DeviceInterface;

public interface DeviceManagerDS {
	
	public int addDeviceToDB( String device_ip , String device_mode , String device_description , String device_status , String device_account , String device_passwd );
	
	public int addInterfaceToDB( int node_id , int interface_id , String interface_description , String interfaceItem_type , String interfaceItem_name );
	
	public int removeDeviceFromDB( String device_ip );
	
	public Vector<String> getAllDeviceIPList();
	
	public Vector getDeviceInfo( String device_ip );
	
	public Vector<Vector> getInterfaceListByNodeID( int node_id , int interface_id );
}
