/******************************************************
 * JDBCMetrics
 * 
 * 
 * Copyright (C) 2013 by Magnus Lundberg (http://magnuslundberg.com) & Peter Hedenskog
 * (http://peterhedenskog.com)
 * 
 ****************************************************** 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 ******************************************************* 
 */
package com.soulgalore.jdbcmetrics;

import java.sql.Connection;
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

import com.soulgalore.jdbcmetrics.proxy.ProxyFactory;

public class Driver implements java.sql.Driver {

  private static final Pattern JDBCMETRICS_IN_URL_PATTERN =
      Pattern
          .compile("jdbc\\:(jdbcmetrics(?:\\?driver\\=((?:[\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*))?\\:).*");
  private final Map<String, java.sql.Driver> cachedDrivers =
      new ConcurrentHashMap<String, java.sql.Driver>();
  private final ProxyFactory proxyFactory = new ProxyFactory();

  static {
    try {
      DriverManager.registerDriver(new Driver());
    } catch (SQLException e) {
      throw new RuntimeException("JDBCMetrics could not register driver", e);
    }
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {
    if (url == null) {
      return false;
    }
    Matcher m = JDBCMETRICS_IN_URL_PATTERN.matcher(url);
    return m.matches();
  }

  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    final String cleanUrl = cleanUrl(url);
    java.sql.Driver driver = getDriver(url, cleanUrl);
    if (driver != null) {
      Connection connection = driver.connect(cleanUrl, info);
      if (connection != null) {
        return proxyFactory.connectionProxy(connection);
      }
    }
    return null;
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
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    final String cleanUrl = cleanUrl(url);
    java.sql.Driver driver = getDriver(url, cleanUrl);
    if (driver != null) {
      return driver.getPropertyInfo(cleanUrl, info);
    }
    return null;
  }

  @Override
  public boolean jdbcCompliant() {
    return false;
  }

  // @Override in java7
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException();
  }

  protected java.sql.Driver getDriver(String url, String cleanUrl) throws SQLException {
    java.sql.Driver driver = cachedDrivers.get(url);
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

  protected java.sql.Driver getDriverFromDriverManager(String cleanUrl) throws SQLException {
    Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
    while (drivers.hasMoreElements()) {
      java.sql.Driver driver = drivers.nextElement();
      if (driver.acceptsURL(cleanUrl)) {
        return driver;
      }
    }
    return null;
  }

  protected java.sql.Driver getDriverByClassName(String className) {
    try {
      Class<?> c = Class.forName(className);
      Object o = c.newInstance();
      if (o instanceof java.sql.Driver) {
        return (java.sql.Driver) o;
      } else {
        throw new RuntimeException("JDBCMetrics could cast " + className + " to java.sql.Driver");
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
