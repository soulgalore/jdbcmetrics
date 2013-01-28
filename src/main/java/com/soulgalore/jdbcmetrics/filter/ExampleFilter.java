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

	Meter getRequests;
	Timer timer;

	@Override
	public void destroy() {
		getRequests.stop();
		timer.stop();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		getRequests.mark();
		final TimerContext context = timer.time();
		try {
			chain.doFilter(req, resp);
		} finally {
			context.stop();
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		getRequests = Metrics.newMeter(ExampleFilter.class, "get-requests",
				"requests", TimeUnit.SECONDS);
		timer = Metrics.newTimer(ExampleFilter.class, "get-requests-timer",
				TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

	}

}
