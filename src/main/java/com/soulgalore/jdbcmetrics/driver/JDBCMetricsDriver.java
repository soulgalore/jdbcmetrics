package com.soulgalore.jdbcmetrics.driver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JDBCMetricsDriver implements Driver {

	private static Pattern JDBCMETRICS_IN_URL_PATTERN = Pattern.compile("jdbc\\:(jdbcmetrics(?:\\?driver\\=((?:[\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*))?\\:).*");
	private Map<String, Driver> cachedDrivers = new ConcurrentHashMap<String, Driver>();
	private ProxyFactory proxyFactory = new ProxyFactory();
	
	static {
		try {
			DriverManager.registerDriver(new JDBCMetricsDriver());
		} catch (SQLException e) {
			throw new RuntimeException("JDBCMetrics could not register driver", e);
		}
	}
	
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		Matcher m = JDBCMETRICS_IN_URL_PATTERN.matcher(url);
		return m.matches();
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		final String cleanUrl = cleanUrl(url);
		Connection connection = getDriver(url, cleanUrl).connect(cleanUrl, info);
		if (connection != null) {
			return proxyFactory.connectionProxy(connection);
		} else {
			return null;
		}
	}

	@Override
	public int getMajorVersion() {
		return 0;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		final String cleanUrl = cleanUrl(url);
		return getDriver(url, cleanUrl).getPropertyInfo(cleanUrl, info);
	}

	@Override
	public boolean jdbcCompliant() {
		return false;
	}
	
	//@Override in java7
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}

	protected Driver getDriver(String url, String cleanUrl) throws SQLException {
		Driver driver = cachedDrivers.get(url);
		if (driver == null) {
			final String className = getSpecifiedDriverClassName(url);
			if (className != null) {
				driver = getDriverByClassName(className);
			} else {
				driver = getDriverFromDriverManager(cleanUrl);
			}
			if (driver != null) {
				cachedDrivers.put(url, driver);
			}
		}
		return driver;
	}

	protected Driver getDriverFromDriverManager(String cleanUrl) throws SQLException {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			if (driver.acceptsURL(cleanUrl)) {
				return driver;
			}
		}
		return null;
	}

	protected Driver getDriverByClassName(String className) {
		try {
			Class<?> c = Class.forName(className);
			Object o = c.newInstance();
			if (o instanceof Driver) {
				return (Driver) o;
			} else {
				return null;
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("JDBCMetrics could not find driver class", e);
		} catch (InstantiationException e) {
			throw new RuntimeException("JDBCMetrics could not instantiate driver class", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("JDBCMetrics could not instantiate driver class", e);
		}
	}

	protected String getSpecifiedDriverClassName(String url) {
		Matcher m = JDBCMETRICS_IN_URL_PATTERN.matcher(url);
		if (m.find()) {
			return m.group(2);
		}
		return null;
	}
	
	protected String cleanUrl(String url) {
		Matcher m = JDBCMETRICS_IN_URL_PATTERN.matcher(url);
		if (m.find()) {
			return url.replace(m.group(1), "");
		}
		return url;
	}
	
	
}
