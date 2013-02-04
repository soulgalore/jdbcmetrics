package com.soulgalore.jdbcmetrics.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.soulgalore.jdbcmetrics.JDBCMetrics;
import com.soulgalore.jdbcmetrics.QueryThreadLocal;
import com.soulgalore.jdbcmetrics.ReadAndWrites;

/**
 * Filter that will make JDBCMetrics collect JDBC metrics per request. Set it up like this:
 * <pre>
 * &lt;filter&gt;
 *  &lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
 *	&lt;filter-class&gt;
 *		com.soulgalore.jdbcmetrics.filter.JDBCMetricsFilter
 *	&lt;/filter-class&gt;
 *	&lt;init-param&gt;
 *		&lt;param-name&gt;request-header-name&lt;/param-name&gt;
 *		&lt;param-value&gt;query-statistics&lt;/param-value&gt;
 *	&lt;/init-param&gt;
 * &lt;/filter&gt;
 *
 * &lt;filter-mapping&gt;
 *	&lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
 *	&lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
</pre>
 *
 */
public class JDBCMetricsFilter implements Filter {

	final static String REQUEST_HEADER_NAME_INIT_PARAM_NAME = "request-header-name";
	final static String DEFAULT_REQUEST_HEADER_NAME = "jdbcmetrics";
	final static String RESPONSE_HEADER_NAME_NR_OF_READS = "nr-of-reads";
	final static String RESPONSE_HEADER_NAME_NR_OF_WRITES = "nr-of-writes";

	String requestHeaderName;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		// run once per thread
		if (QueryThreadLocal.getNrOfQueries() == null) {
			QueryThreadLocal.init();

			ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) resp);	
			
			try {
				chain.doFilter(req, responseWrapper);
			} finally {

				ReadAndWrites rw = QueryThreadLocal.getNrOfQueries();
				updateStatistics(rw);
				setHeaders(rw, req, responseWrapper);
				QueryThreadLocal.removeNrOfQueries();
				responseWrapper.write();
			}
		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
		requestHeaderName = config
				.getInitParameter(REQUEST_HEADER_NAME_INIT_PARAM_NAME);
		if (requestHeaderName == null || "".equals(requestHeaderName))
			requestHeaderName = DEFAULT_REQUEST_HEADER_NAME;
	}

	private void updateStatistics(ReadAndWrites rw) {
		if (rw != null) {
			JDBCMetrics.getInstance().getReadCountsPerRequest()
					.update(rw.getReads());
			JDBCMetrics.getInstance().getWriteCountsPerRequest()
					.update(rw.getWrites());
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
