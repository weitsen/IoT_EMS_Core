package Core.adapter.device;

import Core.adapter.unit.InterfaceItem;

public class DeviceInterface extends InterfaceItem {

	private int node_id;
	private String interface_info;
	private int interface_id;
	private String record_type;
	
	//--------------------------------------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------------------------------------
	// �~��InterfaceItem  ( InterfaceItem ���غc�l�p�Gset �@�w�n����  , ���M�n�]�w�ŭ�  , �_�h �o�@�h�غc�l �����  �]���L�����D�n������� )
	public DeviceInterface() {
		interface_id = -1;
		node_id = -1;
		interface_info = "N/A";
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

	public int getInterface_id() {
		return interface_id;
	}

	public void setInterface_id(int interface_id) {
		this.interface_id = interface_id;
	}
	
	public String getInterface_info() {
		return interface_info;
	}

	public void setInterface_info(String interface_info) {
		this.interface_info = interface_info;
	}
	
	public String getRecord_type() {
		return record_type;
	}

	public void setRecord_type(String record_type) {
		this.record_type = record_type;
	}

}
