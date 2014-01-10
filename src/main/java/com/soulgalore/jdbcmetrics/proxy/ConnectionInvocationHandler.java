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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ConnectionInvocationHandler implements InvocationHandler {

  private final Connection connection;
  private final ProxyFactory proxyFactory;

  public ConnectionInvocationHandler(Connection connection, ProxyFactory proxyFactory) {
    super();
    this.connection = connection;
    this.proxyFactory = proxyFactory;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object o;
    try {
      o = method.invoke(connection, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    if ("createStatement".equals(method.getName())) {
      o = proxyFactory.statementProxy((Statement) o);
    } else if ("prepareStatement".equals(method.getName())) {
      o = proxyFactory.preparedStatementProxy((PreparedStatement) o, args[0].toString());
    } else if ("prepareCall".equals(method.getName())) {
      o = proxyFactory.callableStatementProxy((CallableStatement) o, args[0].toString());
    }
    return o;
  }

}
