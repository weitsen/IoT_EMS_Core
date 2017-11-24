package Core.error;

import java.util.Hashtable;

public class ErrorTable {
	// 定表格
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
	
	// Hashtable 方便查詢,快  表格  ( 給 key 可以查詢 value ) key 和  value 可以自己定義型態  彈性大
	// 樣板 Hashtable 此指定型態 < Integer , ErrorItem > 
	private static Hashtable< Integer , ErrorItem > table = null; 
	
	public ErrorTable() {
		table = new Hashtable< Integer , ErrorItem >();
		//  error_mapping.length 會掃描col ( if  error_mapping[0].length 會掃描row )
		for( int i = 0 ; i < error_mapping.length ; i++ ) {
			// ErrorIteam( int id , String msg )
			ErrorItem obj = new ErrorItem( Integer.parseInt( error_mapping[i][0] ) , error_mapping[i][1] );
			// put( key , value )  // 此型態 可以不定  也可以定義  目前指定型態為 < Integer , ErrorItem >  
			// Integer 類別有 static parseInt() 方法，可以將字串 (string) 中的整數轉換 int 型態的數值 (value)
			// static int parseInt(String s)  將字串解析為十進位整數
			table.put( Integer.parseInt( error_mapping[i][0] ) , obj );
		}
	}
	
	public String getErrorMessage( int code_id ) {
		// public V get(Object key)
		// 給 key 查出 value ---> 在用此值(ErrorIteam) 查  訊息
		return table.get( code_id ).getMessage();
	}
}
