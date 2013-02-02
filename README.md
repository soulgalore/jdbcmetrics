# JDBCMetrics - get information from your JDBC driver

Using JDBCMetrics you can get hold of the following information from your driver

Setting up the MetricsDriver:
<ul>
<li>The total number of database reads</li>
<li>The total number of database writes</li>
<li>Number of reads per second (per minute, 5 minutes & 15 minutes)</li>
<li>Number of writes per second (per minute, 5 minutes & 15 minutes)</li>
</ul>
Using the web filter:
<ul>
<li>The number of database reads created for a specific HTTP request</li>
<li>The number of database writes created for a specific HTTP request</li>
<li>Statistics about reads per request (average, median, percentile etc)</li>
<li>Statistics about writes per request (average, median, percentile etc)</li>
</ul>


## How to setup
<ol><li>Jack in JDBCMetrics like this:</li>
<li>Add the filter in your *web.xml* file (make sure it run early in the chain):
	<pre>
&lt;filter&gt;
	&lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
	&lt;filter-class&gt;
		com.soulgalore.jdbcmetrics.filter.JDBCMetricsFilter
	&lt;/filter-class&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;request-header-name&lt;/param-name&gt;
		&lt;param-value&gt;query-statistics&lt;/param-value&gt;
	&lt;/init-param&gt;
&lt;/filter&gt;

&lt;filter-mapping&gt;
	&lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
	&lt;url-pattern&gt;/*&lt;/url-pattern&gt;
&lt;/filter-mapping&gt;
	</pre>
</li>
<li>Setup the result servlet:
<pre>
&lt;servlet&gt;
	&lt;servlet-name&gt;MetricsServlet&lt;/servlet-name&gt;
	&lt;servlet-class&gt;com.yammer.metrics.reporting.MetricsServlet&lt;/servlet-class&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;show-jvm-metrics&lt;/param-name&gt;
		&lt;param-value&gt;false&lt;/param-value&gt;
	&lt;/init-param&gt;
&lt;/servlet&gt;

&lt;servlet-mapping&gt;
	&lt;servlet-name&gt;MetricsServlet&lt;/servlet-name&gt;
	&lt;url-pattern&gt;/jdbcmetrics&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
</pre>
</li>
</ol>

## Fetching info from individual request(s)
You can get information on how many database reads & writes your request generated, by adding a request header. By default, 
the header name is *jdbcmetrics* and you can configure that in *web.xml* and the JDBCMetrics servlet filter.

By sending a header like: *jdbcmetrics=yes*
you will get two response headers: *nr-of-reads* & *nr-of-writes* holding the values of the reads & writes.

*Remember* The cache within your application will give you different values if the caches is primed or not!

## How it works
JDBCMetrics uses the great http://metrics.codahale.com/ library for collecting metric.


## License

Copyright 2012 Magnus Lundberg & Peter Hedenskog

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.