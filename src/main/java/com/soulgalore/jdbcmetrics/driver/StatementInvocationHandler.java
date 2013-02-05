package com.soulgalore.jdbcmetrics.driver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import com.soulgalore.jdbcmetrics.JDBCMetrics;
import com.soulgalore.jdbcmetrics.QueryThreadLocal;

public class StatementInvocationHandler implements InvocationHandler {

	private Statement statement;
	private String sql;

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
		long start = System.nanoTime();
		Object o = method.invoke(statement, args);
		long time = start - System.nanoTime();

		if ("executeQuery".equals(method.getName())) {
			readStats(1, time);
		} else if ("executeUpdate".equals(method.getName())) {
			writeStats(1, time);
		} else if ("execute".equals(method.getName())) {
			if (args == null) {
				incStats(sql, time);
			} else {
				incStats(args[0].toString(), time);
			}
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
			if (nrOfBatchReads > 0)
				readStats(nrOfBatchReads, time);
			if (nrOfBatchWrites > 0)
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
		for (long i = inc; i > 0; i--) {
			QueryThreadLocal.addRead();
		}
		JDBCMetrics.getInstance().getReadTimer()
				.update(time / inc, TimeUnit.NANOSECONDS);
		JDBCMetrics.getInstance().getTotalNumberOfReads().inc(inc);
		JDBCMetrics.getInstance().getReadMeter().mark();
	}

	void writeStats(long inc, long time) {
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
