# JDBCMetrics - get information from your JDBC driver in your Java web environment

Using JDBCMetrics you can get hold of the following information from your driver
<ol>
<li>The number of database reads created for a specific HTTP request</li>
<li>The number of database writes created for a specific HTTP request</li>
<li>The total number of database reads</li>
<li>The total number of database writes</li>
<li>Statistics about reads per request (average, median, percentile etc)</li>
<li>Statistics about writes per request (average, median, percentile etc)</li>
<li>Number of reads per second (per minute, 5 minutes & 15 minutes)</li>
<li>Number of writes per second (per minute, 5 minutes & 15 minutes)</li>
</ol>

## How to setup

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