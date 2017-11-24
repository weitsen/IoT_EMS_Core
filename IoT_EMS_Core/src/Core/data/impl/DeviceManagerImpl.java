package Core.data.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import Core.MySQL.MySQLConn;
import Core.adapter.device.DeviceInterface;
import Core.data.joint.DeviceManagerDS;
import Core.main.Main;

public class DeviceManagerImpl implements DeviceManagerDS {

	@Override
	public int addDeviceToDB( String device_ip , String device_mode , String device_description , String device_status , String device_account , String device_passwd ) {
		int ret = -1;
		MySQLConn conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = Main.mysql_manager.getConnection();
			
			// 讀取目前是否有重複的裝置
			sql = "SELECT node_id FROM device_node WHERE device_ip = ? ";
			stmt = conn.getConnection().prepareStatement(sql);
			stmt.setString( 1 , device_ip );
			rs = stmt.executeQuery();
			// 有找到相同的節點資料
			if( rs.next() ) {
				ret = rs.getInt(1);
			// 沒找到相同的節點資料
			} else {			
				// 塞入資料
				sql = "INSERT INTO device_node( device_ip , device_mode , device_description , device_status , device_account , device_passwd ) VALUES ( ? , ? , ? , ? , ? , ? )";
				stmt = conn.getConnection().prepareStatement(sql);
				stmt.setString( 1 , device_ip );
				stmt.setString( 2 , device_mode );
				stmt.setString( 3 , device_description );
				stmt.setString( 4 , device_status );
				stmt.setString( 5 , device_account );
				stmt.setString( 6 , device_passwd );
				int count = stmt.executeUpdate();
				
				// 讀取配置到的node_id
				sql = "SELECT node_id FROM device_node WHERE device_ip = ? ";
				stmt = conn.getConnection().prepareStatement(sql);
				stmt.setString( 1 , device_ip );
				rs = stmt.executeQuery();
				if( rs.next() ) {
					ret = rs.getInt(1);
				}
			}
		} catch( SQLException sql_e ) {
			sql_e.printStackTrace();
			Main.log_handler.writeSystemLog( sql_e.toString() );
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( e.toString() );
		} finally {
			if( conn != null ) {
				Main.mysql_manager.releaseConnection(conn);
			}
		}
		return ret;
	}

	@Override
	public int addInterfaceToDB( int node_id , int interface_id , String interface_description , String interfaceItem_type , String interfaceItem_name ) {
		int ret = -1;
		MySQLConn conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = Main.mysql_manager.getConnection();
		
			// 讀取目前是否有重複的裝置
			sql = "SELECT node_id FROM device_interface WHERE node_id = ? AND interface_id = ?";
			stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt( 1 , node_id );
			stmt.setInt( 2 , interface_id );
			rs = stmt.executeQuery();
			// 有找到相同的節點資料
			if( rs.next() ) {
				// 更新資料
				sql = "UPDATE device_interface SET interface_description = ? , interfaceItem_type = ? , interfaceItem_name = ? WHERE  node_id = ? AND interface_id = ? ";
				stmt = conn.getConnection().prepareStatement(sql);
				stmt.setString( 1 , interface_description );
				stmt.setString( 2 , interfaceItem_type );
				stmt.setString( 3 , interfaceItem_name );
				stmt.setInt( 4 , node_id );
				stmt.setInt( 5 , interface_id );
				int count = stmt.executeUpdate();
			} else {
				// 塞入資料
				sql = "INSERT INTO device_interface( node_id , interface_id , interface_description , interfaceItem_type , interfaceItem_name ) VALUES ( ? , ? , ? , ? , ? )";
				stmt = conn.getConnection().prepareStatement(sql);
				stmt.setInt( 1 , node_id );
				stmt.setInt( 2 , interface_id );
				stmt.setString( 3 , interface_description );
				stmt.setString( 4 , interfaceItem_type );
				stmt.setString( 5 , interfaceItem_name );
				int count = stmt.executeUpdate();
			}
			ret = 0;
		} catch( SQLException sql_e ) {
			sql_e.printStackTrace();
			Main.log_handler.writeSystemLog( sql_e.toString() );
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( e.toString() );
		} finally {
			if( conn != null ) {
				Main.mysql_manager.releaseConnection(conn);
			}
		}
		return ret;
	}
	
	@Override
	public int removeDeviceFromDB( String device_ip ) {
		int ret = -1;
		MySQLConn conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;
		try {
			conn = Main.mysql_manager.getConnection();
			// 讀取配置到的node_id
			sql = "SELECT node_id FROM device_node WHERE device_ip = ? ";
			stmt = conn.getConnection().prepareStatement(sql);
			stmt.setString( 1 , device_ip );
			rs = stmt.executeQuery();
			if( rs.next() ) {
				ret = rs.getInt(1);
			}
			
			sql = "DELETE FROM device_node WHERE device_ip = ? ";
			stmt = conn.getConnection().prepareStatement(sql);
			stmt.setString( 1 , device_ip );
			count = stmt.executeUpdate();
			
			sql = "DELETE FROM device_interface WHERE node_id = ? ";
			stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt( 1 , ret );
			count = stmt.executeUpdate();
		} catch( SQLException sql_e ) {
			sql_e.printStackTrace();
			Main.log_handler.writeSystemLog( sql_e.toString() );
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( e.toString() );
		} finally {
			if( conn != null ) {
				Main.mysql_manager.releaseConnection(conn);
			}
		}
		return ret;
	}

	@Override
	public Vector<String> getAllDeviceIPList() {
		Vector<String> all_ip_list = new Vector<String>();
		MySQLConn conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = Main.mysql_manager.getConnection();
			sql = "SELECT device_ip FROM device_node";
			stmt = conn.getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();
			// 有找到相同的節點資料
			while( rs.next() ) {
				all_ip_list.add( rs.getString("device_ip") );
			}
		} catch( SQLException sql_e ) {
			sql_e.printStackTrace();
			Main.log_handler.writeSystemLog( sql_e.toString() );
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( e.toString() );
		} finally {
			if( conn != null ) {
				Main.mysql_manager.releaseConnection(conn);
			}
		}
		return all_ip_list;
	}

	@Override
	public Vector<String> getDeviceInfo( String device_ip ) {
		Vector device_info = new Vector();
		MySQLConn conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = Main.mysql_manager.getConnection();

			sql = "SELECT node_id , device_ip , device_mode , device_description , device_status , device_account , device_passwd FROM device_node WHERE device_ip = ? ";
			stmt = conn.getConnection().prepareStatement(sql);
			stmt.setString( 1 , device_ip );
			rs = stmt.executeQuery();
			// 有找到相同的節點資料
			if( rs.next() ) {
				device_info.add( rs.getInt(1) );     // node_id
				device_info.add( rs.getString(2) );  // device_ip
				device_info.add( rs.getString(3) );  // device_mode
				device_info.add( rs.getString(4) );  // device_description
				device_info.add( rs.getString(5) );  // device_status
				device_info.add( rs.getString(6) );  // device_account
				device_info.add( rs.getString(7) );  // device_passwd
			}
		} catch( SQLException sql_e ) {
			sql_e.printStackTrace();
			Main.log_handler.writeSystemLog( sql_e.toString() );
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( e.toString() );
		} finally {
			if( conn != null ) {
				Main.mysql_manager.releaseConnection(conn);
			}
		}
		return device_info;
	}

	@Override
	public Vector<Vector> getInterfaceListByNodeID( int node_id , int interface_id ) {
		Vector<Vector> device_interface_list = new Vector<Vector>();
		
		MySQLConn conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = Main.mysql_manager.getConnection();

			sql = "SELECT node_id , interface_id , interface_description , interfaceItem_type , interfaceItem_name FROM device_interface WHERE node_id = ? AND interface_id = ?";
			stmt = conn.getConnection().prepareStatement(sql);
			stmt.setInt( 1 , node_id );
			stmt.setInt( 2 , interface_id );
			rs = stmt.executeQuery();
			// 有找到相同的節點資料
			while( rs.next() ) {
				Vector interface_info = new Vector();
				interface_info.add( rs.getInt(1) );     // node_id
				interface_info.add( rs.getInt(2) );     // interface_id
				interface_info.add( rs.getString(3) );  // interface_description
				interface_info.add( rs.getString(4) );  // interfaceItem_type
				interface_info.add( rs.getString(5) );  // interfaceItem_name
				device_interface_list.add(interface_info);
			}
		} catch( SQLException sql_e ) {
			sql_e.printStackTrace();
			Main.log_handler.writeSystemLog( sql_e.toString() );
		} catch( Exception e ) {
			e.printStackTrace();
			Main.log_handler.writeSystemLog( e.toString() );
		} finally {
			if( conn != null ) {
				Main.mysql_manager.releaseConnection(conn);
			}
		}
		
		return device_interface_list;
	}
}
