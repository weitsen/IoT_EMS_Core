package Core.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Configuration {
	// ----------------------------------------------------------------------------------------------------------------- //
	// private members
	// ----------------------------------------------------------------------------------------------------------------- //	
	private Properties properties = null;
	
	private String filePath = "";
	
	private Properties getInstance() {
		if( properties == null ) {
			properties = new Properties();
			try {
				InputStream is = Configuration.class.getClassLoader().getResourceAsStream(filePath);
				if( is != null ) {
					properties.load(is);
				} else {
					properties.load( new BufferedReader( new InputStreamReader( new FileInputStream(filePath), "UTF8") ) );
				}
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	// ----------------------------------------------------------------------------------------------------------------- //
	// public members
	// ----------------------------------------------------------------------------------------------------------------- //
	public final static long   DEFAULT_LONG_VALUE = 0;
	public final static int    DEFAULT_INT_VALUE = 0;
	public final static String DEFAULT_STRING_VALUE = "";
	
	public Configuration( String path ) {
		setProFilePath( path );
	}
	
	public void setProFilePath(String path) {
		filePath = path;
	}
	
	public String getPropertyAsString(String key) {
		if( getInstance() != null ) {
			String temp = getInstance().getProperty(key);
			return temp == null ? DEFAULT_STRING_VALUE : temp;
		}
		return DEFAULT_STRING_VALUE;
	}
	
	public int getPropertyAsInt(String key) {
		if( getInstance() != null ) {
			String temp = getInstance().getProperty(key);
			if( temp != null ) {
				int buf = DEFAULT_INT_VALUE;
				try {
					buf = Integer.parseInt( temp );
				} catch( NumberFormatException e ) {
					buf = DEFAULT_INT_VALUE;
				}
				return buf;
			} else {
				return DEFAULT_INT_VALUE;
			}
		}
		return DEFAULT_INT_VALUE;
	}
	
	public long getPropertyAsLong(String key) {
		if( getInstance() != null ) {
			String temp = getInstance().getProperty(key);
			if( temp != null ) {
				long buf = DEFAULT_LONG_VALUE;
				try {
					buf = Long.parseLong( temp );
				} catch( NumberFormatException e ) {
					buf = DEFAULT_LONG_VALUE;
				}
				return buf;
			} else {
				return DEFAULT_LONG_VALUE;
			}
		}
		return DEFAULT_LONG_VALUE;
	}
	
	public String getPropertyAsString(String key, String value){
		if (getInstance() != null) {
			return getInstance().getProperty(key, value);
		}else{
			return value;
		}
	}
	
	public int getPropertyAsInt(String key, int value){
		if (getInstance() != null) {
			return Integer.parseInt( getInstance().getProperty(key, String.valueOf(value)) );
		}else{
			return value;
		}
	}
	
	public long getPropertyAsLong(String key, long value){
		if (getInstance() != null) {
			return Long.parseLong( getInstance().getProperty(key, String.valueOf(value)) );
		}else{
			return value;
		}
	}
	
}
