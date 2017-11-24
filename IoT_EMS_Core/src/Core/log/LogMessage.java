package Core.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class LogMessage {
	
	private String log_time;
	private String log_type;
	private String log_message;

	public static final String LOG_SYSTEM = "system";
	public static final String LOG_ACCESS = "access";
	public static final String LOG_DB = "database";
	public static final String LOG_ADAPTER = "adapter";
	public static final String LOG_DEBUG = "debug";
	
	
	
	public LogMessage( String log_type_ , String log_message_ ){
		// 時間格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// Calendar.getInstance() 抓系統目前時間物件
		Calendar sys_time = Calendar.getInstance();
		// 轉換成你要的格式
		log_time = sdf.format( sys_time.getTime() ); 
		
		log_type = log_type_;
		log_message = log_message_;
		
	}
	
	public String toString(){
		
		String buf = new String();
		buf = "[" + log_time + "] (" + log_type + ")" + log_message;
		
		return buf;
	}
	
	
}
