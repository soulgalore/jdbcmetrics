# JDBCMetrics - get information from your JDBC driver

Using JDBCMetrics you can get hold of the following information from your driver

By only setting up the JDBCMetricsDriver:
<ul>
<li>The total number of database reads</li>
<li>The total number of database writes</li>
<li>Number of reads per second (per minute, 5 minutes & 15 minutes)</li>
<li>Number of writes per second (per minute, 5 minutes & 15 minutes)</li>
</ul>
By also setting up the JDBCMetricsFilter:
<ul>
<li>The number of database reads created for a specific HTTP request</li>
<li>The number of database writes created for a specific HTTP request</li>
<li>Statistics about reads per request (average, median, percentile etc)</li>
<li>Statistics about writes per request (average, median, percentile etc)</li>
</ul>


## How to setup
<ol><li>Jack in JDBCMetricsDriver like this:</li>
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
</ol>

## Reporters
JDBCMetrics uses the great [Metrics](http://metrics.codahale.com/) as metric backend, that have the following different ways of reporting:
<ul>
<li>[JMX](http://metrics.codahale.com/manual/core/#jmx) (not recommended for production)</li>
<li>[console](http://metrics.codahale.com/manual/core/#console)</li>
<li>[CSV](http://metrics.codahale.com/manual/core/#csv) - which periodically appends to a set of .csv files in a given directory.</li>
<li>[Servlet](http://metrics.codahale.com/manual/servlet/#metricsservlet) -  which will retrun all metrics as JSON</li>
<li>[Ganglia](http://metrics.codahale.com/manual/ganglia/#manual-ganglia) - read more about Ganglia [here](http://ganglia.sourceforge.net/)</li>
<li>[Graphite](http://metrics.codahale.com/manual/graphite/#manual-graphite) - read more about Graphite [here](http://graphite.wikidot.com/)</li>
</ul>

Click [here](http://metrics.codahale.com/manual/core/#reporters) for documentation of how to setup the reporters.

And here's a real world example of setting up an metrics servlet:

First add it the servlet to your pom.xml file:
<pre>
&lt;dependency&gt;
	&lt;groupId&gt;com.yammer.metrics&lt;/groupId&gt;
	&lt;artifactId&gt;metrics-servlet&lt;/artifactId&gt;
	&lt;version&gt;2.2.0&lt;/version&gt;
&lt;/dependency&gt;
</pre>

Then set it up in your web.xml:
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


## Fetching info from individual request(s)
You can get information on how many database reads & writes your request generated, by adding a request header. By default, 
the header name is *jdbcmetrics* and you can configure that in *web.xml* and the JDBCMetrics servlet filter.

By sending a header like: *jdbcmetrics=yes*
you will get two response headers: *nr-of-reads* & *nr-of-writes* holding the values of the reads & writes.

*Remember* The cache within your application will give you different values if the caches is primed or not!

## How it works

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