package com.soulgalore.jdbcmetrics;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource extends AbstractCommonDataSource<javax.sql.DataSource> implements javax.sql.DataSource {
	
	public DataSource() {
		super();
	}
	
	public DataSource(javax.sql.DataSource dataSource) {
		super(dataSource);
	}

	// jndi lookup as "java:/comp/env/jdbc/testDS" or "jdbc/testDS"
	public DataSource(String referenceName) {
		super(referenceName);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getDataSource().unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getDataSource().isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		Connection connection = getDataSource().getConnection();
		if (connection != null) {
			return proxyFactory.connectionProxy(connection);
		}
		return null;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection connection = getDataSource().getConnection(username, password);
		if (connection != null) {
			return proxyFactory.connectionProxy(connection);
		}
		return null;
	}

}
