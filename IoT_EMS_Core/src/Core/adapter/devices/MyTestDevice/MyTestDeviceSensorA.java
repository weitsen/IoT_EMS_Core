package Core.adapter.devices.MyTestDevice;

import Core.adapter.device.DeviceInterface;
import Core.data.impl.DeviceReportImpl;

//虛擬測試裝置範例(感測器介面)
public class MyTestDeviceSensorA extends DeviceInterface {
	
	// 標準結構
	public MyTestDeviceSensorA( int node_id ) {
		this.setNode_id(node_id);
		this.setInterfaceItem_type("SensorA");
		this.setInterfaceItem_name("Test");
		this.setRecord_type( DeviceReportImpl.DATA_TYPE_STRING );
	}
	
	// 監測的工作
	public void pollingSensorData() {
		DeviceReportImpl db = new DeviceReportImpl();
		
		// 假如讀到的資料如下
		String data[] = { "15" , "22.0" };
		
		// 送入DB(Cassandra)儲存
		db.sendTextToReport( this.getInterface_id() , this.getNode_id() , data );
		
		// 印出訊息(測試)
		System.out.println("Hello~~(" + this.getNode_id() + ")");
	}
}
