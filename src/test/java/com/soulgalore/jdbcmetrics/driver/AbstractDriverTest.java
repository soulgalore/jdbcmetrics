package com.soulgalore.jdbcmetrics.driver;

import java.sql.Driver;

public abstract class AbstractDriverTest {

	public static final String URL_JDBC_METRICS =  "jdbc:jdbcmetrics:realDriver:someFancyUrl:3306?doit";
	public static final String URL_KNOWN_DRIVER =  "jdbc:realDriver:someFancyUrl:3306?doit";
	public static final String URL_UNKNOWN =  "jdbc:someDriver:someFancyUrl:3306?doit";
	
	public JDBCMetricsDriver driver = MockDriverHelper.driver;
	public Driver underlayingDriver = MockDriverHelper.underlayingDriver;
	
}
