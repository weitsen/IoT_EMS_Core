package Core.error;

import java.util.Hashtable;

public class ErrorTable {
	// �w���
	/*
	public static final int SYSTEM_ERROR_ID = 1001;
	public static final int NONE_SYSTEM_ERROR_ID = 1002;
	public static final int PERIODIC_SYSTEM_ERROR_ID = 1003;
	public static final int NONE_PERIODIC_SYSTEM_ERROR_ID = 1004;
	public static final int TASK_MANAGER_ERROR_ID = 1005;
	public static final int LOG_MANAGER_ERROR_ID = 1006;
	*/
	private static final String error_mapping[][] = {
	{ "1001" , "System error!" } ,
	{ "1002" , "NoneSystem error!" } ,
	{ "1003" , "Periodic System error!" } ,
	{ "1004" , "NonePeriodic System error!" } ,
	{ "1005" , "Task Manager error!" },
	{ "1006" , "Log Manager error!" },
	};
	
	// Hashtable ��K�d��,��  ���  ( �� key �i�H�d�� value ) key �M  value �i�H�ۤv�w�q���A  �u�ʤj
	// �˪O Hashtable �����w���A < Integer , ErrorItem > 
	private static Hashtable< Integer , ErrorItem > table = null; 
	
	public ErrorTable() {
		table = new Hashtable< Integer , ErrorItem >();
		//  error_mapping.length �|���ycol ( if  error_mapping[0].length �|���yrow )
		for( int i = 0 ; i < error_mapping.length ; i++ ) {
			// ErrorIteam( int id , String msg )
			ErrorItem obj = new ErrorItem( Integer.parseInt( error_mapping[i][0] ) , error_mapping[i][1] );
			// put( key , value )  // �����A �i�H���w  �]�i�H�w�q  �ثe���w���A�� < Integer , ErrorItem >  
			// Integer ���O�� static parseInt() ��k�A�i�H�N�r�� (string) ��������ഫ int ���A���ƭ� (value)
			// static int parseInt(String s)  �N�r��ѪR���Q�i����
			table.put( Integer.parseInt( error_mapping[i][0] ) , obj );
		}
	}
	
	public String getErrorMessage( int code_id ) {
		// public V get(Object key)
		// �� key �d�X value ---> �b�Φ���(ErrorIteam) �d  �T��
		return table.get( code_id ).getMessage();
	}
}
