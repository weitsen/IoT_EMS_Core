package Core.cassandra;
import com.datastax.driver.core.Session;

public class CassandraSession {
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Private members
	// -------------------------------------------------------------------------------------------------------------------------- //
	private Session cassandra_session;
	private int connection_state;
	
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Public members
	// -------------------------------------------------------------------------------------------------------------------------- //
	public static final int CONNECTION_UNUSED = 0;
	public static final int CONNECTION_USED   = 1;
	
	public CassandraSession( Session se , int state ) {
		cassandra_session = se;
		connection_state = state;
	}
	
	public Session getSession() {
		return cassandra_session;
	}
	
	public void setState( int state ) {
		connection_state = state;
	}
	
	public int getState() {
		return connection_state;
	}
}
