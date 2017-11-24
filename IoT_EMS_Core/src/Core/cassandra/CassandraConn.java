package Core.cassandra;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Session.State;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;

import Core.main.Main;
import Core.util.Configuration;

// Cassandra 連線
public class CassandraConn {
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Private members
	// -------------------------------------------------------------------------------------------------------------------------- //
	private static int CONNECTION_HEARTBEAT_INTERVAL = 60;
	private static int CONNECTION_CORE               = 20;		
	private static int CONNECTION_MAX                = 30;
	private static int CONNECTION_TIME_OUT           = 60;		// 60秒
	private static int CONNECTION_PORT               = 9042;
	// 以上是Cassandra的設定
	
	private static String CONNECTION_ACC             = "";
	private static String CONNECTION_PWD             = "";
	private static String CONTACT_POINTS             = "127.0.0.1,";
	private static Session session                   = null;	// 電腦之間成功建立的一條連線稱之session
	private static Cluster cluster                   = null;	// 雲的前身 有聚集之意  也可以分散各地利用網路連接 (cluster的特性 可以同時有很多台  只要給與一點時間  可以達成每一台有同步的效果 )
	private static PoolingOptions poolingOptions     = null;	
	private static CassandraConn instance            = null;
	
	private CassandraConn() {
		try{
			loadConnConfig();
			poolingOptions = new PoolingOptions();
			poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL, CONNECTION_CORE);
			poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, CONNECTION_MAX);
			poolingOptions.setCoreConnectionsPerHost(HostDistance.REMOTE, CONNECTION_CORE);
			poolingOptions.setMaxConnectionsPerHost(HostDistance.REMOTE, CONNECTION_MAX);
			poolingOptions.setHeartbeatIntervalSeconds(CONNECTION_HEARTBEAT_INTERVAL);
			poolingOptions.setIdleTimeoutSeconds(CONNECTION_TIME_OUT);
			
			cluster = Cluster.builder()
					 		 .addContactPoints(convertIPAddr(CONTACT_POINTS))
					 		 .withPort(CONNECTION_PORT)
					 		 .withCredentials(CONNECTION_ACC, CONNECTION_PWD)
					 		 .withPoolingOptions(poolingOptions)
					 		 .withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.QUORUM))
					 		 .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
					 		 .build();
			session = cluster.connect();
			monitoringPool();
		}catch(UnknownHostException e){
			e.printStackTrace();
			Main.log_handler.writeDebugLog( "CassandraConn::Creat Cluster(" + CONTACT_POINTS + ") Exception: " + e.toString() );
		}
	}
	
	private void loadConnConfig() {
		try {
			String config_path = ".." + File.separator + "conf" + File.separator + "System.ini";
			String path = URLDecoder.decode( CassandraConn.class.getClassLoader().getResource("").getPath() , "UTF-8" );
			//System.out.println("path : " + CassandraConn.class.getClassLoader().getResource("").getPath() );
			if( new File( path + config_path ).exists() ) {
				Configuration cf = new Configuration( path + config_path );
				CONNECTION_CORE= cf.getPropertyAsInt("CAS_CONNECTION_CORE", CONNECTION_CORE);
				CONNECTION_MAX = cf.getPropertyAsInt("CAS_CONNECTION_MAX", CONNECTION_MAX);
				CONNECTION_TIME_OUT = cf.getPropertyAsInt("CAS_CONNECTION_TIME_OUT", CONNECTION_MAX);
				CONNECTION_ACC = cf.getPropertyAsString("CAS_CONNECTION_ACC", CONNECTION_ACC);
				CONNECTION_PWD = cf.getPropertyAsString("CAS_CONNECTION_PWD", CONNECTION_PWD);
				CONNECTION_PORT = cf.getPropertyAsInt("CAS_CONNECTION_PORT", CONNECTION_PORT);
				CONTACT_POINTS = cf.getPropertyAsString("CAS_CONTACT_POINTS", CONTACT_POINTS);
			} else {
				Main.log_handler.writeSystemLog("file not found! : " + CassandraConn.class.getProtectionDomain().getCodeSource().getLocation().getPath() );
			}
			
			Main.log_handler.writeSystemLog( "CassandraConn.loadConnConfig() : CONNECTION_CORE - " +  CONNECTION_CORE );
			Main.log_handler.writeSystemLog( "CassandraConn.loadConnConfig() : CONNECTION_MAX - " +  CONNECTION_CORE );
			Main.log_handler.writeSystemLog( "CassandraConn.loadConnConfig() : CONNECTION_TIME_OUT - " +  CONNECTION_CORE );
			Main.log_handler.writeSystemLog( "CassandraConn.loadConnConfig() : CONNECTION_ACC - " +  CONNECTION_ACC );
			Main.log_handler.writeSystemLog( "CassandraConn.loadConnConfig() : CONNECTION_PWD - " +  CONNECTION_PWD );
			Main.log_handler.writeSystemLog( "CassandraConn.loadConnConfig() : CONNECTION_PORT - " +  CONNECTION_PORT );
			Main.log_handler.writeSystemLog( "CassandraConn.loadConnConfig() : CONTACT_POINTS - " +  CONTACT_POINTS );
			
		} catch( IOException e ) {
			 e.printStackTrace();
			 Main.log_handler.writeDebugLog( "CassandraConn.loadConnConfig():" + e.toString() );
		}
	}
	
	private void monitoringPool(){
		long initialDelay = 60, period = 60;
		ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
		scheduled.scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
				State state = session.getState();
				LoadBalancingPolicy loadBalancingPolicy = cluster.getConfiguration().getPolicies().getLoadBalancingPolicy();
				 for( Host host : state.getConnectedHosts() ) {
					 int connections = state.getOpenConnections(host);
					 int inFlightQueries = state.getInFlightQueries(host);
					 int maxLoad = connections * poolingOptions.getMaxRequestsPerConnection(loadBalancingPolicy.distance(host));
					 Main.log_handler.writeSystemLog(String.format("%s connections=%d, current load=%d, max load=%d%n", host, connections, inFlightQueries, maxLoad));
				 }
		    }}, initialDelay, period, TimeUnit.SECONDS);
	}
	
	private List<InetAddress> convertIPAddr(String ipStr) throws UnknownHostException {
		List<InetAddress> nodes = new ArrayList<InetAddress>();
		String [] ipArray = ipStr.split(",");
		for(int i = 0; i < ipArray.length; i++){
			nodes.add(InetAddress.getByName(ipArray[i]));
		}
		return nodes;
	}
	
	/*
	private static class SingletonHolder{
		private static CassandraConn instance = new CassandraConn();
	}
	*/
	
	// -------------------------------------------------------------------------------------------------------------------------- //
	// Public members
	// -------------------------------------------------------------------------------------------------------------------------- //
	public static CassandraConn getInstance() {
		if( instance == null ) {
			instance = new CassandraConn();
		}
		return instance;
	}
	
	public int getMaxConnectionNum() {
		return CONNECTION_MAX;
	}
	
	public Session getSession() throws NullPointerException {
		if( session == null ) {
			throw new NullPointerException( "CassandraConn.getSession(): session is null." );
		}
		return session;
	}
	
}
