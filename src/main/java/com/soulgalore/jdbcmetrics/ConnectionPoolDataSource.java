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

import java.sql.SQLException;

import javax.sql.PooledConnection;

import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

public class ConnectionPoolDataSource
    extends AbstractCommonDataSource<javax.sql.ConnectionPoolDataSource>
    implements
      javax.sql.ConnectionPoolDataSource {

  private final Timer timer;

  public ConnectionPoolDataSource() {
    super();
    timer = JDBCMetrics.getInstance().getWaitForConnectionInPool();
  }

  public ConnectionPoolDataSource(javax.sql.ConnectionPoolDataSource dataSource) {
    super(dataSource);
    timer = JDBCMetrics.getInstance().getWaitForConnectionInPool();
  }

  // jndi lookup as "java:/comp/env/jdbc/testDS" or "jdbc/testDS"
  public ConnectionPoolDataSource(String referenceName) {
    super(referenceName);
    timer = JDBCMetrics.getInstance().getWaitForConnectionInPool();
  }

  @Override
  public PooledConnection getPooledConnection() throws SQLException {

    final Context context = timer.time();
    try {
      PooledConnection connection = getDataSource().getPooledConnection();
      if (connection != null) {
        return proxyFactory.pooledConnectionProxy(connection);
      }
      return null;
    } finally {
      context.stop();
    }

  }

  @Override
  public PooledConnection getPooledConnection(String user, String password) throws SQLException {
    final Context context = timer.time();
    try {
      PooledConnection connection = getDataSource().getPooledConnection(user, password);
      if (connection != null) {
        return proxyFactory.pooledConnectionProxy(connection);
      }
      return null;
    } finally {
      context.stop();
    }
  }
}
