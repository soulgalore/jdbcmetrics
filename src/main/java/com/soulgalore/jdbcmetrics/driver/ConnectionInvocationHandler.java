package com.soulgalore.jdbcmetrics.driver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ConnectionInvocationHandler implements InvocationHandler {

	private Connection connection;
	private ProxyFactory proxyFactory;
	
	public ConnectionInvocationHandler(Connection connection,
			ProxyFactory proxyFactory) {
		super();
		this.connection = connection;
		this.proxyFactory = proxyFactory;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object o = method.invoke(connection, args);
		if ("createStatement".equals(method.getName())) {
			o = proxyFactory.statementProxy((Statement) o);
		} else if ("prepareStatement".equals(method.getName())) {
			o = proxyFactory.preparedStatementProxy((PreparedStatement) o, args[0].toString());
		} else if ("prepareCall".equals(method.getName())) {
			o = proxyFactory.callableStatementProxy((CallableStatement) o, args[0].toString());
		}
		return o;
	}

}
