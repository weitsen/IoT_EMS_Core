package Core.log;

import java.util.Vector;

public class LogQueue {

	public static final int LOG_SIZE = 5;
	public static final int LOG_SYSTEM_INDEX = 0;
	public static final int LOG_ACCESS_INDEX = 1;
	public static final int LOG_DB_INDEX = 2;
	public static final int LOG_ADAPTER_INDEX = 3;
	public static final int LOG_DEBUG_INDEX = 4;
	
	public static Vector<LogMessage> log_queue_0 = new Vector(); 
	public static Vector<LogMessage> log_queue_1 = new Vector();
	public static Vector<LogMessage> log_queue_2 = new Vector();
	public static Vector<LogMessage> log_queue_3 = new Vector();
	public static Vector<LogMessage> log_queue_4 = new Vector();
	
	public static final int MAX_LOG_QUEUE_SIZE[] = { 200 , 200 , 200 , 200 , 200 };
	

	
}
