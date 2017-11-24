package Core.cassandra;

import com.datastax.driver.core.Session;

import Core.cassandra.agent.CassandraConnAgent;
import Core.main.Main;

public class CassandraConnManager {
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Private members
	// -------------------------------------------------------------------------------------------------------------------------- //
	private CassandraConnAgent cca = null;
	
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Public members
	// -------------------------------------------------------------------------------------------------------------------------- //
	public CassandraConnManager() {
		
		if( cca == null ) {
			cca = new CassandraConnAgent();
		}
		cca.EnableAgentKernel();
		
		Main.log_handler.writeSystemLog( "Cassandra Connection Manager !" );
	}
	
	public Session getSession() {
		Session ret = null;
		
		int i;
		for( i = 0 ; i < cca.getCurrentSessionSize() ; i++ ) {
			CassandraSession temp = cca.getSession(i);
			if( temp.getState() == CassandraSession.CONNECTION_UNUSED ) {
				temp.setState( CassandraSession.CONNECTION_USED );
				ret = temp.getSession();
				break;
			}
		}
		
		//  在Session Pool裡面找不到可以用的Session, 新增一個Session試試看
		if( i == 0 || i < cca.getMaxSessionSize() ) {
			CassandraSession temp = cca.addNewSession();
			// 無法再新增了(因為可以用Session滿了)
			if( temp == null ) {
				Main.log_handler.writeSystemLog( "Cassandra Connection Manager :: Session is full, can't open new session!" );
			} else {
				temp.setState( CassandraSession.CONNECTION_USED );
				ret = temp.getSession();
			}
		}
		
		Main.log_handler.writeSystemLog( "Cassandra Connection Manager :: getSession - " + ret );
		
		return ret;
	}
	
	public void release( Session se ) {
		Main.log_handler.writeSystemLog( "Cassandra Connection Manager :: release - " + se );

		int i;
		for( i = 0 ; i < cca.getMaxSessionSize() ; i++ ) {
			CassandraSession temp = cca.getSession(i);
			if( temp.getSession() == se ) {
				temp.setState( CassandraSession.CONNECTION_UNUSED );
				Main.log_handler.writeSystemLog( "Cassandra Connection Manager :: Success to release!" );
				break;
			}
		}
		
	}
}
