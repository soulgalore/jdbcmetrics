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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class DataSourceFactory implements ObjectFactory {

  public static final String REFERENCE_NAME = "referenceName";
  public static final String CLASS_NAME = "className";

  @Override
  public Object getObjectInstance(Object obj, Name name, Context nameCtx,
      Hashtable<?, ?> environment) throws Exception {

    Reference reference = (Reference) obj;

    String resourceName = getRefString(reference, REFERENCE_NAME);
    String className = getRefString(reference, CLASS_NAME);

    if (XADataSource.class.getName().equals(className)) {
      return new XADataSource(resourceName);
    } else if (ConnectionPoolDataSource.class.getName().equals(className)) {
      return new ConnectionPoolDataSource(resourceName);
    }
    return new DataSource(resourceName);
  }

  protected String getRefString(Reference reference, String name) {
    RefAddr refAddr = reference.get(name);
    if (refAddr != null) {
      String s = (String) refAddr.getContent();
      if (s != null) {
        return s;
      }
    }
    return null;
  }
}
