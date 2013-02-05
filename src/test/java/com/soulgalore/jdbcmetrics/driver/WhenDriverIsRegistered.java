package com.soulgalore.jdbcmetrics.driver;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class WhenDriverIsRegistered extends AbstractDriverTest {

	@Test
	public void driverShouldBeInDriverManager() {
		List<Driver> driversInManager = Collections.list(DriverManager.getDrivers());
		assertThat(driversInManager, hasItem(isA(JDBCMetricsDriver.class)));
		assertThat(underlayingDriver, isIn(driversInManager));
	}

	@Test
	public void driverShouldBeLocatedByKnownUrl() throws SQLException {
		Driver d = DriverManager.getDriver(URL_JDBC_METRICS);
		assertThat("JDBCMetricsDriver should be registered in DriverManager and understand url", d, notNullValue());
		assertThat(d, instanceOf(JDBCMetricsDriver.class));
	}
	
	@Test(expected=SQLException.class)
	public void driverShouldNotBeLocatedByUnknownUrl() throws SQLException {
		DriverManager.getDriver(URL_UNKNOWN);
		// Should throw, no suitable driver
	}

	@Test
	public void knownUrlShouldBeCleaned() {
		assertThat("The url should be cleaned from jdbcmetrics", driver.cleanUrl(URL_JDBC_METRICS), equalTo(URL_KNOWN_DRIVER));
	}
	
	@Test
	public void unknownUrlShouldNotBeCleaned() {
		assertThat("The url should be intact", driver.cleanUrl(URL_UNKNOWN), equalTo(URL_UNKNOWN));
	}

	@Test
	public void underlayingDriverShouldExist() throws SQLException {
		assertThat("Should be the underlaying driver", driver.getDriver(URL_JDBC_METRICS), sameInstance(underlayingDriver));
	}

}
