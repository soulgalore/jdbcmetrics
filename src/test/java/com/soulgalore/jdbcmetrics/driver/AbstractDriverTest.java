package com.soulgalore.jdbcmetrics.driver;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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

		Connection connection = mock(Connection.class);
		Statement statement = mock(Statement.class);
		CallableStatement callableStatement = mock(CallableStatement.class);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.createStatement()).thenReturn(statement);
		when(connection.prepareCall(anyString())).thenReturn(callableStatement);
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

		underlayingDriver = mock(Driver.class);
		when(underlayingDriver.acceptsURL(URL_KNOWN_DRIVER)).thenReturn(true);
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
