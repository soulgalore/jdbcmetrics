package com.soulgalore.jdbcmetrics.driver;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import com.soulgalore.jdbcmetrics.QueryThreadLocal;

public class WhenQueryIsExecuted extends AbstractDriverTest {

	Connection connection;
	Statement statement;
	
	@Before
	public void setup() throws SQLException {
		connection = driver.connect(URL_JDBC_METRICS, null);
		assertThat(connection, notNullValue());
		statement = connection.createStatement();
		assertThat(statement, notNullValue());
		
		QueryThreadLocal.init();
		assertThat(reads(), is(0));
		assertThat(writes(), is(0));
	}
	
	@Test
	public void executeShouldIncreaseReadCounter() throws SQLException {
		statement.execute("SELECT 1");
		assertThat(reads(), is(1));
		assertThat(writes(), is(0));
	}
	
	@Test
	public void executeUpdateShouldIncreaseWriteCounter() throws SQLException {
		statement.executeUpdate("INSERT 1");
		assertThat(reads(), is(0));
		assertThat(writes(), is(1));
	}

	// implement rest
	
	private int reads() {
		return QueryThreadLocal.getNrOfQueries().getReads();
	}
	
	private int writes() {
		return QueryThreadLocal.getNrOfQueries().getWrites();
	}
}
