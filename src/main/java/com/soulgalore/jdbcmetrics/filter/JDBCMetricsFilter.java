package com.soulgalore.jdbcmetrics.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.soulgalore.jdbcmetrics.QueryThreadLocal;
import com.soulgalore.jdbcmetrics.ReadAndWrites;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

public class JDBCMetricsFilter implements Filter {

	private final String GROUP = "jdbc";
	private final String TYPE_READ = "read";
	private final String TYPE_WRITE = "write";

	final MetricsRegistry registry = new MetricsRegistry();
	final Counter totalNumberOfReads = registry.newCounter(new MetricName(
			GROUP, TYPE_READ, "total-of-reads"));

	final Counter totalNumberOfWrites = Metrics.newCounter(new MetricName(
			GROUP, TYPE_WRITE, "total-of-writes"));

	final Histogram readCountsPerPage = Metrics.newHistogram(new MetricName(
			GROUP, TYPE_READ, "read-counts-per-page"));

	final Histogram writeCountsPerPage = Metrics.newHistogram(new MetricName(
			GROUP, TYPE_READ, "write-counts-per-page"));

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		try {
			chain.doFilter(req, resp);
		} finally {
			ReadAndWrites rw = QueryThreadLocal.getNrOfQueries();
			if (rw != null) {
				totalNumberOfReads.inc(rw.getReads());
				totalNumberOfWrites.inc(rw.getWrites());
				readCountsPerPage.update(rw.getReads());
				writeCountsPerPage.update(rw.getWrites());
			}
			QueryThreadLocal.removeNrOfQueries();
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
