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

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.CommonDataSource;

import com.soulgalore.jdbcmetrics.proxy.ProxyFactory;

public abstract class AbstractCommonDataSource<T extends CommonDataSource>
    implements
      CommonDataSource,
      Referenceable,
      Serializable {

  protected final transient ProxyFactory proxyFactory = new ProxyFactory();
  private final transient AtomicBoolean initialized = new AtomicBoolean();
  private transient T dataSource;

  private String referenceName;

  public AbstractCommonDataSource() {}

  public AbstractCommonDataSource(T dataSource) {
    this.dataSource = dataSource;
    initialized.set(true);
  }

  // jndi lookup as "java:/comp/env/jdbc/testDS" or "jdbc/testDS"
  public AbstractCommonDataSource(String referenceName) {
    this.referenceName = referenceName;
  }

  private void initFromReferenceName() {
    if (referenceName != null && !referenceName.startsWith("java:")) {
      referenceName = "java:comp/env/" + referenceName;
    }
    try {
      Context ctx = new InitialContext();
      T ds = (T) ctx.lookup(referenceName);
      this.dataSource = ds;
    } catch (NamingException e) {
      throw new RuntimeException("Lookup of datasource failed", e);
    }
  }

  private void checkInit() {
    if (!initialized.get()) {
      synchronized (initialized) {
        if (!initialized.get()) {
          if (referenceName != null) {
            initFromReferenceName();
          } else if (dataSource == null) {
            throw new IllegalStateException("No reference or data source set");
          }
          initialized.set(true);
        }
      }
    }
  }

  public void setReferenceName(String referenceName) {
    this.referenceName = referenceName;
    initialized.set(false);
  }

  public void setDataSource(T dataSource) {
    this.dataSource = dataSource;
    initialized.set(false);
  }

  protected T getDataSource() {
    checkInit();
    return dataSource;
  }

  @Override
  public Reference getReference() throws NamingException {
    String factoryName = DataSourceFactory.class.getName();
    Reference ref = new Reference(getClass().getName(), factoryName, null);
    ref.add(new StringRefAddr("referenceName", referenceName));
    // TODO: maybe more than just name here ...
    return ref;
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return getDataSource().getLogWriter();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    getDataSource().setLogWriter(out);
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    getDataSource().setLoginTimeout(seconds);
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return getDataSource().getLoginTimeout();
  }

  // @Override in java7
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException();
    // return getDataSource().getParentLogger();
  }

}
