package Core.data.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import Core.data.joint.DeviceReportDS;
import Core.main.Main;

public class DeviceReportImpl implements DeviceReportDS {

	public static String DATA_TYPE_STRING = "text";
	public static String DATA_TYPE_BYTE = "byte";
	
	@Override
	public void sendTextToReport( int interface_id, int node_id, String[] data ) {
		if( data == null ) {
			Main.log_handler.writeSystemLog("DeviceReportImpl::sendTextToReport - data( " + interface_id + " , " + node_id + " ) is null!");
			return;
		}
		
		Session se = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		String sqlCmd = null;
		BoundStatement temp = null;
		try {
			while( se == null ) {
				se = Main.cassandra_manager.getSession();
				Thread.sleep(100);
			}
			
			sqlCmd = "SELECT * FROM iot_report.report_item WHERE interface_id = ? AND node_id = ?";
			statement = se.prepare( sqlCmd );
			temp = new BoundStatement(statement);
			temp.setInt( 0 , interface_id );
			temp.setInt( 1 , node_id );
			results = se.execute( temp );
				
			if( results == null || results.all().size() == 0 ) {
				sqlCmd = "INSERT INTO iot_report.report_item( interface_id , node_id , create_time , record_count , report_type ) VALUES (  ? , ? , ? , ? , ? )";
				statement = se.prepare(sqlCmd);					
				Date date = Calendar.getInstance().getTime();
				temp = statement.bind().setInt( 0 , interface_id )
				                       .setInt( 1 , node_id )
				                       .setTimestamp( 2 , date )
				                       .setInt( 3 , data.length )
				                       .setString( 4 , DATA_TYPE_STRING );
				se.execute( temp );
			}
				
			String report_id = Integer.toString( interface_id ) + Integer.toString( node_id ) + DATA_TYPE_STRING;
			String temp_data[] = { "" , "" , "" , "" , "" , "" , "" , "" };
			int max_data = data.length < 8 ? data.length : 8; 
			for( int i = 0 ; i < max_data ; i++ ) {
				temp_data[i] = data[i];
			}
			sqlCmd = "INSERT INTO iot_report.report_detail_type_text( report_id , data_1 , data_2 , data_3 , data_4 , data_5 , data_6 , data_7 , data_8 , update_time ) VALUES (  ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) USING TTL 604800";
			statement = se.prepare(sqlCmd);					
			Date date = Calendar.getInstance().getTime();
			temp = statement.bind().setString( 0 , report_id )
			                       .setString( 1 , temp_data[0] )
			                       .setString( 2 , temp_data[1] )
			                       .setString( 3 , temp_data[2] )
			                       .setString( 4 , temp_data[3] )
			                       .setString( 5 , temp_data[4] )
			                       .setString( 6 , temp_data[5] )
			                       .setString( 7 , temp_data[6] )
			                       .setString( 8 , temp_data[7] )
			                       .setTimestamp( 9 , date );
			se.execute( temp );
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( "DeviceReportImpl::sendTextToReport - Exception e : " + e.toString() );
		} finally {
			if( se != null ) {
				Main.cassandra_manager.release(se);
			}
		}
	}
	
	@Override
	public Vector<String> getTextReportData( int interface_id , int node_id , String start_datetime , String end_datetime ) {
		Vector<String> ret = new Vector<String>();
		Session se = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		String sqlCmd = null;
		BoundStatement temp = null;
		try {
			while( se == null ) {
				se = Main.cassandra_manager.getSession();
				Thread.sleep(100);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date s_date = sdf.parse( start_datetime );
			Date e_date = sdf.parse( end_datetime );
			
			String report_id = Integer.toString( interface_id ) + Integer.toString( node_id ) + DATA_TYPE_STRING;
			sqlCmd = "SELECT report_id , data_1 , data_2 , data_3 , data_4 , data_5 , data_6 , data_7 , data_8 , update_time FROM iot_report.report_detail_type_text WHERE report_id = ? AND update_time >= ? AND update_time <= ?";
			statement = se.prepare(sqlCmd);
			temp = statement.bind().setString( 0 , report_id )
			                       .setTimestamp( 1 , s_date )
			                       .setTimestamp( 2 , e_date );
			results = se.execute( temp );
			for( Row row : results ) {
				String buf = "";
				buf += row.getString(0) + ",";
				buf += row.getString(1) + ","; 
				buf += row.getString(2) + ","; 
				buf += row.getString(3) + ","; 
				buf += row.getString(4) + ","; 
				buf += row.getString(5) + ","; 
				buf += row.getString(6) + ",";
				buf += row.getString(7) + ",";
				buf += row.getString(8) + ",";
				Date date = row.getTimestamp(9);
				buf += sdf.format(date);
				ret.add( buf );
			}
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( "DeviceReportImpl::getTextReportData - Exception e : " + e.toString() );
		} finally {
			if( se != null ) {
				Main.cassandra_manager.release(se);
			}
		}
		
		return ret;
	}
}
