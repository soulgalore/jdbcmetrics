package com.soulgalore.jdbcmetrics;

import java.sql.SQLException;

import javax.sql.PooledConnection;

import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;


public class ConnectionPoolDataSource extends AbstractCommonDataSource<javax.sql.ConnectionPoolDataSource>
		implements javax.sql.ConnectionPoolDataSource {

	private Timer timer;
	public ConnectionPoolDataSource() {
		super();
		timer = JDBCMetrics.getInstance().getWaitForConnectionInPool();
	}
	
	public ConnectionPoolDataSource(javax.sql.ConnectionPoolDataSource dataSource) {
		super(dataSource);
		timer = JDBCMetrics.getInstance().getWaitForConnectionInPool();
	}

	// jndi lookup as "java:/comp/env/jdbc/testDS" or "jdbc/testDS"
	public ConnectionPoolDataSource(String referenceName) {
		super(referenceName);
		 timer = JDBCMetrics.getInstance().getWaitForConnectionInPool();
	}
	
	@Override
	public PooledConnection getPooledConnection() throws SQLException {
		
		final TimerContext context = timer.time();
		try {
			PooledConnection connection = getDataSource().getPooledConnection();
			if (connection != null) {
				return proxyFactory.pooledConnectionProxy(connection);
			}
			return null;
		} finally {
			context.stop();
		}
		
	}

	@Override
	public PooledConnection getPooledConnection(String user, String password)
			throws SQLException {
		final TimerContext context = timer.time();
		try {
			PooledConnection connection = getDataSource().getPooledConnection(
					user, password);
			if (connection != null) {
				return proxyFactory.pooledConnectionProxy(connection);
			}
			return null;
		} finally {
			context.stop();
		}
	}
}
