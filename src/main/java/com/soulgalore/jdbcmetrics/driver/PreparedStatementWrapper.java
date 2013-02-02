package com.soulgalore.jdbcmetrics.driver;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.soulgalore.jdbcmetrics.QueryThreadLocal;

public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {

	private final PreparedStatement pst;
	private final String sql;
	public PreparedStatementWrapper(final PreparedStatement pst) {
		super(pst);
		this.pst = pst;
		sql = "";
	}
	
	public PreparedStatementWrapper(final PreparedStatement pst, String sql) {
		super(pst);
		this.pst = pst;
		this.sql = sql;
	}
	
	public void addBatch() throws SQLException {
		pst.addBatch();
	}
	public void clearParameters() throws SQLException {
		pst.clearParameters();
	}
	public boolean execute() throws SQLException {
		if (JDBCMetricsDriver.isRead(sql))
			readStats();
		else writeStats();
		return pst.execute();
	}
	public ResultSet executeQuery() throws SQLException {
		readStats();
		return pst.executeQuery();
	}
	public int executeUpdate() throws SQLException {
		writeStats();
		return pst.executeUpdate();
	}
	public ResultSetMetaData getMetaData() throws SQLException {
		return pst.getMetaData();
	}
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return pst.getParameterMetaData();
	}
	public void setArray(int parameterIndex, Array x) throws SQLException {
		pst.setArray(parameterIndex, x);
	}
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		pst.setAsciiStream(parameterIndex, x, length);
	}
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		pst.setAsciiStream(parameterIndex, x, length);
	}
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		pst.setAsciiStream(parameterIndex, x);
	}
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		pst.setBigDecimal(parameterIndex, x);
	}
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		pst.setBinaryStream(parameterIndex, x, length);
	}
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		pst.setBinaryStream(parameterIndex, x, length);
	}
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		pst.setBinaryStream(parameterIndex, x);
	}
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		pst.setBlob(parameterIndex, x);
	}
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		pst.setBlob(parameterIndex, inputStream, length);
	}
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		pst.setBlob(parameterIndex, inputStream);
	}
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		pst.setBoolean(parameterIndex, x);
	}
	public void setByte(int parameterIndex, byte x) throws SQLException {
		pst.setByte(parameterIndex, x);
	}
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		pst.setBytes(parameterIndex, x);
	}
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		pst.setCharacterStream(parameterIndex, reader, length);
	}
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		pst.setCharacterStream(parameterIndex, reader, length);
	}
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		pst.setCharacterStream(parameterIndex, reader);
	}
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		pst.setClob(parameterIndex, x);
	}
	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		pst.setClob(parameterIndex, reader, length);
	}
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		pst.setClob(parameterIndex, reader);
	}
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		pst.setDate(parameterIndex, x, cal);
	}
	public void setDate(int parameterIndex, Date x) throws SQLException {
		pst.setDate(parameterIndex, x);
	}
	public void setDouble(int parameterIndex, double x) throws SQLException {
		pst.setDouble(parameterIndex, x);
	}
	public void setFloat(int parameterIndex, float x) throws SQLException {
		pst.setFloat(parameterIndex, x);
	}
	public void setInt(int parameterIndex, int x) throws SQLException {
		pst.setInt(parameterIndex, x);
	}
	public void setLong(int parameterIndex, long x) throws SQLException {
		pst.setLong(parameterIndex, x);
	}
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		pst.setNCharacterStream(parameterIndex, value, length);
	}
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		pst.setNCharacterStream(parameterIndex, value);
	}
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		pst.setNClob(parameterIndex, value);
	}
	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		pst.setNClob(parameterIndex, reader, length);
	}
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		pst.setNClob(parameterIndex, reader);
	}
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		pst.setNString(parameterIndex, value);
	}
	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		pst.setNull(parameterIndex, sqlType, typeName);
	}
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		pst.setNull(parameterIndex, sqlType);
	}
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		pst.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		pst.setObject(parameterIndex, x, targetSqlType);
	}
	public void setObject(int parameterIndex, Object x) throws SQLException {
		pst.setObject(parameterIndex, x);
	}
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		pst.setRef(parameterIndex, x);
	}
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		pst.setRowId(parameterIndex, x);
	}
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		pst.setSQLXML(parameterIndex, xmlObject);
	}
	public void setShort(int parameterIndex, short x) throws SQLException {
		pst.setShort(parameterIndex, x);
	}
	public void setString(int parameterIndex, String x) throws SQLException {
		pst.setString(parameterIndex, x);
	}
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		pst.setTime(parameterIndex, x, cal);
	}
	public void setTime(int parameterIndex, Time x) throws SQLException {
		pst.setTime(parameterIndex, x);
	}
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		pst.setTimestamp(parameterIndex, x, cal);
	}
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		pst.setTimestamp(parameterIndex, x);
	}
	public void setURL(int parameterIndex, URL x) throws SQLException {
		pst.setURL(parameterIndex, x);
	}
	@Deprecated
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		pst.setUnicodeStream(parameterIndex, x, length);
	}
	
}
