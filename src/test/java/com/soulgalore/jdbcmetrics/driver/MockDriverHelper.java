package com.soulgalore.jdbcmetrics.driver;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

// Static help class - just register mock drivers once in DriverManager (which is static)
public class MockDriverHelper {

	static JDBCMetricsDriver driver;
	static Driver underlayingDriver;
	
	static {
		try {
			driver = new JDBCMetricsDriver(); // static block registers in driver manager
	
			Connection connection = mock(Connection.class);
			Statement statement = mock(Statement.class);
			CallableStatement callableStatement = mock(CallableStatement.class);
			PreparedStatement preparedStatement = mock(PreparedStatement.class);
			when(connection.createStatement()).thenReturn(statement);
			when(connection.createStatement(anyInt(), anyInt())).thenReturn(statement);
			when(connection.createStatement(anyInt(), anyInt(), anyInt())).thenReturn(statement);
			when(connection.prepareCall(anyString())).thenReturn(callableStatement);
			when(connection.prepareCall(anyString(), anyInt(), anyInt())).thenReturn(callableStatement);
			when(connection.prepareCall(anyString(), anyInt(), anyInt(), anyInt())).thenReturn(callableStatement);
			when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
			when(connection.prepareStatement(anyString(), any(int[].class))).thenReturn(preparedStatement);
			when(connection.prepareStatement(anyString(), any(String[].class))).thenReturn(preparedStatement);
			when(connection.prepareStatement(anyString(), anyInt(), anyInt())).thenReturn(preparedStatement);
			when(connection.prepareStatement(anyString(), anyInt(), anyInt(), anyInt())).thenReturn(preparedStatement);
	
			underlayingDriver = mock(Driver.class);
			when(underlayingDriver.acceptsURL(AbstractDriverTest.URL_KNOWN_DRIVER)).thenReturn(true);
			when(underlayingDriver.connect(anyString(), any(Properties.class))).thenReturn(connection);
			
			DriverManager.registerDriver(underlayingDriver);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
