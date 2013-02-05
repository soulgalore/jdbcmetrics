package com.soulgalore.jdbcmetrics.driver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class WhenConnectionIsMade extends AbstractDriverTest {

	@Before
	public void setup() throws SQLException {
	}
	
	@Test
	public void connectionShouldBeProxy() throws SQLException {
		Connection connection = driver.connect(URL_JDBC_METRICS, null);
		assertThat(connection, notNullValue());
		assertThat(connection, instanceOf(Proxy.class));
		assertThat(Proxy.getInvocationHandler(connection), instanceOf(ConnectionInvocationHandler.class));
	}

}
