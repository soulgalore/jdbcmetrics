package com.soulgalore.jdbcmetrics.driver;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractDriverTest {

	public static final String URL_JDBC_METRICS =  "jdbc:jdbcmetrics:realDriver:someFancyUrl:3306?doit";
	public static final String URL_KNOWN_DRIVER =  "jdbc:realDriver:someFancyUrl:3306?doit";
	public static final String URL_UNKNOWN =  "jdbc:someDriver:someFancyUrl:3306?doit";
	
	public static JDBCMetricsDriver driver;
	public static Driver underlayingDriver;
	
	@BeforeClass
	public static void setUp() throws SQLException {
		driver = new JDBCMetricsDriver(); 
		
		underlayingDriver = mock(Driver.class);
		when(underlayingDriver.acceptsURL(URL_KNOWN_DRIVER)).thenReturn(true);
		Connection connection = mock(Connection.class);
		when(underlayingDriver.connect(anyString(), any(Properties.class))).thenReturn(connection);
		
		DriverManager.registerDriver(underlayingDriver);
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			DriverManager.deregisterDriver(drivers.nextElement());
		}
	}


}
