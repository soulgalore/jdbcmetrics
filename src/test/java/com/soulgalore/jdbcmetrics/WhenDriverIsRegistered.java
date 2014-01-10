package com.soulgalore.jdbcmetrics;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.soulgalore.jdbcmetrics.Driver;
import com.soulgalore.jdbcmetrics.proxy.AbstractDriverTest;

public class WhenDriverIsRegistered extends AbstractDriverTest {

  @Test
  public void driverShouldBeInDriverManager() {
    List<java.sql.Driver> driversInManager = Collections.list(DriverManager.getDrivers());
    boolean foundJDBCMetricsDriver = false;
    for (java.sql.Driver driver : driversInManager) {
      if (driver instanceof Driver) {
        foundJDBCMetricsDriver = true;
        break;
      }
    }
    assertThat("JDBCMetricsDriver should be registered in DriverManager", foundJDBCMetricsDriver);
    assertThat(underlayingDriver, isIn(driversInManager));
  }

  @Test
  public void driverShouldBeLocatedByKnownUrl() throws SQLException {
    java.sql.Driver d = DriverManager.getDriver(URL_JDBC_METRICS);
    assertThat("JDBCMetricsDriver should be registered in DriverManager and understand url", d,
        notNullValue());
    assertThat(d, instanceOf(Driver.class));
  }

  @Test(expected = SQLException.class)
  public void driverShouldNotBeLocatedByUnknownUrl() throws SQLException {
    DriverManager.getDriver(URL_UNKNOWN);
    // Should throw, no suitable driver
  }

  @Test
  public void knownUrlShouldBeCleaned() {
    assertThat("The url should be cleaned from jdbcmetrics", driver.cleanUrl(URL_JDBC_METRICS),
        equalTo(URL_KNOWN_DRIVER));
  }

  @Test
  public void knownUrlWithDriverShouldBeCleaned() {
    assertThat("The url should be cleaned from jdbcmetrics",
        driver.cleanUrl(URL_JDBC_METRICS_SPECIFIED_DRIVER), equalTo(URL_KNOWN_DRIVER));
  }

  @Test
  public void unknownUrlShouldNotBeCleaned() {
    assertThat("The url should be intact", driver.cleanUrl(URL_UNKNOWN), equalTo(URL_UNKNOWN));
  }

  @Test
  public void underlayingDriverShouldExistInDriverManager() throws SQLException {
    assertThat("Should be the underlaying driver",
        driver.getDriverFromDriverManager(driver.cleanUrl(URL_KNOWN_DRIVER)),
        sameInstance(underlayingDriver));
  }

  @Test
  public void underlayingDriverShouldExist() throws SQLException {
    assertThat("Should be the underlaying driver",
        driver.getDriver(URL_JDBC_METRICS, URL_KNOWN_DRIVER), sameInstance(underlayingDriver));
  }

  @Test
  public void specifiedDriverClassNameShouldBeParsedFromUrl() {
    assertThat(driver.getSpecifiedDriverClassName(URL_JDBC_METRICS_SPECIFIED_DRIVER),
        equalTo("org.postgresql.Driver"));
  }

  @Test
  public void specifiedDriverClassNameShouldNotBeFoundInUrl() {
    assertThat(driver.getSpecifiedDriverClassName(URL_JDBC_METRICS), nullValue());
  }

  @Test(expected = RuntimeException.class)
  public void driverByNonExistingClassNameShouldThrow() {
    driver.getDriverByClassName("non.existing.Class");
  }

  @Test(expected = RuntimeException.class)
  public void driverByClassNameNotImplementingDriverShouldThrow() {
    driver.getDriverByClassName(String.class.getName());
  }

  @Test
  public void driverByClassNameShouldReturn() {
    java.sql.Driver d = driver.getDriverByClassName(Driver.class.getName());
    assertThat(d, notNullValue());
    assertThat("Should be jdbcmetrics driver", d.getClass().equals(Driver.class));
  }

  @Test
  public void shouldAcceptUrl() throws SQLException {
    assertThat(driver.acceptsURL(URL_JDBC_METRICS), is(true));
    assertThat(driver.acceptsURL(URL_JDBC_METRICS_SPECIFIED_DRIVER), is(true));
  }

  @Test
  public void shouldNotAcceptUrl() throws SQLException {
    assertThat(driver.acceptsURL(URL_KNOWN_DRIVER), is(false));
    assertThat(driver.acceptsURL(URL_UNKNOWN), is(false));
  }

}
