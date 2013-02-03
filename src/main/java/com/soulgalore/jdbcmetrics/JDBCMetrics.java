package com.soulgalore.jdbcmetrics;

import java.util.concurrent.TimeUnit;

import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

/**
 * Class responsible for holding all the Yammer Metrics.
 *
 */
public class JDBCMetrics {

	private final static String GROUP = "jdbc";
	private final static String TYPE_READ = "read";
	private final static String TYPE_WRITE = "write";
	
	private final MetricsRegistry registry = new MetricsRegistry();
	
	private final Counter totalNumberOfReads = registry.newCounter(new MetricName(
			GROUP, TYPE_READ, "total-of-reads"));

	private final Counter totalNumberOfWrites = registry.newCounter(new MetricName(
			GROUP, TYPE_WRITE, "total-of-writes"));

	private final Histogram readCountsPerRequest = registry.newHistogram(new MetricName(
			GROUP, TYPE_READ, "read-counts-per-request"), true);

	private final Histogram writeCountsPerRequest = registry.newHistogram(new MetricName(
			GROUP, TYPE_WRITE, "write-counts-per-request"), true);

	private final Meter readMeter = registry.newMeter(new MetricName(GROUP, TYPE_READ,
			"reads"), "jdbcread", TimeUnit.SECONDS);

	private final Meter writeMeter = registry.newMeter(new MetricName(GROUP,
			TYPE_WRITE, "writes"), "jdbcwrite", TimeUnit.SECONDS);
	
	private final Timer readTimer = registry.newTimer(new MetricName(GROUP, TYPE_READ,
			"read-timer"), TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

	private final Timer writeTimer = registry.newTimer(new MetricName(GROUP, TYPE_READ,
			"write-timer"), TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
	
	private static final JDBCMetrics INSTANCE = new JDBCMetrics();

	
	private JDBCMetrics() {
	}

	/**
	 * Get the instance.
	 * 
	 * @return the singleton instance.
	 */
	public static JDBCMetrics getInstance() {
		return INSTANCE;
	}

	public Counter getTotalNumberOfReads() {
		return totalNumberOfReads;
	}

	public Counter getTotalNumberOfWrites() {
		return totalNumberOfWrites;
	}

	public Histogram getReadCountsPerRequest() {
		return readCountsPerRequest;
	}

	public Histogram getWriteCountsPerRequest() {
		return writeCountsPerRequest;
	}

	public Meter getReadMeter() {
		return readMeter;
	}

	public Meter getWriteMeter() {
		return writeMeter;
	}
	
	public Timer getWriteTimer() {
		return writeTimer;
	}
	
	public Timer getReadTimer() {
		return readTimer;
	}
	
}
