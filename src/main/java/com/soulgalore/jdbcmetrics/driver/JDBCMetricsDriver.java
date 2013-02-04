package com.soulgalore.jdbcmetrics.driver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public class JDBCMetricsDriver implements Driver {

	static {
		try {
			DriverManager.registerDriver(new JDBCMetricsDriver());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith("jdbc:jdbcmetrics:");
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		//return new ConnectionWrapper(getDriver(url).connect(cleanUrl(url), info));
		ProxyFactory pf = new ProxyFactory();
		return pf.connectionProxy(getDriver(url).connect(cleanUrl(url), info));
	}

	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		return getDriver(cleanUrl(url)).getPropertyInfo(url, info);
	}

	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//Override in java7
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}

	protected Driver getDriver(String url) throws SQLException {
		//TODO: "cache" the driver? on url?
		if (acceptsURL(url)) {
			url = cleanUrl(url);
			Enumeration<Driver> drivers = DriverManager.getDrivers();
			while (drivers.hasMoreElements()) {
				Driver d = drivers.nextElement();
				if (d.acceptsURL(url)) {
					return d;
				}
			}
		}
		return null;
	}

	protected String cleanUrl(String url) {
		return url.replaceFirst("\\:jdbcmetrics", "");
	}
	
	
}
