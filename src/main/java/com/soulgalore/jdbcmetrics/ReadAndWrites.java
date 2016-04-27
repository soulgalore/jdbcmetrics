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

/**
 * Hold read &amp; writes per request. The class is not thread safe but is used within a ThreadLocal.
 * 
 */
public class ReadAndWrites {

  private int reads = 0;
  private int writes = 0;
  private long totalReadTime = 0;
  private long totalWriteTime = 0;

  public ReadAndWrites() {}

  public void incReads(long readTime) {
    reads++;
    totalReadTime += readTime;
  }

  public void incWrites(long writeTime) {
    writes++;
    totalWriteTime += writeTime;
  }

  public int getWrites() {
    return writes;
  }

  public int getReads() {
    return reads;
  }

  public long getTotalReadTime() {
    return totalReadTime;
  }

  public long getTotalWriteTime() {
    return totalWriteTime;
  }

  public void clear() {
    reads = 0;
    writes = 0;
    totalReadTime = 0;
    totalWriteTime = 0;
  }

}
