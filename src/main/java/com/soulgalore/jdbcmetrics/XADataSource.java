package com.soulgalore.jdbcmetrics;

import java.sql.SQLException;

import javax.sql.XAConnection;

public class XADataSource extends AbstractCommonDataSource<javax.sql.XADataSource> implements javax.sql.XADataSource {
	
	public XADataSource() {
		super();
	}
	
	public XADataSource(javax.sql.XADataSource dataSource) {
		super(dataSource);
	}

	// jndi lookup as "java:/comp/env/jdbc/testDS" or "jdbc/testDS"
	public XADataSource(String referenceName) {
		super(referenceName);
	}	

	@Override
	public XAConnection getXAConnection() throws SQLException {
		XAConnection connection = getDataSource().getXAConnection();
		if (connection != null) {
			return proxyFactory.xaConnectionProxy(connection);
		}
		return null;
	}

	@Override
	public XAConnection getXAConnection(String user, String password)
			throws SQLException {
		XAConnection connection = getDataSource().getXAConnection(user, password);
		if (connection != null) {
			return proxyFactory.xaConnectionProxy(connection);
		}
		return null;
	}
}
