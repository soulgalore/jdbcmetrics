# JDBCMetrics - get information from your JDBC driver

Using **JDBCMetrics** you can get hold of the following information from your driver:

By only setting up the **JDBCMetricsDriver** (meaning configure your driver):

* The total number of database reads
* The total number of database writes
* Number of reads per second (per minute, 5 minutes & 15 minutes)
* Number of writes per second (per minute, 5 minutes & 15 minutes)
* TODO add info about the timer

By also setting up the **JDBCMetricsFilter** you will get:

* The number of database reads created for a specific HTTP request
* The number of database writes created for a specific HTTP request
* Statistics about reads per request (average, median, percentile etc)
* Statistics about writes per request (average, median, percentile etc)

##Background##
In 99% of the projects we have worked with, when the shit hits the fan the problem is the database. We want a super easy way 
of knowing what actually is happening with the database.

##Setup##
1. Add the jar
2. Setup the driver
3. Setup a reporter
4. Setup the filter (optional)


##Add the jar
In your *pom.xml* file add:
<pre>
&lt;dependency&gt;
 &lt;groupId&gt;com.soulgalore&lt;/groupId&gt;
 &lt;artifactId&gt;jdbcmetrics&lt;/artifactId&gt;
 &lt;version&gt;0.5&lt;/version&gt;
&lt;/dependency&gt;
</pre>

##Setup the driver

###Using DataSource###
   
* Configure the driver class to be <code>com.soulgalore.jdbcmetrics.driver.JDBCMetricsDriver</code>
	
###Using DriverManager###
   
* Set the JVM parameter: <code>-Djdbc.drivers=com.soulgalore.jdbcmetrics.driver.JDBCMetricsDriver</code>
* Or load the driver in your code: <code>Class.forName("com.soulgalore.jdbcmetrics.driver.JDBCMetricsDriver");</code>

### Configure the jdbc url/connect string###

* If your existing connect string looks like this: <code>jdbc:mysql://localhost:3306/test_db</code><br/>
Prefix it with <code>jdbcmetrics:</code> like this <code>jdbc:jdbcmetrics:mysql://localhost:3306/test_db</code>

* Specify the underlaying driver, your regular driver, in the url like this <code>jdbc:jdbcmetrics?driver=com.mysql.jdbc.Driver:mysql://localhost:3306/test_db</code><br/>
JDBCMetricsDriver will then instantiate the driver to use it underneath. If you omit the driver param JDBCMetricsDriver will try to match the url to a driver registered in DriverManager.

## Setup the filter (optional) 

Add the filter in your *web.xml* file (make sure it run early in the chain):
	<pre>
&lt;filter&gt;
	&lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
	&lt;filter-class&gt;
		com.soulgalore.jdbcmetrics.filter.JDBCMetricsFilter
	&lt;/filter-class&gt;
	&lt;param-name&gt;use-headers&lt;/param-name&gt;
			&lt;param-value&gt;true&lt;/param-value&gt;
	&lt;/init-param&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;request-header-name&lt;/param-name&gt;
		&lt;param-value&gt;query-statistics&lt;/param-value&gt;
	&lt;/init-param&gt;
&lt;/filter&gt;
</pre>
<pre>
&lt;filter-mapping&gt;
	&lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
	&lt;url-pattern&gt;/*&lt;/url-pattern&gt;
&lt;/filter-mapping&gt;
</pre>


##Reporters##
**JDBCMetrics** uses the great [Metrics](http://metrics.codahale.com/) as metric backend, that have the following different ways of reporting:

* [JMX](http://metrics.codahale.com/manual/core/#jmx) (not recommended for production)
* [console](http://metrics.codahale.com/manual/core/#console)
* [CSV](http://metrics.codahale.com/manual/core/#csv) - which periodically appends to a set of .csv files in a given directory.
* [Servlet](http://metrics.codahale.com/manual/servlet/#metricsservlet) -  which will return all metrics as JSON.
* [Ganglia](http://metrics.codahale.com/manual/ganglia/#manual-ganglia) - read more about Ganglia [here](http://ganglia.sourceforge.net/).
* [Graphite](http://metrics.codahale.com/manual/graphite/#manual-graphite) - read more about Graphite [here](http://graphite.wikidot.com/).


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
You can get information on how many database reads & writes your request generates by two different ways: Either it is logged to your log system or you can get it as response headers
when you access the page.

### Response headers ###
To get the information back as response headers, you need to turn on that 
functionality in the filter config by setting  **use-headers** to **true**. The reason for why this is not default behaviour is that it will wrap the response and not flush the resoonse until everything is finished, so 
if you have some smart flushing content early thing, the filter will remove that functionality (so for some systems, this is not good running in production).

You can configure the request header name that will trigger the response, by default it is **jdbcmetrics** and you configure that in *web.xml* and the JDBCMetrics servlet filter.

By sending a header like: **jdbcmetrics=yes**
you will get two response headers: **nr-of-reads** & **nr-of-writes** holding the values of the reads & writes.


### Getting the log ###
The log logs at *debug* level to **com.soulgalore.jdbcmetrics.filter.JDBCMetricsFilter**. An log entry will look like this:
<pre>

</pre>


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
