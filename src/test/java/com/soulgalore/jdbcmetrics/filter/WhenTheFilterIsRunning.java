package com.soulgalore.jdbcmetrics.filter;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WhenTheFilterIsRunning {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void anEmptyInitParameterShouldFallbackToDefault() {
		FilterConfig config = Mockito.mock(FilterConfig.class);
		Mockito.when(
				config.getInitParameter(JDBCMetricsFilter.REQUEST_HEADER_NAME_INIT_PARAM_NAME))
				.thenReturn("");
		JDBCMetricsFilter filter = new JDBCMetricsFilter();
		try {
			try {
				filter.init(config);
				assertThat(filter.requestHeaderName,
						is(JDBCMetricsFilter.DEFAULT_REQUEST_HEADER_NAME));
			} catch (ServletException e) {
				fail();
			}
		} finally {
			filter.destroy();
		}
	}
}
