package com.soulgalore.jdbcmetrics.driver;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import org.junit.Test;

public class WhenDriverIsRegistered extends AbstractDriverTest {

	@Test
	public void driverShouldBeInDriverManager() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver d = drivers.nextElement();
			if (d instanceof JDBCMetricsDriver) {
				return;
			}
		}
		fail("JDBCMetricsDriver should be registered in DriverManager");
	}

	@Test
	public void driverShouldBeLocatedByKnownUrl() throws SQLException {
		Driver d = DriverManager.getDriver(URL_JDBC_METRICS);
		assertNotNull("JDBCMetricsDriver should be registered in DriverManager and understand url", d);
		assertThat(d, instanceOf(JDBCMetricsDriver.class));
	}
	
	@Test
	public void driverShouldNotBeLocatedByUnknownUrl() throws SQLException {
		try {
			DriverManager.getDriver(URL_UNKNOWN);
			fail("Should throw, no suitable driver");
		} catch (SQLException e) {
		}
	}

	@Test
	public void knownUrlShouldBeCleaned() {
		assertEquals("The url should be cleaned from jdbcmetrics", URL_KNOWN_DRIVER, driver.cleanUrl(URL_JDBC_METRICS));
	}
	
	@Test
	public void unknownUrlShouldNotBeCleaned() {
		assertEquals("The url should be intact", URL_UNKNOWN, driver.cleanUrl(URL_UNKNOWN));
	}

	@Test
	public void underlayingDriverShouldExist() throws SQLException {
		assertSame("Should be the underlaying driver", underlayingDriver, driver.getDriver(URL_JDBC_METRICS));
	}

}
