# JDBCMetrics - get information from your JDBC driver [![Build Status](https://travis-ci.org/soulgalore/jdbcmetrics.png?branch=master)](https://travis-ci.org/soulgalore/jdbcmetrics)

[![I Love Open Source](http://www.iloveopensource.io/images/logo-lightbg.png)](http://www.iloveopensource.io/projects/5261764143c6bdee140001cb)

Using **JDBCMetrics** you can get hold of the following information from your driver:

By only setting up the **JDBCMetricsDriver** (meaning configure your driver):

* The total number of database reads
* The total number of database writes
* Number of reads per second (per minute, 5 minutes & 15 minutes)
* Number of writes per second (per minute, 5 minutes & 15 minutes)
* Average/median/percentile query times for reads & for writes

By also setting up the **JDBCMetricsFilter** you will get:

* The number of database reads created for a specific HTTP request
* The number of database writes created for a specific HTTP request
* The time spent in the database for reads & writes per request
* Statistics about reads & read time per request (average, median, percentile etc)
* Statistics about writes & write time per request (average, median, percentile etc)

##Background##
In 99% of the projects we have worked with, when the shit hits the fan the problem is the interaction with the database. We want a super easy way 
of knowing what actually is happening between the application server & the database.

##Setup##
1. [Add the jar](#add-the-jar)
2. [Setup the driver](#setup-the-driver)
3. [Setup the filter](#setup-the-filter-optional) (optional) 
4. [Setup a reporter](#reporters)
5. [Choosing registry](#choose-registry) (optional)


###Add the jar
In your **pom.xml** file add:

<pre>
&lt;dependency&gt;
 &lt;groupId&gt;com.soulgalore&lt;/groupId&gt;
 &lt;artifactId&gt;jdbcmetrics&lt;/artifactId&gt;
 &lt;version&gt;2.0.1&lt;/version&gt;
 &lt;scope&gt;runtime&lt;/scope&gt;
&lt;/dependency&gt;
</pre>

###Setup the driver
Depending on your current setup, this need to be done in different ways.

####Using DataSource####
   
The idea is to change the existing data sources name and create a new JDBCMetrics data source that wraps/proxies the existing and name it with the real data sources original name. You feed it with either the real data source or it's new name (like "java:/comp/env/jdbc/testDS" or "jdbc/testDS").
This can be done as contructor parameters or by setters named "dataSource" or "referenceName". The classes are <code>com.soulgalore.jdbcmetrics.DataSource, ConnectionPoolDataSource, XADataSource</code>.

You can also use the factory <code>com.soulgalore.jdbcmetrics.DataSourceFactory</code> with the params "referenceName" (like "java:/comp/env/jdbc/testDS" or "jdbc/testDS") and "className" which is the type to use (as above, defaults to the DataSouce class if left out).

If you need to pass along the resources in web.xml, don't forget to add the real one with the new name.

If your existing data source can be configured with any jdbc driver, you can use the "Using DriverManager" method instead.
	
####Using DriverManager####
   
The driver is automatically registered in DriverManager (as of JDBC4 in java6). If you need to register it manually either set the JVM parameter <code>-Djdbc.drivers=com.soulgalore.jdbcmetrics.Driver</code>
or load the driver in your code like <code>Class.forName("com.soulgalore.jdbcmetrics.Driver");</code>

Then configure the jdbc url/connect string. If your existing connect string looks like this: <code>jdbc:mysql://localhost:3306/test_db</code><br/>
Prefix it with <code>jdbcmetrics:</code> like this <code>jdbc:jdbcmetrics:mysql://localhost:3306/test_db</code>

Specify the underlaying driver, your regular driver, in the url like this <code>jdbc:jdbcmetrics?driver=com.mysql.jdbc.Driver:mysql://localhost:3306/test_db</code><br/>

**JDBCMetricsDriver** will then instantiate the specified driver to use it underneath. If you omit the driver param **JDBCMetricsDriver** will try to match the url to a driver registered in DriverManager.

### Setup the filter (optional) 

Add the filter in your **web.xml** file (make sure it run early in the chain):
	<pre>
&lt;filter&gt;
	&lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
	&lt;filter-class&gt;
		com.soulgalore.jdbcmetrics.filter.JDBCMetricsFilter
	&lt;/filter-class&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;use-headers&lt;/param-name&gt;
		&lt;param-value&gt;true&lt;/param-value&gt;
	&lt;/init-param&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;request-header-name&lt;/param-name&gt;
		&lt;param-value&gt;jdbcmetrics&lt;/param-value&gt;
	&lt;/init-param&gt;
&lt;/filter&gt;
</pre>
<pre>
&lt;filter-mapping&gt;
	&lt;filter-name&gt;JDBCMetricsFilter&lt;/filter-name&gt;
	&lt;url-pattern&gt;/*&lt;/url-pattern&gt;
&lt;/filter-mapping&gt;
</pre>

###Reporters###
**JDBCMetrics** uses the great [Metrics](https://dropwizard.github.io/metrics/3.1.0/) as metric backend, that have the following different ways of reporting:

* [JMX](https://dropwizard.github.io/metrics/3.1.0/manual/core/#jmx) (not recommended for production)
* [console](https://dropwizard.github.io/metrics/3.1.0/manual/core/#man-core-reporters-console)
* [CSV](https://dropwizard.github.io/metrics/3.1.0/manual/core/#man-core-reporters-csv) - which periodically appends to a set of .csv files in a given directory.
* [Servlet](https://dropwizard.github.io/metrics/3.1.0/manual/servlets/) -  which will return all metrics as JSON.
* [Ganglia](https://dropwizard.github.io/metrics/3.1.0/manual/ganglia/#manual-ganglia) - read more about Ganglia [here](http://ganglia.sourceforge.net/).
* [Graphite](https://dropwizard.github.io/metrics/3.1.0/manual/graphite/#manual-graphite) - read more about Graphite [here](http://graphite.wikidot.com/).


Click [here](https://dropwizard.github.io/metrics/3.1.0/manual/core/#reporters) for documentation of how to setup the reporters.

And here's a real world example of setting up an metrics servlet:

First add it the servlet to your **pom.xml** file:
<pre>
&lt;dependency&gt;
    &lt;groupId&gt;io.dropwizard.metrics&lt;/groupId&gt;
    &lt;artifactId&gt;metrics-servlets&lt;/artifactId&gt;
    &lt;version&gt;3.1.0&lt;/version&gt;
&lt;/dependency&gt;
</pre>

Then set it up in your **web.xml**:
<pre>
&lt;servlet&gt;
	&lt;servlet-name&gt;MetricsServlet&lt;/servlet-name&gt;
	&lt;servlet-class&gt;com.codahale.metrics.servlets.AdminServlet&lt;/servlet-class&gt;
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

###Choose registry###
By default, a new <em>MetricRegistry</em> is used. If you wish to obtain a specific <em>MetricRegistry</em> from <em>SharedMetricRegistries</em>, for example to add other metrics to it, you may either:

* Set the System property <em>com.soulgalore.jdbcmetrics.MetricRegistry</em> to the name of the registry you wish to use
* Set a property called metricRegistry.name in a file called jdbcmetrics.properties at the root of the classpath.

The system property takes precedence over the properties file.

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
The log logs at *debug* level to **com.soulgalore.jdbcmetrics.filter.JDBCMetricsFilter** and uses **SLF4j**. To setup it up to support your current logging framework, checkout the 
[documentation](http://www.slf4j.org/manual.html#swapping).

A log entry will look something like this:
<pre>
URL: http://www.example.com reads:10 writes:1
</pre>


## License

Copyright 2014 Magnus Lundberg & Peter Hedenskog

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/soulgalore/jdbcmetrics/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

