/******************************************************
 * JDBCMetrics
 * 
 *
 * Copyright (C) 2013 by Magnus Lundberg (http://magnuslundberg.com) & Peter Hedenskog (http://peterhedenskog.com)
 *
 ******************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License. You may obtain a copy of the License at
 * 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is 
 * distributed  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
 * See the License for the specific language governing permissions and limitations under the License.
 *
 *******************************************************
 */
package com.soulgalore.jdbcmetrics;

import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

/**
 * Class responsible for holding all the Yammer Metrics.
 * The default MetricRegistry is used by default, if you want a new to be created 
 * add a System property  
 * <em>com.soulgalore.jdbcmetrics.MetricRegistry</em> with the value <em>new</em>.
 *
 */
public class JDBCMetrics {

	public static final String REGISTRY_PROPERTY_NAME = "com.soulgalore.jdbcmetrics.MetricRegistry";
	
	private static final String GROUP = "jdbc";
	private static final String GROUP_POOL = "connectionpool";
	private static final String TYPE_READ = "read";
	private static final String TYPE_WRITE = "write";
	private static final String TYPE_READ_OR_WRITE = "readorwrite";
	
	private static final String REGISTRY_DEFAULT = "default";
	
	private final MetricsRegistry registry; 
	
	private final Histogram readCountsPerRequest;
	private final Histogram writeCountsPerRequest;

	private final Timer writeTimer;
	private final Timer readTimer;
	private final Timer writeTimerPerRequest;
	private final Timer readTimerPerRequest;
	private final Timer connectionPoolTimer;
	
	private static final JDBCMetrics INSTANCE = new JDBCMetrics();

	
	private JDBCMetrics() {
		
		String propertyValue = System.getProperty(REGISTRY_PROPERTY_NAME,REGISTRY_DEFAULT);
		if (REGISTRY_DEFAULT.equals(propertyValue))
			registry = Metrics.defaultRegistry();
		else
			registry = new MetricsRegistry();
	
		
		readCountsPerRequest = registry.newHistogram(new MetricName(
				GROUP, TYPE_READ, "read-counts-per-request"), true);
		
		 writeCountsPerRequest = registry.newHistogram(new MetricName(
					GROUP, TYPE_WRITE, "write-counts-per-request"), true);
		 
		 writeTimer = registry.newTimer(new MetricName(GROUP,
					TYPE_WRITE, "write-time"), TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
		 
		 readTimer = registry.newTimer(new MetricName(GROUP,
					TYPE_READ, "read-time"), TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
		 
		 writeTimerPerRequest = registry.newTimer(new MetricName(GROUP,
					TYPE_WRITE, "write-time-per-request"), TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
		 
		 readTimerPerRequest = registry.newTimer(new MetricName(GROUP,
					TYPE_READ, "read-time-per-request"), TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
		 
		 
		 connectionPoolTimer = registry.newTimer(new MetricName(GROUP_POOL,
				 TYPE_READ_OR_WRITE, "wait-for-connection"), TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
	}

	/**
	 * Get the instance.
	 * 
	 * @return the singleton instance.
	 */
	public static JDBCMetrics getInstance() {
		return INSTANCE;
	}


	public Histogram getReadCountsPerRequest() {
		return readCountsPerRequest;
	}

	public Histogram getWriteCountsPerRequest() {
		return writeCountsPerRequest;
	}
	
	public Timer getWriteTimer() {
		return writeTimer;
	}
	
	public Timer getReadTimer() {
		return readTimer;
	}
	
	public Timer getWaitForConnectionInPool() {
		return connectionPoolTimer;
	}
	
	public MetricsRegistry getRegistry() {
		return registry;
	}

	public Timer getWriteTimerPerRequest() {
		return writeTimerPerRequest;
	}

	public Timer getReadTimerPerRequest() {
		return readTimerPerRequest;
	}

}
