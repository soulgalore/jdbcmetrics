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

import javax.sql.XAConnection;

public class XADataSource extends AbstractCommonDataSource<javax.sql.XADataSource>
    implements
      javax.sql.XADataSource {

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
  public XAConnection getXAConnection(String user, String password) throws SQLException {
    XAConnection connection = getDataSource().getXAConnection(user, password);
    if (connection != null) {
      return proxyFactory.xaConnectionProxy(connection);
    }
    return null;
  }
}
