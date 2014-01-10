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
package com.soulgalore.jdbcmetrics.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.PooledConnection;
import javax.sql.XAConnection;

public class ProxyFactory {

  public Connection connectionProxy(Connection connection) {
    return createProxy(Connection.class, new ConnectionInvocationHandler(connection, this));
  }

  public PooledConnection pooledConnectionProxy(PooledConnection connection) {
    return createProxy(PooledConnection.class, new PooledConnectionInvocationHandler(connection,
        this));
  }

  public XAConnection xaConnectionProxy(XAConnection connection) {
    return createProxy(XAConnection.class, new PooledConnectionInvocationHandler(connection, this));
  }

  public Statement statementProxy(Statement statement) {
    return createProxy(Statement.class, new StatementInvocationHandler(statement));
  }

  public PreparedStatement preparedStatementProxy(PreparedStatement preparedStatement, String sql) {
    return createProxy(PreparedStatement.class, new StatementInvocationHandler(preparedStatement,
        sql));
  }

  public CallableStatement callableStatementProxy(CallableStatement callableStatement, String sql) {
    return createProxy(CallableStatement.class, new StatementInvocationHandler(callableStatement,
        sql));
  }

  private <T> T createProxy(Class<T> clazz, InvocationHandler handler) {
    return clazz.cast(Proxy.newProxyInstance(handler.getClass().getClassLoader(),
        new Class[] {clazz}, handler));
  }

}
