package com.soulgalore.jdbcmetrics.driver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ProxyFactory {

    public Connection connectionProxy(Connection connection) {
    	return createProxy(Connection.class, new ConnectionInvocationHandler(connection, this));
    }

	public Statement statementProxy(Statement statement) {
    	return createProxy(Statement.class, new StatementInvocationHandler(statement));
	}
	
	public PreparedStatement preparedStatementProxy(PreparedStatement preparedStatement) {
		return createProxy(PreparedStatement.class, new StatementInvocationHandler(preparedStatement));
	}
	
	public CallableStatement callableStatementProxy(CallableStatement callableStatement) {
		return createProxy(CallableStatement.class, new StatementInvocationHandler(callableStatement));
	}

	private <T> T createProxy(Class<T> clazz, InvocationHandler handler) {
		return clazz.cast(Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class[] {clazz}, handler));
	}

}
