package Core.data.joint;

import java.util.Vector;

public interface DeviceReportDS {
	public void sendTextToReport( int interface_id , int node_id , String data[] );
	
	public Vector<String> getTextReportData( int interface_id , int node_id , String start_datetime , String end_datetime );
}
