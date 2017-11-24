package Core.adapter.devices.MyTestDevice;

import Core.adapter.device.DeviceInterface;
import Core.data.impl.DeviceReportImpl;

//�������ո˸m�d��(�P��������)
public class MyTestDeviceSensorA extends DeviceInterface {
	
	// �зǵ��c
	public MyTestDeviceSensorA( int node_id ) {
		this.setNode_id(node_id);
		this.setInterfaceItem_type("SensorA");
		this.setInterfaceItem_name("Test");
		this.setRecord_type( DeviceReportImpl.DATA_TYPE_STRING );
	}
	
	// �ʴ����u�@
	public void pollingSensorData() {
		DeviceReportImpl db = new DeviceReportImpl();
		
		// ���pŪ�쪺��Ʀp�U
		String data[] = { "15" , "22.0" };
		
		// �e�JDB(Cassandra)�x�s
		db.sendTextToReport( this.getInterface_id() , this.getNode_id() , data );
		
		// �L�X�T��(����)
		System.out.println("Hello~~(" + this.getNode_id() + ")");
	}
}
