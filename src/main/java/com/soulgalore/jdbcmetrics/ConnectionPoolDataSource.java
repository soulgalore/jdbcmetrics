package com.soulgalore.jdbcmetrics;

import java.sql.SQLException;

import javax.sql.PooledConnection;


public class ConnectionPoolDataSource extends AbstractCommonDataSource<javax.sql.ConnectionPoolDataSource>
		implements javax.sql.ConnectionPoolDataSource {

	public ConnectionPoolDataSource() {
		super();
	}
	
	public ConnectionPoolDataSource(javax.sql.ConnectionPoolDataSource dataSource) {
		super(dataSource);
	}

	// jndi lookup as "java:/comp/env/jdbc/testDS" or "jdbc/testDS"
	public ConnectionPoolDataSource(String referenceName) {
		super(referenceName);
	}
	
	@Override
	public PooledConnection getPooledConnection() throws SQLException {
		PooledConnection connection = getDataSource().getPooledConnection();
		if (connection != null) {
			return proxyFactory.pooledConnectionProxy(connection);
		}
		return null;
	}

	@Override
	public PooledConnection getPooledConnection(String user, String password)
			throws SQLException {
		PooledConnection connection = getDataSource().getPooledConnection(user, password);
		if (connection != null) {
			return proxyFactory.pooledConnectionProxy(connection);
		}
		return null;
	}
}
