package Core.adapter.devices.MyTestDevice;

import Core.adapter.device.DeviceInterface;
import Core.adapter.device.DeviceNode;
import Core.main.Main;

// �������ո˸m�d��
public class MyTestDevice extends DeviceNode {
	// �����]  �U�Ȧۤv���ݨD �U�ȼg��
	
	// �غc�l(�зǤ���)
	public MyTestDevice( String device_ip_ , String info , String account , String password ) {
		super( device_ip_, info , account , password );
	}

	// �C��device �����n init
	// �]�n�g function ���e 
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
		
		// �[�J�ʴ�����
		MyTestDeviceSensorA interface_a = new MyTestDeviceSensorA( this.getNode_id() );
		this.addDeviceInterface( interface_a );
		
		// �Ұʺʴ��u�@
		this.addCycleJob( "PollingTaskTest" , 5000 );
	}
	
	// �ʴ����u�@���e�y�z
	public void PollingTaskTest() {
		Main.log_handler.writeAdapterLog("This adapter log~~~");
		// ���y�Ҧ�����
		for( int i = 0 ; i < this.getDeviceInterfaceList().size() ; i++ ) {
			DeviceInterface temp = this.getDeviceInterfaceList().get(i);
			if( temp.getInterfaceItem_type().equals("SensorA") ) {
				MyTestDeviceSensorA buf = (MyTestDeviceSensorA)temp;
				// ����ʴ��u�@
				buf.pollingSensorData();
			}
		}
	}
}
