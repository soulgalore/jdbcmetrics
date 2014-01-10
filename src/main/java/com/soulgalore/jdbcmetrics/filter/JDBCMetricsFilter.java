/******************************************************
 * JDBCMetrics
 * 
 * 
 * Copyright (C) 2013 by Magnus Lundberg (http://magnuslundberg.com) & Peter Hedenskog
 * (http://peterhedenskog.com)
 * 
 ****************************************************** 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 ******************************************************* 
 */
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soulgalore.jdbcmetrics.JDBCMetrics;
import com.soulgalore.jdbcmetrics.QueryThreadLocal;
import com.soulgalore.jdbcmetrics.ReadAndWrites;

/**
 * Filter that will make JDBCMetrics collect JDBC metrics per request. Set it up like this:
 * 
 * <pre>
 * &lt;filter&gt;
 *  &lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
 * &lt;filter-class&gt;
 * 	com.soulgalore.jdbcmetrics.filter.JDBCMetricsFilter
 * &lt;/filter-class&gt;
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;request-header-name&lt;/param-name&gt;
 * 	&lt;param-value&gt;query-statistics&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;/filter&gt;
 * 
 * &lt;filter-mapping&gt;
 * &lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
 * &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 * 
 */
public class JDBCMetricsFilter implements Filter {

  static final String REQUEST_HEADER_NAME_INIT_PARAM_NAME = "request-header-name";
  static final String USE_HEADERS_INIT_PARAM_NAME = "use-headers";
  static final String DEFAULT_REQUEST_HEADER_NAME = "jdbcmetrics";
  static final String RESPONSE_HEADER_NAME_NR_OF_READS = "nr-of-reads";
  static final String RESPONSE_HEADER_NAME_NR_OF_WRITES = "nr-of-writes";
  static final String RESPONSE_HEADER_NAME_TIME_SPENT_IN_READS = "read-time";
  static final String RESPONSE_HEADER_NAME_TIME_SPENT_IN_WRITES = "write-time";

  private final Logger logger = LoggerFactory.getLogger(JDBCMetricsFilter.class);

  private static final String LOG_URL_TEXT = "URL: ";
  private static final String LOG_URL_QUERY_TEXT = "?";
  private static final String LOG_READS = " reads:";
  private static final String LOG_WRITES = " writes:";
  private static final String LOG_READ_TIME = " readTime:";
  private static final String LOG_WRITE_TIME = " writeTime:";
  private static final String LOG_MS = " ms";

  private static final long NANOS_TO_MILLIS = 1000000;

  protected String requestHeaderName;
  private boolean useHeadersInConfig;


  @Override
  public void destroy() {}

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {

    // run once per thread
    if (QueryThreadLocal.getNrOfQueries() == null) {
      QueryThreadLocal.init();


      // setup the response wrapper ONLY if we set the response headers
      boolean useHeaders = useHeaders((HttpServletRequest) req);
      ResponseWrapper responseWrapper = null;
      if (useHeaders) responseWrapper = new ResponseWrapper((HttpServletResponse) resp);

      try {
        chain.doFilter(req, useHeaders ? responseWrapper : resp);
      } finally {

        // set the stats & cleanup
        ReadAndWrites rw = QueryThreadLocal.getNrOfQueries();
        updateStatistics(rw);
        if (useHeaders) setHeaders(rw, responseWrapper);
        log(req, rw);
        QueryThreadLocal.removeNrOfQueries();
        if (useHeaders) responseWrapper.write();
      }
    }
  }

  @Override
  public void init(FilterConfig config) throws ServletException {

    requestHeaderName = config.getInitParameter(REQUEST_HEADER_NAME_INIT_PARAM_NAME);
    if (requestHeaderName == null || "".equals(requestHeaderName))
      requestHeaderName = DEFAULT_REQUEST_HEADER_NAME;
    useHeadersInConfig = Boolean.valueOf((config.getInitParameter(USE_HEADERS_INIT_PARAM_NAME)));
  }

  private boolean useHeaders(HttpServletRequest request) {
    if (!useHeadersInConfig)
      return false;
    else if (request.getHeader(requestHeaderName) != null)
      return true;
    else
      return false;
  }

  private void log(ServletRequest req, ReadAndWrites rw) {
    if (logger.isDebugEnabled() && (rw.getReads() > 0 || rw.getWrites() > 0)) {
      StringBuilder builder = new StringBuilder(LOG_URL_TEXT);
      HttpServletRequest request = (HttpServletRequest) req;
      builder.append(request.getRequestURL());
      if (request.getQueryString() != null)
        builder.append(LOG_URL_QUERY_TEXT).append(request.getQueryString());
      builder.append(LOG_READS).append(rw.getReads()).append(LOG_WRITES).append(rw.getWrites());
      builder.append(LOG_READ_TIME).append(rw.getTotalReadTime() / NANOS_TO_MILLIS).append(LOG_MS)
          .append(LOG_WRITE_TIME).append(rw.getTotalWriteTime() / NANOS_TO_MILLIS).append(LOG_MS);
      logger.debug(builder.toString());
    }
  }

  private void updateStatistics(ReadAndWrites rw) {
    JDBCMetrics metrics = JDBCMetrics.getInstance();
    metrics.getReadCountsPerRequest().update(rw.getReads());
    metrics.getWriteCountsPerRequest().update(rw.getWrites());
    metrics.getReadTimerPerRequest().update(rw.getTotalReadTime(), TimeUnit.NANOSECONDS);
    metrics.getWriteTimerPerRequest().update(rw.getTotalWriteTime(), TimeUnit.NANOSECONDS);
  }

  private void setHeaders(ReadAndWrites rw, HttpServletResponse response) {

    response.setHeader(RESPONSE_HEADER_NAME_NR_OF_READS, String.valueOf(rw.getReads()));
    response.setHeader(RESPONSE_HEADER_NAME_NR_OF_WRITES, String.valueOf(rw.getWrites()));
    // report in millis
    response.setHeader(RESPONSE_HEADER_NAME_TIME_SPENT_IN_READS,
        String.valueOf(rw.getTotalReadTime() / NANOS_TO_MILLIS));
    response.setHeader(RESPONSE_HEADER_NAME_TIME_SPENT_IN_WRITES,
        String.valueOf(rw.getTotalWriteTime() / NANOS_TO_MILLIS));
  }
}
