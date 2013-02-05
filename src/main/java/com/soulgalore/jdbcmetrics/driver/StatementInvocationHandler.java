package com.soulgalore.jdbcmetrics.driver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Statement;

import com.soulgalore.jdbcmetrics.JDBCMetrics;
import com.soulgalore.jdbcmetrics.QueryThreadLocal;

public class StatementInvocationHandler implements InvocationHandler {

	private Statement statement;
	
	private long nrOfBatchReads = 0;
	private long nrOfBatchWrites = 0;

	public StatementInvocationHandler(Statement statement) {
		super();
		this.statement = statement;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object o = method.invoke(statement, args);
		if ("executeQuery".equals(method.getName())) {
			readStats(1);
		}
		else if ("executeUpdate".equals(method.getName())) {
			writeStats(1);
		}
		else if ("execute".equals(method.getName())) {
			if (isRead(args[0].toString())) {
				readStats(1);
			} else {
				writeStats(1);
			}
		}
		else if ("addBatch".equals(method.getName())) {
			if (isRead(args[0].toString())) {
				nrOfBatchReads++;
			} else {
				nrOfBatchWrites++;
			}
		}
		else if ("clearBatch".equals(method.getName())) {
			nrOfBatchReads = 0;
			nrOfBatchWrites = 0;	
		}
		else if ("executeBatch".equals(method.getName())) {
			readStats(nrOfBatchReads);
			writeStats(nrOfBatchWrites);
			nrOfBatchReads = 0;
			nrOfBatchWrites = 0;
		}
		return o;
	}

	void readStats(long inc) {
		for (long i=inc; i>0; i--) {
			QueryThreadLocal.addRead();
		}
		JDBCMetrics.getInstance().getTotalNumberOfReads().inc(inc);
		JDBCMetrics.getInstance().getReadMeter().mark();
	}
	
	void writeStats(long inc) {
		for (long i=inc; i>0; i--) {
			QueryThreadLocal.addWrite();
		}
		JDBCMetrics.getInstance().getTotalNumberOfWrites().inc(inc);
		JDBCMetrics.getInstance().getWriteMeter().mark();
	}
	
	static boolean isRead(String sql) {
		return (sql.toLowerCase().trim().startsWith("select"));
	}
}
