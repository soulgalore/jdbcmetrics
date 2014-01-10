package com.soulgalore.jdbcmetrics.filter;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.soulgalore.jdbcmetrics.QueryThreadLocal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WhenTheFilterIsRunning {

  @Before
  public void setUp() throws Exception {
    QueryThreadLocal.removeNrOfQueries();
  }

  @Test
  public void anEmptyInitParameterShouldFallbackToDefault() {
    FilterConfig config = Mockito.mock(FilterConfig.class);
    Mockito.when(config.getInitParameter(JDBCMetricsFilter.REQUEST_HEADER_NAME_INIT_PARAM_NAME))
        .thenReturn("");
    Mockito.when(config.getInitParameter(JDBCMetricsFilter.USE_HEADERS_INIT_PARAM_NAME))
        .thenReturn("true");
    JDBCMetricsFilter filter = new JDBCMetricsFilter();
    try {
      try {
        filter.init(config);
        assertThat("The header name should be the default one", filter.requestHeaderName,
            is(JDBCMetricsFilter.DEFAULT_REQUEST_HEADER_NAME));
      } catch (ServletException e) {
        fail();
      }
    } finally {
      filter.destroy();
    }
  }

  @Test
  public void headersShouldBeSetWhenRightHeaderIsSupplied() throws ServletException, IOException {
    FilterConfig config = Mockito.mock(FilterConfig.class);
    String headerName = "jdbc";
    Mockito.when(config.getInitParameter(JDBCMetricsFilter.REQUEST_HEADER_NAME_INIT_PARAM_NAME))
        .thenReturn(headerName);
    Mockito.when(config.getInitParameter(JDBCMetricsFilter.USE_HEADERS_INIT_PARAM_NAME))
        .thenReturn("true");

    HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
    Mockito.when(req.getHeader(headerName)).thenReturn("yes");
    HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
    ServletOutputStream stream = Mockito.mock(ServletOutputStream.class);
    Mockito.when(resp.getOutputStream()).thenReturn(stream);

    FilterChain chain = Mockito.mock(FilterChain.class);

    JDBCMetricsFilter filter = new JDBCMetricsFilter();
    try {
      filter.init(config);
      filter.doFilter(req, resp, chain);

      verify(resp).setHeader(JDBCMetricsFilter.RESPONSE_HEADER_NAME_NR_OF_READS, "0");
      verify(resp).setHeader(JDBCMetricsFilter.RESPONSE_HEADER_NAME_NR_OF_WRITES, "0");
      verify(resp).setHeader(JDBCMetricsFilter.RESPONSE_HEADER_NAME_TIME_SPENT_IN_READS, "0");
      verify(resp).setHeader(JDBCMetricsFilter.RESPONSE_HEADER_NAME_TIME_SPENT_IN_WRITES, "0");

    } finally {
      filter.destroy();
    }

  }

  @Test
  public void headersShouldNotBeSetWithMissingRequestHeader() throws ServletException, IOException {
    FilterConfig config = Mockito.mock(FilterConfig.class);
    String headerName = "jdbc";
    Mockito.when(config.getInitParameter(JDBCMetricsFilter.REQUEST_HEADER_NAME_INIT_PARAM_NAME))
        .thenReturn(headerName);
    Mockito.when(config.getInitParameter(JDBCMetricsFilter.USE_HEADERS_INIT_PARAM_NAME))
        .thenReturn("true");

    HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
    Mockito.when(req.getHeader(headerName)).thenReturn(null);
    HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
    ServletOutputStream stream = Mockito.mock(ServletOutputStream.class);
    Mockito.when(resp.getOutputStream()).thenReturn(stream);

    FilterChain chain = Mockito.mock(FilterChain.class);

    JDBCMetricsFilter filter = new JDBCMetricsFilter();
    try {
      filter.init(config);
      filter.doFilter(req, resp, chain);
      verify(resp, times(0)).setHeader(anyString(), anyString());

    } finally {
      filter.destroy();
    }

  }

}
