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
package com.soulgalore.jdbcmetrics.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import com.soulgalore.jdbcmetrics.JDBCMetrics;
import com.soulgalore.jdbcmetrics.QueryThreadLocal;

public class StatementInvocationHandler implements InvocationHandler {

	private final Statement statement;
	private final String sql;

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
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		// TODO we only need to time the onces that executes a query
		// this adds a little overhead
		final long start = System.nanoTime();
		Object o;
		try {
			o = method.invoke(statement, args);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
		final long time = start - System.nanoTime();

		if ("executeQuery".equals(method.getName())) {
			readStats(1, time);
		} else if ("executeUpdate".equals(method.getName())) {
			writeStats(1, time);
		} else if ("execute".equals(method.getName())) {
			incStats(args != null ? args[0].toString() : sql, time);
		} else if ("addBatch".equals(method.getName())) {
			if (isRead(args[0].toString())) {
				nrOfBatchReads++;
			} else {
				nrOfBatchWrites++;
			}
		} else if ("clearBatch".equals(method.getName())) {
			nrOfBatchReads = 0;
			nrOfBatchWrites = 0;
		} else if ("executeBatch".equals(method.getName())) {
			readStats(nrOfBatchReads, time);
			writeStats(nrOfBatchWrites, time);
			nrOfBatchReads = 0;
			nrOfBatchWrites = 0;
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
			QueryThreadLocal.addRead();
		}
		JDBCMetrics.getInstance().getReadTimer()
				.update(time / inc, TimeUnit.NANOSECONDS);
		JDBCMetrics.getInstance().getTotalNumberOfReads().inc(inc);
		JDBCMetrics.getInstance().getReadMeter().mark();
		
		// TODO how should we handle slow queries? or is the timing enough?
	}

	void writeStats(long inc, long time) {
		if (inc == 0) {
			return;
		}
		for (long i = inc; i > 0; i--) {
			QueryThreadLocal.addWrite();
		}
		JDBCMetrics.getInstance().getWriteTimer()
				.update(time / inc, TimeUnit.NANOSECONDS);
		JDBCMetrics.getInstance().getTotalNumberOfWrites().inc(inc);
		JDBCMetrics.getInstance().getWriteMeter().mark();
	}

	static boolean isRead(String sql) {
		return (sql.toLowerCase().trim().startsWith("select"));
	}
}
