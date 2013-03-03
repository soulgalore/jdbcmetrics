package com.soulgalore.jdbcmetrics;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;

import com.soulgalore.jdbcmetrics.proxy.ProxyFactory;

public class DataSource implements /*XADataSource,*/ javax.sql.DataSource, /*ConnectionPoolDataSource,*/ Serializable, Referenceable {

	private transient ProxyFactory proxyFactory = new ProxyFactory();
	private transient javax.sql.DataSource dataSource;
//	private transient ConnectionPoolDataSource poolDataSource;
//	private transient XADataSource xaDataSource;
	private transient AtomicBoolean initialized = new AtomicBoolean();
	private String referenceName;
	
	public DataSource() {
		
	}
	
	public DataSource(javax.sql.DataSource dataSource) {
		initFromDataSource(dataSource);
		initialized.set(true);
	}

	// jndi lookup as "java:/comp/env/jdbc/testDS" or "jdbc/testDS"
	public DataSource(String referenceName) {
		this.referenceName = referenceName;
	}
	
	private void initFromDataSource(javax.sql.DataSource dataSource) {
		this.dataSource = dataSource;
//		if (dataSource instanceof ConnectionPoolDataSource) {
//			poolDataSource = (ConnectionPoolDataSource) dataSource;
//		}
//		if (dataSource instanceof XADataSource) {
//			xaDataSource = (XADataSource) dataSource;
//		}
	}

	private void initFromReferenceName() {
		if (referenceName != null && !referenceName.startsWith("java:")) {
			referenceName = "java:comp/env/" + referenceName;
		}
		try {
			Context ctx = new InitialContext();
			javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup(referenceName);
			initFromDataSource(ds);
		} catch (NamingException e) {
			throw new RuntimeException("Lookup of datasource failed", e);
		}
	}
	
	private void checkInit() {
		if (!initialized.get()) {
			synchronized (initialized) {
				if (!initialized.get()) {
					if (referenceName != null) {
						initFromReferenceName();
					} else if (dataSource != null){
						initFromDataSource(dataSource);
					} else {
						//TODO: anything else to do?
						throw new IllegalStateException("No reference or data source set");
					}
					initialized.set(true);
				}
			}
		}
	}
	
	private javax.sql.DataSource getDataSource() {
		checkInit();
		return dataSource;
	}
	
//	private ConnectionPoolDataSource getPooledDataSource() {
//		checkInit();
//		if (poolDataSource == null) {
//			throw new IllegalStateException("Can't call getPooledConnection() when proxied datasource is not a ConnectionPoolDataSource");
//		}
//		return poolDataSource;
//	}
//
//	private XADataSource getXADataSource() {
//		checkInit();
//		if (xaDataSource == null) {
//			throw new IllegalStateException("Can't call getXAConnection() when proxied datasource is not a XADataSource");
//		}
//		return xaDataSource;
//	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
		initialized.set(false);
	}
	
//	public void setDataSource(DataSource dataSource) {
//		this.dataSource = dataSource;
//		initialized.set(false);
//	}
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return getDataSource().getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		getDataSource().setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		getDataSource().setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return getDataSource().getLoginTimeout();
	}

	//@Override in java7
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
		//return getDataSource().getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getDataSource().unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getDataSource().isWrapperFor(iface);
	}

//	@Override
//	public PooledConnection getPooledConnection() throws SQLException {
//		PooledConnection connection = getPooledDataSource().getPooledConnection();
//		if (connection != null) {
//			return proxyFactory.pooledConnectionProxy(connection);
//		}
//		return null;
//	}
//
//	@Override
//	public PooledConnection getPooledConnection(String user, String password)
//			throws SQLException {
//		PooledConnection connection = getPooledDataSource().getPooledConnection(user, password);
//		if (connection != null) {
//			return proxyFactory.pooledConnectionProxy(connection);
//		}
//		return null;
//	}

	@Override
	public Connection getConnection() throws SQLException {
		Connection connection = getDataSource().getConnection();
		if (connection != null) {
			System.out.println("Getting con " + connection);
			return proxyFactory.connectionProxy(connection);
		}
		return null;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection connection = getDataSource().getConnection(username, password);
		if (connection != null) {
			System.out.println("Getting con " + connection);
			return proxyFactory.connectionProxy(connection);
		}
		return null;
	}

//	@Override
//	public XAConnection getXAConnection() throws SQLException {
//		if(xaDataSource ==null) return null;
//		
//		XAConnection connection = getXADataSource().getXAConnection();
//		if (connection != null) {
//			return proxyFactory.xaConnectionProxy(connection);
//		}
//		return null;
//	}
//
//	@Override
//	public XAConnection getXAConnection(String user, String password)
//			throws SQLException {
//		if(xaDataSource ==null) return null;
//		
//		XAConnection connection = getXADataSource().getXAConnection(user, password);
//		if (connection != null) {
//			return proxyFactory.xaConnectionProxy(connection);
//		}
//		return null;
//	}

	@Override
	public Reference getReference() throws NamingException {
		String factoryName = DataSourceFactory.class.getName(); 
		Reference ref = new Reference(getClass().getName(), factoryName, null);
		ref.add(new StringRefAddr("referenceName", referenceName));
		//TODO: maybe more than just name ...
		return ref;
	}
}
