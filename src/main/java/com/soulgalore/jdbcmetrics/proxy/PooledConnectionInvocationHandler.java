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
import java.sql.Connection;

import javax.sql.PooledConnection;

public class PooledConnectionInvocationHandler implements InvocationHandler {

  private final PooledConnection connection;
  private final ProxyFactory proxyFactory;

  public PooledConnectionInvocationHandler(PooledConnection connection, ProxyFactory proxyFactory) {
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

    if ("getConnection".equals(method.getName())) {
      o = proxyFactory.connectionProxy((Connection) o);
    }
    return o;
  }

}
