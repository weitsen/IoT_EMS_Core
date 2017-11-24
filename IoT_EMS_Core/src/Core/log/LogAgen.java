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
		
	
	// �]�w�ɶ��M��
	// [1]
	public void FlushSystemLogToFile() {		
		synchronized( LogQueue.log_queue_0 ) {
			// �S�����e
			if( LogQueue.log_queue_0.size() == 0 ) {
				return;
			}

			FileWriter fileWriter = null;
			// IO ���n try catch
			try {
				// ��s�X
				//String current_path = URLDecoder.decode( LogAgen.class.getClassLoader().getResource("").getPath() , "UTF-8" );
				String current_path = "";
				// �g�ɪ����|+�ɦW
				String fname = current_path + log_file_path + system_log_file_name;
				String fname_bak = current_path + log_file_path + system_log_file_name + ".bak";
				
				// �����|�N�ܪ���  
				File fd = new File(fname);
				// �p�G�ɮפj�p  �W�L �]�w�e�q�j�p  �N��swap [��Ĥ@���ɧ�W���ĤG���ɡA�p�G�ĤG���ɤ��O�Ū��A�N���M���ĤG����]  �û��g�Ĥ@����
				// �ت� : �H����g�J���ɮפj�p�Ӥj �y�� �O���� �z�� ( �ҥH���]�w�@�� File Size ����e )
				if( fd.length() > file_size ) {
					File fd2 = new File(fname_bak);
					// �s�b�N�R��
					if( fd2.exists() ) {
						fd2.delete();
					}
					// ���ɦW(�Ĥ@���ɦW �令 �ĤG���ɦW)
					fd.renameTo(fd2);
					// false ���ɴN���Ω��U�g�F(�]���w�g�ǳƶ}�s����)
					fileWriter = new FileWriter( fname , false );
				} else {
					// true ���ɥi�H�~��g
					fileWriter = new FileWriter( fname , true );
				}
				
				for( int i = 0 ; i < LogQueue.log_queue_0.size() ; i++ ) {
					LogMessage temp = LogQueue.log_queue_0.get( i );
					// \r\n ����Ÿ�(\r Windows)(\n Linux)
					// �g��
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
					// ����
					fileWriter.close();
				} catch( IOException e ) {
					e.printStackTrace();
				}
			}
			// �M�� log_queue_0
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
			// �N queue(LOG_SYSTEM) ���e�g�J�ɮ׫� �A �M��[ log_queue_0 ] �� queue
			FlushSystemLogToFile();
			// �N queue(LOG_ASSCESS) ���e�g�J�ɮ׫� �A �M��[ log_queue_1 ] �� queue
			FlushAccessLogToFile();
			// �N queue(LOG_DB) ���e�g�J�ɮ׫� �A �M��[ log_queue_2 ] �� queue
			FlushDatabaseLogToFile();
			// �N queue(LOG_ADAPTER) ���e�g�J�ɮ׫� �A �M��[ log_queue_3 ] �� queue
			FlushAdapterLogToFile();
			// �N queue(LOG_DEBUG) ���e�g�J�ɮ׫� �A �M��[ log_queue_4 ] �� queue
			FlushDebugLogToFile();
			
			// Delay 1 s (�C�j�@���@��)
			try {
				Thread.sleep(interval_time);
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}//while_END
	}
	
}
