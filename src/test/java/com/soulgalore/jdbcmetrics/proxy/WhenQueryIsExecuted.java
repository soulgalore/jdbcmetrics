package com.soulgalore.jdbcmetrics.proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import com.soulgalore.jdbcmetrics.JDBCMetrics;
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
  public void executeShouldIncreaseWriteCounter() throws SQLException {
    statement.execute("INSERT 1 (SELECT 2)");
    assertThat(reads(), is(0));
    assertThat(writes(), is(1));
  }

  @Test
  public void executeQueryShouldIncreaseReadCounter() throws SQLException {
    statement.executeQuery("SELECT 1");
    assertThat(reads(), is(1));
    assertThat(writes(), is(0));
  }

  @Test
  public void executeUpdateShouldIncreaseWriteCounter() throws SQLException {
    statement.executeUpdate("INSERT 1");
    assertThat(reads(), is(0));
    assertThat(writes(), is(1));
  }

  @Test
  public void executeBatchShouldIncreaseCounters() throws SQLException {
    statement.addBatch("SELECT 1");
    statement.addBatch("INSERT 2");
    statement.addBatch("SELECT 3");
    statement.addBatch("SELECT 4");
    statement.addBatch("INSERT 5");
    statement.executeBatch();
    assertThat(reads(), is(3));
    assertThat(writes(), is(2));
  }

  @Test
  public void clearBatchShouldResetCounters() throws SQLException {
    statement.addBatch("SELECT 1");
    statement.addBatch("INSERT 2");
    statement.clearBatch();
    assertThat(reads(), is(0));
    assertThat(writes(), is(0));
  }

  @Test
  public void executePreparedStatementShouldIncreaseReadCounter() throws SQLException {
    PreparedStatement pst = connection.prepareStatement("SELECT 1");
    pst.execute();
    assertThat(reads(), is(1));
    assertThat(writes(), is(0));
  }

  @Test
  public void executePreparedStatementShouldIncreaseWriteCounter() throws SQLException {
    PreparedStatement pst = connection.prepareStatement("INSERT 1");
    pst.execute();
    assertThat(reads(), is(0));
    assertThat(writes(), is(1));
  }

  @Test
  public void executeCallableStatementShouldIncreaseReadCounter() throws SQLException {
    CallableStatement cst = connection.prepareCall("SELECT 1");
    cst.execute();
    assertThat(reads(), is(1));
    assertThat(writes(), is(0));
  }

  @Test
  public void executeCallableStatementShouldIncreaseWriteCounter() throws SQLException {
    CallableStatement cst = connection.prepareCall("INSERT 1");
    cst.execute();
    assertThat(reads(), is(0));
    assertThat(writes(), is(1));
  }

  @Test
  public void allPrepareStatementsExecuteShouldIncreaseCounter() throws SQLException {

    PreparedStatement pst = null;
    try {
      pst = connection.prepareStatement("SELECT 1");
      pst.execute();
      assertThat(reads(), is(1));
      assertThat(writes(), is(0));
      pst.executeQuery();
      assertThat(reads(), is(2));
      assertThat(writes(), is(0));
      pst.execute("SELECT 2");
      assertThat(reads(), is(3));
      assertThat(writes(), is(0));
      pst.execute("SELECT 1", 1);
      assertThat(reads(), is(4));
      assertThat(writes(), is(0));
      pst.execute("SELECT 1,2", new int[] {1, 2});
      assertThat(reads(), is(5));
      assertThat(writes(), is(0));
      pst.execute("SELECT 1", new String[] {"mycolumn"});
      assertThat(reads(), is(6));
      assertThat(writes(), is(0));
      pst.addBatch("SELECT 1");
      pst.addBatch("SELECT 2");
      pst.executeBatch();
      assertThat(reads(), is(8));
      assertThat(writes(), is(0));

    } finally {
      if (pst != null) pst.close();
    }

  }


  @Test
  public void executeSelectShouldIncreaseReadTimer() throws SQLException {
    long oldValue = JDBCMetrics.getInstance().getReadTimer().getCount();
    PreparedStatement pst = connection.prepareStatement("SELECT 1");
    pst.execute();
    assertThat(JDBCMetrics.getInstance().getReadTimer().getCount(), is(oldValue + 1));
  }


  @Test
  public void executeInsertShouldIncreaseWriteTimer() throws SQLException {
    long oldValue = JDBCMetrics.getInstance().getWriteTimer().getCount();
    PreparedStatement pst = connection.prepareStatement("INSERT 1");
    pst.execute();
    assertThat(JDBCMetrics.getInstance().getWriteTimer().getCount(), is(oldValue + 1));
  }


  private int reads() {
    return QueryThreadLocal.getNrOfQueries().getReads();
  }

  private int writes() {
    return QueryThreadLocal.getNrOfQueries().getWrites();
  }
}
