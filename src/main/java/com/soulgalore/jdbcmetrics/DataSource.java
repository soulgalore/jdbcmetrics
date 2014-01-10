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
import java.sql.SQLException;

public class DataSource extends AbstractCommonDataSource<javax.sql.DataSource>
    implements
      javax.sql.DataSource {

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
  public Connection getConnection(String username, String password) throws SQLException {
    final Connection connection = getDataSource().getConnection(username, password);
    if (connection != null) {
      return proxyFactory.connectionProxy(connection);
    }
    return null;
  }

}
