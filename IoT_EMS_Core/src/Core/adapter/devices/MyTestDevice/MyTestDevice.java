package Core.adapter.devices.MyTestDevice;

import Core.adapter.device.DeviceInterface;
import Core.adapter.device.DeviceNode;
import Core.main.Main;

// 虛擬測試裝置範例
public class MyTestDevice extends DeviceNode {
	// 此假設  顧客自己的需求 顧客寫的
	
	// 建構子(標準介面)
	public MyTestDevice( String device_ip_ , String info , String account , String password ) {
		super( device_ip_, info , account , password );
	}

	// 每個device 都有要 init
	// 也要寫 function 內容 
	// need to overwrite
	@Override
	public void initDeviceName() {
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if( enclosingClass != null ) {
			this.setManage_type( enclosingClass.getName() );
		} else {
			this.setManage_type( getClass().getName() );
		}
	}
	
	// need to overwrite
	@Override
	public void initDeviceConfiguration() {
		Main.log_handler.writeAdapterLog("This adapter load initDeviceConfiguration()...");
		
		// 加入監測介面
		MyTestDeviceSensorA interface_a = new MyTestDeviceSensorA( this.getNode_id() );
		this.addDeviceInterface( interface_a );
		
		// 啟動監測工作
		this.addCycleJob( "PollingTaskTest" , 5000 );
	}
	
	// 監測的工作內容描述
	public void PollingTaskTest() {
		Main.log_handler.writeAdapterLog("This adapter log~~~");
		// 掃描所有介面
		for( int i = 0 ; i < this.getDeviceInterfaceList().size() ; i++ ) {
			DeviceInterface temp = this.getDeviceInterfaceList().get(i);
			if( temp.getInterfaceItem_type().equals("SensorA") ) {
				MyTestDeviceSensorA buf = (MyTestDeviceSensorA)temp;
				// 執行監測工作
				buf.pollingSensorData();
			}
		}
	}
}
