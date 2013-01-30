package com.soulgalore.jdbcmetrics.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;

public class ExampleFilter implements Filter {

	final Meter meterRequests = Metrics.newMeter(ExampleFilter.class, "requests",
			"requests", TimeUnit.SECONDS);
	final Timer requestTimer = Metrics.newTimer(ExampleFilter.class, "request-time",
			TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

	@Override
	public void destroy() {
		meterRequests.stop();
		requestTimer.stop();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		meterRequests.mark();
		final TimerContext context = requestTimer.time();
		try {
			chain.doFilter(req, resp);
		} finally {
			context.stop();
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
