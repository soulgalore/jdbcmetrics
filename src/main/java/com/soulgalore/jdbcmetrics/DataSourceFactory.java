package com.soulgalore.jdbcmetrics;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class DataSourceFactory implements ObjectFactory {

	public static final String REFERENCE_NAME = "referenceName";
	
	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx,
			Hashtable<?, ?> environment) throws Exception {
		
		Reference reference = (Reference) obj;
		RefAddr refAddr = reference.get(REFERENCE_NAME);
		if (refAddr != null) {
			String resourceName = (String)refAddr.getContent();
			if (resourceName != null) {
				return new DataSource(resourceName);
			}
		}
		return null;
	}
}
