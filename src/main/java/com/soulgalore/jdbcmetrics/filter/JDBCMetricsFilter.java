package com.soulgalore.jdbcmetrics.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.soulgalore.jdbcmetrics.QueryThreadLocal;
import com.soulgalore.jdbcmetrics.ReadAndWrites;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

public class JDBCMetricsFilter implements Filter {

	private final static String GROUP = "jdbc";
	private final static String TYPE_READ = "read";
	private final static String TYPE_WRITE = "write";

	private final static String REQUEST_HEADER_NAME = "request-header-name";
	private final static String RESPONSE_HEADER_NAME_NR_OF_READS = "nr-of-reads";
	private final static String RESPONSE_HEADER_NAME_NR_OF_WRITES = "nr-of-writes";

	private String requestHeaderName;

	final MetricsRegistry registry = new MetricsRegistry();
	final Counter totalNumberOfReads = registry.newCounter(new MetricName(
			GROUP, TYPE_READ, "total-of-reads"));

	final Counter totalNumberOfWrites = registry.newCounter(new MetricName(
			GROUP, TYPE_WRITE, "total-of-writes"));

	final Histogram readCountsPerPage = registry.newHistogram(new MetricName(
			GROUP, TYPE_READ, "read-counts-per-request"), true);

	final Histogram writeCountsPerPage = registry.newHistogram(new MetricName(
			GROUP, TYPE_WRITE, "write-counts-per-request"), true);

	final Meter readMeter = registry.newMeter(new MetricName(GROUP, TYPE_READ,
			"reads"), "jdbcread", TimeUnit.SECONDS);

	final Meter writeMeter = registry.newMeter(new MetricName(GROUP,
			TYPE_WRITE, "writes"), "jdbcwrite", TimeUnit.SECONDS);

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		// run once
		if (QueryThreadLocal.getNrOfQueries() == null) {
			QueryThreadLocal.setMeters(readMeter, writeMeter);

			try {
				chain.doFilter(req, resp);
			} finally {

				ReadAndWrites rw = QueryThreadLocal.getNrOfQueries();
				updateStatistics(rw);
				setHeaders(rw, req, resp);
				QueryThreadLocal.removeNrOfQueries();
			}
		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		requestHeaderName = config.getInitParameter(REQUEST_HEADER_NAME);
	}

	private void updateStatistics(ReadAndWrites rw) {
		if (rw != null) {
			totalNumberOfReads.inc(rw.getReads());
			totalNumberOfWrites.inc(rw.getWrites());
			readCountsPerPage.update(rw.getReads());
			writeCountsPerPage.update(rw.getWrites());
		}
	}

	private void setHeaders(ReadAndWrites rw, ServletRequest req,
			ServletResponse resp) {
		if (rw != null) {
			HttpServletRequest request = (HttpServletRequest) req;
			if (requestHeaderName != null
					&& request.getHeader(requestHeaderName) != null) {
				HttpServletResponse response = (HttpServletResponse) resp;
				response.setHeader(RESPONSE_HEADER_NAME_NR_OF_READS,
						String.valueOf(rw.getReads()));
				response.setHeader(RESPONSE_HEADER_NAME_NR_OF_WRITES,
						String.valueOf(rw.getWrites()));
			}
		}
	}

}
