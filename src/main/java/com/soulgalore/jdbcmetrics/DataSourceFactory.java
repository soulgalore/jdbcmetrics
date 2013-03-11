package com.soulgalore.jdbcmetrics;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class DataSourceFactory implements ObjectFactory {

	public static final String REFERENCE_NAME = "referenceName";
	public static final String CLASS_NAME = "className";
	
	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx,
			Hashtable<?, ?> environment) throws Exception {
		
		Reference reference = (Reference) obj;

		String resourceName = getRefString(reference, REFERENCE_NAME);
		String className = getRefString(reference, CLASS_NAME);

		if (XADataSource.class.getName().equals(className)) {
			return new XADataSource(resourceName);
		}
		else if (ConnectionPoolDataSource.class.getName().equals(className)) {
			return new ConnectionPoolDataSource(resourceName);
		}
		return new DataSource(resourceName);
	}
	
	private String getRefString(Reference reference, String name) {
		RefAddr refAddr = reference.get(name);
		if (refAddr != null) {
			String s = (String)refAddr.getContent();
			if (s != null) {
				return s;
			}
		}
		return null;
	}
}
