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
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soulgalore.jdbcmetrics.JDBCMetrics;
import com.soulgalore.jdbcmetrics.QueryThreadLocal;

public class StatementInvocationHandler implements InvocationHandler {

  private static final String METHOD_NAME_EXECUTE_QUERY = "executeQuery";
  private static final String METHOD_NAME_EXECUTE_UPDATE = "executeUpdate";
  private static final String METHOD_NAME_EXECUTE = "execute";
  private static final String METHOD_NAME_ADD_BATCH = "addBatch";
  private static final String METHOD_NAME_CLEAR_BATCH = "clearBatch";
  private static final String METHOD_NAME_EXECUTE_BATCH = "executeBatch";

  private final Statement statement;
  private final String sql;

  private final Logger logger = LoggerFactory.getLogger(StatementInvocationHandler.class);

  private long nrOfBatchReads = 0;
  private long nrOfBatchWrites = 0;

  public StatementInvocationHandler(Statement statement, String sql) {
    super();
    this.statement = statement;
    this.sql = sql;
  }

  public StatementInvocationHandler(Statement statement) {
    this(statement, null);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    boolean isTouched = true;

    // TODO we only need to time the onces that executes a query
    // this adds a little overhead
    final long start = System.nanoTime();
    Object o;
    try {
      o = method.invoke(statement, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
    final long time = System.nanoTime() - start;

    if (METHOD_NAME_EXECUTE_QUERY.equals(method.getName())) {
      readStats(1, time);
    } else if (METHOD_NAME_EXECUTE_UPDATE.equals(method.getName())) {
      writeStats(1, time);
    } else if (METHOD_NAME_EXECUTE.equals(method.getName())) {
      incStats(args != null ? args[0].toString() : sql, time);
    } else if (METHOD_NAME_ADD_BATCH.equals(method.getName())) {
      if (isRead(args != null ? args[0].toString() : sql)) {
        nrOfBatchReads++;
      } else {
        nrOfBatchWrites++;
      }
    } else if (METHOD_NAME_CLEAR_BATCH.equals(method.getName())) {
      nrOfBatchReads = 0;
      nrOfBatchWrites = 0;
    } else if (METHOD_NAME_EXECUTE_BATCH.equals(method.getName())) {
      readStats(nrOfBatchReads, time);
      writeStats(nrOfBatchWrites, time);
      nrOfBatchReads = 0;
      nrOfBatchWrites = 0;
    } else {
      isTouched = false;
    }

    if (logger.isDebugEnabled() && isTouched) {
      logger.debug(method.getName() + " "
          + (args != null ? args[0].toString() : (sql != null ? sql : "")) + " " + time + " ns");
    }

    return o;
  }

  void incStats(String sql, long time) {
    if (isRead(sql)) {
      readStats(1, time);
    } else {
      writeStats(1, time);
    }
  }

  void readStats(long inc, long time) {
    if (inc == 0) {
      return;
    }
    for (long i = inc; i > 0; i--) {
      QueryThreadLocal.addRead(time / inc);
    }
    JDBCMetrics.getInstance().getReadTimer().update(time / inc, TimeUnit.NANOSECONDS);

    // TODO how should we handle slow queries? or is the timing enough?
  }

  void writeStats(long inc, long time) {
    if (inc == 0) {
      return;
    }
    for (long i = inc; i > 0; i--) {
      QueryThreadLocal.addWrite(time / inc);
    }
    JDBCMetrics.getInstance().getWriteTimer().update(time / inc, TimeUnit.NANOSECONDS);
  }

  static boolean isRead(String sql) {
    return (sql.toLowerCase().trim().startsWith("select"));
  }
}
