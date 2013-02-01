package com.soulgalore.jdbcmetrics.driver;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class CallableStatementWrapper extends PreparedStatementWrapper 
		implements CallableStatement {
	
	private final CallableStatement cst;
	
	public CallableStatementWrapper(final CallableStatement cst, String sql) {
		super(cst,sql);
		this.cst = cst;
	}
	public Array getArray(int parameterIndex) throws SQLException {
		return cst.getArray(parameterIndex);
	}
	public Array getArray(String parameterName) throws SQLException {
		return cst.getArray(parameterName);
	}
	@Deprecated
	public BigDecimal getBigDecimal(int parameterIndex, int scale)
			throws SQLException {
		return cst.getBigDecimal(parameterIndex, scale);
	}
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		return cst.getBigDecimal(parameterIndex);
	}
	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		return cst.getBigDecimal(parameterName);
	}
	public Blob getBlob(int parameterIndex) throws SQLException {
		return cst.getBlob(parameterIndex);
	}
	public Blob getBlob(String parameterName) throws SQLException {
		return cst.getBlob(parameterName);
	}
	public boolean getBoolean(int parameterIndex) throws SQLException {
		return cst.getBoolean(parameterIndex);
	}
	public boolean getBoolean(String parameterName) throws SQLException {
		return cst.getBoolean(parameterName);
	}
	public byte getByte(int parameterIndex) throws SQLException {
		return cst.getByte(parameterIndex);
	}
	public byte getByte(String parameterName) throws SQLException {
		return cst.getByte(parameterName);
	}
	public byte[] getBytes(int parameterIndex) throws SQLException {
		return cst.getBytes(parameterIndex);
	}
	public byte[] getBytes(String parameterName) throws SQLException {
		return cst.getBytes(parameterName);
	}
	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		return cst.getCharacterStream(parameterIndex);
	}
	public Reader getCharacterStream(String parameterName) throws SQLException {
		return cst.getCharacterStream(parameterName);
	}
	public Clob getClob(int parameterIndex) throws SQLException {
		return cst.getClob(parameterIndex);
	}
	public Clob getClob(String parameterName) throws SQLException {
		return cst.getClob(parameterName);
	}
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		return cst.getDate(parameterIndex, cal);
	}
	public Date getDate(int parameterIndex) throws SQLException {
		return cst.getDate(parameterIndex);
	}
	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		return cst.getDate(parameterName, cal);
	}
	public Date getDate(String parameterName) throws SQLException {
		return cst.getDate(parameterName);
	}
	public double getDouble(int parameterIndex) throws SQLException {
		return cst.getDouble(parameterIndex);
	}
	public double getDouble(String parameterName) throws SQLException {
		return cst.getDouble(parameterName);
	}
	public float getFloat(int parameterIndex) throws SQLException {
		return cst.getFloat(parameterIndex);
	}
	public float getFloat(String parameterName) throws SQLException {
		return cst.getFloat(parameterName);
	}
	public int getInt(int parameterIndex) throws SQLException {
		return cst.getInt(parameterIndex);
	}
	public int getInt(String parameterName) throws SQLException {
		return cst.getInt(parameterName);
	}
	public long getLong(int parameterIndex) throws SQLException {
		return cst.getLong(parameterIndex);
	}
	public long getLong(String parameterName) throws SQLException {
		return cst.getLong(parameterName);
	}
	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		return cst.getNCharacterStream(parameterIndex);
	}
	public Reader getNCharacterStream(String parameterName) throws SQLException {
		return cst.getNCharacterStream(parameterName);
	}
	public NClob getNClob(int parameterIndex) throws SQLException {
		return cst.getNClob(parameterIndex);
	}
	public NClob getNClob(String parameterName) throws SQLException {
		return cst.getNClob(parameterName);
	}
	public String getNString(int parameterIndex) throws SQLException {
		return cst.getNString(parameterIndex);
	}
	public String getNString(String parameterName) throws SQLException {
		return cst.getNString(parameterName);
	}
	public Object getObject(int parameterIndex, Map<String, Class<?>> map)
			throws SQLException {
		return cst.getObject(parameterIndex, map);
	}
	public Object getObject(int parameterIndex) throws SQLException {
		return cst.getObject(parameterIndex);
	}
	public Object getObject(String parameterName, Map<String, Class<?>> map)
			throws SQLException {
		return cst.getObject(parameterName, map);
	}
	public Object getObject(String parameterName) throws SQLException {
		return cst.getObject(parameterName);
	}
	public Ref getRef(int parameterIndex) throws SQLException {
		return cst.getRef(parameterIndex);
	}
	public Ref getRef(String parameterName) throws SQLException {
		return cst.getRef(parameterName);
	}
	public RowId getRowId(int parameterIndex) throws SQLException {
		return cst.getRowId(parameterIndex);
	}
	public RowId getRowId(String parameterName) throws SQLException {
		return cst.getRowId(parameterName);
	}
	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		return cst.getSQLXML(parameterIndex);
	}
	public SQLXML getSQLXML(String parameterName) throws SQLException {
		return cst.getSQLXML(parameterName);
	}
	public short getShort(int parameterIndex) throws SQLException {
		return cst.getShort(parameterIndex);
	}
	public short getShort(String parameterName) throws SQLException {
		return cst.getShort(parameterName);
	}
	public String getString(int parameterIndex) throws SQLException {
		return cst.getString(parameterIndex);
	}
	public String getString(String parameterName) throws SQLException {
		return cst.getString(parameterName);
	}
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		return cst.getTime(parameterIndex, cal);
	}
	public Time getTime(int parameterIndex) throws SQLException {
		return cst.getTime(parameterIndex);
	}
	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		return cst.getTime(parameterName, cal);
	}
	public Time getTime(String parameterName) throws SQLException {
		return cst.getTime(parameterName);
	}
	public Timestamp getTimestamp(int parameterIndex, Calendar cal)
			throws SQLException {
		return cst.getTimestamp(parameterIndex, cal);
	}
	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		return cst.getTimestamp(parameterIndex);
	}
	public Timestamp getTimestamp(String parameterName, Calendar cal)
			throws SQLException {
		return cst.getTimestamp(parameterName, cal);
	}
	public Timestamp getTimestamp(String parameterName) throws SQLException {
		return cst.getTimestamp(parameterName);
	}
	public URL getURL(int parameterIndex) throws SQLException {
		return cst.getURL(parameterIndex);
	}
	public URL getURL(String parameterName) throws SQLException {
		return cst.getURL(parameterName);
	}
	public void registerOutParameter(int parameterIndex, int sqlType, int scale)
			throws SQLException {
		cst.registerOutParameter(parameterIndex, sqlType, scale);
	}
	public void registerOutParameter(int parameterIndex, int sqlType,
			String typeName) throws SQLException {
		cst.registerOutParameter(parameterIndex, sqlType, typeName);
	}
	public void registerOutParameter(int parameterIndex, int sqlType)
			throws SQLException {
		cst.registerOutParameter(parameterIndex, sqlType);
	}
	public void registerOutParameter(String parameterName, int sqlType,
			int scale) throws SQLException {
		cst.registerOutParameter(parameterName, sqlType, scale);
	}
	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) throws SQLException {
		cst.registerOutParameter(parameterName, sqlType, typeName);
	}
	public void registerOutParameter(String parameterName, int sqlType)
			throws SQLException {
		cst.registerOutParameter(parameterName, sqlType);
	}
	public void setAsciiStream(String parameterName, InputStream x, int length)
			throws SQLException {
		cst.setAsciiStream(parameterName, x, length);
	}
	public void setAsciiStream(String parameterName, InputStream x, long length)
			throws SQLException {
		cst.setAsciiStream(parameterName, x, length);
	}
	public void setAsciiStream(String parameterName, InputStream x)
			throws SQLException {
		cst.setAsciiStream(parameterName, x);
	}
	public void setBigDecimal(String parameterName, BigDecimal x)
			throws SQLException {
		cst.setBigDecimal(parameterName, x);
	}
	public void setBinaryStream(String parameterName, InputStream x, int length)
			throws SQLException {
		cst.setBinaryStream(parameterName, x, length);
	}
	public void setBinaryStream(String parameterName, InputStream x, long length)
			throws SQLException {
		cst.setBinaryStream(parameterName, x, length);
	}
	public void setBinaryStream(String parameterName, InputStream x)
			throws SQLException {
		cst.setBinaryStream(parameterName, x);
	}
	public void setBlob(String parameterName, Blob x) throws SQLException {
		cst.setBlob(parameterName, x);
	}
	public void setBlob(String parameterName, InputStream inputStream,
			long length) throws SQLException {
		cst.setBlob(parameterName, inputStream, length);
	}
	public void setBlob(String parameterName, InputStream inputStream)
			throws SQLException {
		cst.setBlob(parameterName, inputStream);
	}
	public void setBoolean(String parameterName, boolean x) throws SQLException {
		cst.setBoolean(parameterName, x);
	}
	public void setByte(String parameterName, byte x) throws SQLException {
		cst.setByte(parameterName, x);
	}
	public void setBytes(String parameterName, byte[] x) throws SQLException {
		cst.setBytes(parameterName, x);
	}
	public void setCharacterStream(String parameterName, Reader reader,
			int length) throws SQLException {
		cst.setCharacterStream(parameterName, reader, length);
	}
	public void setCharacterStream(String parameterName, Reader reader,
			long length) throws SQLException {
		cst.setCharacterStream(parameterName, reader, length);
	}
	public void setCharacterStream(String parameterName, Reader reader)
			throws SQLException {
		cst.setCharacterStream(parameterName, reader);
	}
	public void setClob(String parameterName, Clob x) throws SQLException {
		cst.setClob(parameterName, x);
	}
	public void setClob(String parameterName, Reader reader, long length)
			throws SQLException {
		cst.setClob(parameterName, reader, length);
	}
	public void setClob(String parameterName, Reader reader)
			throws SQLException {
		cst.setClob(parameterName, reader);
	}
	public void setDate(String parameterName, Date x, Calendar cal)
			throws SQLException {
		cst.setDate(parameterName, x, cal);
	}
	public void setDate(String parameterName, Date x) throws SQLException {
		cst.setDate(parameterName, x);
	}
	public void setDouble(String parameterName, double x) throws SQLException {
		cst.setDouble(parameterName, x);
	}
	public void setFloat(String parameterName, float x) throws SQLException {
		cst.setFloat(parameterName, x);
	}
	public void setInt(String parameterName, int x) throws SQLException {
		cst.setInt(parameterName, x);
	}
	public void setLong(String parameterName, long x) throws SQLException {
		cst.setLong(parameterName, x);
	}
	public void setNCharacterStream(String parameterName, Reader value,
			long length) throws SQLException {
		cst.setNCharacterStream(parameterName, value, length);
	}
	public void setNCharacterStream(String parameterName, Reader value)
			throws SQLException {
		cst.setNCharacterStream(parameterName, value);
	}
	public void setNClob(String parameterName, NClob value) throws SQLException {
		cst.setNClob(parameterName, value);
	}
	public void setNClob(String parameterName, Reader reader, long length)
			throws SQLException {
		cst.setNClob(parameterName, reader, length);
	}
	public void setNClob(String parameterName, Reader reader)
			throws SQLException {
		cst.setNClob(parameterName, reader);
	}
	public void setNString(String parameterName, String value)
			throws SQLException {
		cst.setNString(parameterName, value);
	}
	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException {
		cst.setNull(parameterName, sqlType, typeName);
	}
	public void setNull(String parameterName, int sqlType) throws SQLException {
		cst.setNull(parameterName, sqlType);
	}
	public void setObject(String parameterName, Object x, int targetSqlType,
			int scale) throws SQLException {
		cst.setObject(parameterName, x, targetSqlType, scale);
	}
	public void setObject(String parameterName, Object x, int targetSqlType)
			throws SQLException {
		cst.setObject(parameterName, x, targetSqlType);
	}
	public void setObject(String parameterName, Object x) throws SQLException {
		cst.setObject(parameterName, x);
	}
	public void setRowId(String parameterName, RowId x) throws SQLException {
		cst.setRowId(parameterName, x);
	}
	public void setSQLXML(String parameterName, SQLXML xmlObject)
			throws SQLException {
		cst.setSQLXML(parameterName, xmlObject);
	}
	public void setShort(String parameterName, short x) throws SQLException {
		cst.setShort(parameterName, x);
	}
	public void setString(String parameterName, String x) throws SQLException {
		cst.setString(parameterName, x);
	}
	public void setTime(String parameterName, Time x, Calendar cal)
			throws SQLException {
		cst.setTime(parameterName, x, cal);
	}
	public void setTime(String parameterName, Time x) throws SQLException {
		cst.setTime(parameterName, x);
	}
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
			throws SQLException {
		cst.setTimestamp(parameterName, x, cal);
	}
	public void setTimestamp(String parameterName, Timestamp x)
			throws SQLException {
		cst.setTimestamp(parameterName, x);
	}
	public void setURL(String parameterName, URL val) throws SQLException {
		cst.setURL(parameterName, val);
	}
	public boolean wasNull() throws SQLException {
		return cst.wasNull();
	}
}
