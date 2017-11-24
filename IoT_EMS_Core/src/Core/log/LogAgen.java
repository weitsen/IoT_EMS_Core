package Core.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;

import Core.task.task.unit.SystemTask;

public class LogAgen extends SystemTask {

	private int interval_time = 1000;
	private static final int file_size = 102400000;		// 10Mbytes
	
	private final String log_file_path = "./log/";
	private final String system_log_file_name = "system_log.log";
	private final String access_log_file_name = "access_log.log";
	private final String database_log_file_name = "database_log.log";
	private final String adapter_log_file_name = "adapter_log.log";
	private final String debug_log_file_name = "debug_log.log";
	
	
	public LogAgen( int i_time ) {
		interval_time = i_time;
		System.out.println( "Log agent init..." );
	}
		
	
	// 設定時間清掉
	// [1]
	public void FlushSystemLogToFile() {		
		synchronized( LogQueue.log_queue_0 ) {
			// 沒有內容
			if( LogQueue.log_queue_0.size() == 0 ) {
				return;
			}

			FileWriter fileWriter = null;
			// IO 都要 try catch
			try {
				// 轉編碼
				//String current_path = URLDecoder.decode( LogAgen.class.getClassLoader().getResource("").getPath() , "UTF-8" );
				String current_path = "";
				// 寫檔的路徑+檔名
				String fname = current_path + log_file_path + system_log_file_name;
				String fname_bak = current_path + log_file_path + system_log_file_name + ".bak";
				
				// 給路徑就變物件  
				File fd = new File(fname);
				// 如果檔案大小  超過 設定容量大小  就做swap [把第一個檔改名為第二個檔，如果第二個檔不是空的，就先清掉第二個檔]  永遠寫第一個檔
				// 目的 : 以防止寫入的檔案大小太大 造成 記憶體 爆掉 ( 所以先設定一個 File Size 當門檻 )
				if( fd.length() > file_size ) {
					File fd2 = new File(fname_bak);
					// 存在就刪除
					if( fd2.exists() ) {
						fd2.delete();
					}
					// 改檔名(第一個檔名 改成 第二個檔名)
					fd.renameTo(fd2);
					// false 此檔就不用往下寫了(因為已經準備開新的檔)
					fileWriter = new FileWriter( fname , false );
				} else {
					// true 此檔可以繼續寫
					fileWriter = new FileWriter( fname , true );
				}
				
				for( int i = 0 ; i < LogQueue.log_queue_0.size() ; i++ ) {
					LogMessage temp = LogQueue.log_queue_0.get( i );
					// \r\n 換行符號(\r Windows)(\n Linux)
					// 寫檔
					fileWriter.write( temp.toString() + "\r\n" );
				}
			} catch( IOException e ) {
				StringWriter writer = new StringWriter();
				PrintWriter printWriter = new PrintWriter( writer );
				e.printStackTrace( printWriter );
				printWriter.flush();
			}
			
			if( fileWriter != null ) {
				try {
					// 關檔
					fileWriter.close();
				} catch( IOException e ) {
					e.printStackTrace();
				}
			}
			// 清掉 log_queue_0
			LogQueue.log_queue_0.clear();
		}
	}
	
	// [2]
	public void FlushAccessLogToFile(){
		synchronized( LogQueue.log_queue_1 ){
			if( LogQueue.log_queue_1.size() == 0 ){
				return ;
			}
			
			//
			FileWriter fileWriter = null;
			try{
				//String current_path = URLDecoder.decode( LogAgen.class.getClassLoader().getResource("").getPath() , "UTF-8" );
				String current_path = "";
				String fname = current_path + log_file_path + access_log_file_name;
				String fname_bak = current_path + log_file_path + access_log_file_name + ".bak";
				// open file
				File fd  = new File( fname );
				if( fd.length() > file_size ){
					File fd2 = new File( fname_bak );
					if( fd2.exists() ) {
						fd2.delete();
					}
					fd.renameTo(fd2);
					fileWriter = new FileWriter( fname , false );
				} else {
					fileWriter = new FileWriter( fname , true );
				}
				
				for( int i = 0 ; i < LogQueue.log_queue_1.size() ; i++ ){
					LogMessage temp = LogQueue.log_queue_1.get( i );
					fileWriter.write( temp.toString() + "\r\n" );
				}
				
			} catch ( IOException e ) {
				StringWriter writer = new StringWriter();
				PrintWriter printerWriter = new PrintWriter( writer );
				e.printStackTrace( printerWriter );
				printerWriter.flush();
				
			}
			
			if( fileWriter != null ){
				try{
					fileWriter.close();
				} catch ( IOException e){
					e.printStackTrace();
				}
			}
			
			LogQueue.log_queue_1.clear();
		}
	}
	
	// [3]
	public void FlushDatabaseLogToFile(){
		synchronized( LogQueue.log_queue_2 ){
			if( LogQueue.log_queue_2.size() == 0 ){
				return ;
			}
				
			//
			FileWriter fileWriter = null;
			try{
				//String current_path = URLDecoder.decode( LogAgen.class.getClassLoader().getResource("").getPath() , "UTF-8" );
				String current_path = "";
				String fname = current_path + log_file_path + database_log_file_name;
				String fname_bak = current_path + log_file_path + database_log_file_name + ".bak";
				// open file
				File fd  = new File( fname );
				if( fd.length() > file_size ){
					File fd2 = new File( fname_bak );
					if( fd2.exists() ) {
						fd2.delete();
					}
					fd.renameTo(fd2);
					fileWriter = new FileWriter( fname , false );
				} else {
					fileWriter = new FileWriter( fname , true );
				}
					
				for( int i = 0 ; i < LogQueue.log_queue_2.size() ; i++ ){
					LogMessage temp = LogQueue.log_queue_2.get( i );
					fileWriter.write( temp.toString() + "\r\n" );
				}
					
			} catch ( IOException e ) {
				StringWriter writer = new StringWriter();
				PrintWriter printerWriter = new PrintWriter( writer );
				e.printStackTrace( printerWriter );
				printerWriter.flush();
					
			}
				
			if( fileWriter != null ){
				try{
					fileWriter.close();
				} catch ( IOException e){
					e.printStackTrace();
				}
			}
				
			LogQueue.log_queue_2.clear();
		}
	}	
	
	// [4] adapter_log_file_name
	public void FlushAdapterLogToFile(){
		synchronized( LogQueue.log_queue_3 ){
			if( LogQueue.log_queue_3.size() == 0 ){
				return;
			}
			
			//
			FileWriter fileWriter = null;
			try{
				//String current_path = URLDecoder.decode( LogAgen.class.getClassLoader().getResource("").getPath() , "UTF-8" );
				String current_path = "";
				String fname = current_path + log_file_path + adapter_log_file_name;
				String fname_bak = current_path + log_file_path + adapter_log_file_name + ".bak";
				
				//
				File fd = new File( fname );
				if( fd.length() > file_size ){
					File fd2 = new File( fname_bak );
					if( fd2.exists() ){
						fd2.delete();
					}
					fd.renameTo(fd2);
					fileWriter = new FileWriter( fname , false );
				} else {
					fileWriter = new FileWriter( fname , true );
				}
				for( int i = 0 ; i < LogQueue.log_queue_3.size() ; i++ ){
					LogMessage temp = LogQueue.log_queue_3.get(i);
					fileWriter.write( temp.toString() + "\r\n" );
				}
				
			}catch( IOException e ){
				StringWriter writer = new StringWriter();
				PrintWriter printerWriter = new PrintWriter( writer );
				e.printStackTrace( printerWriter );
				printerWriter.flush();
			}
			
			if( fileWriter != null ){
				try{
					fileWriter.close();
				} catch ( IOException e ){
					e.printStackTrace();
				}
			}
			
			LogQueue.log_queue_3.clear();
		}
	}
	
	//[5]debug_log_file_name
	public void FlushDebugLogToFile(){
		synchronized( LogQueue.log_queue_4 ){
			if( LogQueue.log_queue_4.size() == 0 ){
				return;
			}
				
			//
			FileWriter fileWriter = null;
			try{
				//String current_path = URLDecoder.decode( LogAgen.class.getClassLoader().getResource("").getPath() , "UTF-8" );
				String current_path = "";
				String fname = current_path + log_file_path + debug_log_file_name;
				String fname_bak = current_path + log_file_path + debug_log_file_name + ".bak";
					
				//
				File fd = new File( fname );
				if( fd.length() > file_size ){
					File fd2 = new File( fname_bak );
					if( fd2.exists() ){
						fd2.delete();
					}
					fd.renameTo(fd2);
					fileWriter = new FileWriter( fname , false );
				} else {
					fileWriter = new FileWriter( fname , true );
				}
				for( int i = 0 ; i < LogQueue.log_queue_4.size() ; i++ ){
					LogMessage temp = LogQueue.log_queue_4.get(i);
					fileWriter.write( temp.toString() + "\r\n" );
				}
					
			}catch( IOException e ){
				StringWriter writer = new StringWriter();
				PrintWriter printerWriter = new PrintWriter( writer );
				e.printStackTrace( printerWriter );
				printerWriter.flush();
			}
				
			if( fileWriter != null ){
				try{
					fileWriter.close();
				} catch ( IOException e ){
					e.printStackTrace();
				}
			}
			LogQueue.log_queue_4.clear();
		}
	}
	
	
	
	public void TaskRun() {
		while( true ) {
			
			//System.out.println("Flush...");
			// 將 queue(LOG_SYSTEM) 內容寫入檔案後 ， 清除[ log_queue_0 ] 的 queue
			FlushSystemLogToFile();
			// 將 queue(LOG_ASSCESS) 內容寫入檔案後 ， 清除[ log_queue_1 ] 的 queue
			FlushAccessLogToFile();
			// 將 queue(LOG_DB) 內容寫入檔案後 ， 清除[ log_queue_2 ] 的 queue
			FlushDatabaseLogToFile();
			// 將 queue(LOG_ADAPTER) 內容寫入檔案後 ， 清除[ log_queue_3 ] 的 queue
			FlushAdapterLogToFile();
			// 將 queue(LOG_DEBUG) 內容寫入檔案後 ， 清除[ log_queue_4 ] 的 queue
			FlushDebugLogToFile();
			
			// Delay 1 s (每隔一秒做一次)
			try {
				Thread.sleep(interval_time);
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}//while_END
	}
	
}
