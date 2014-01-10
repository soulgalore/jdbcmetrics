package com.soulgalore.jdbcmetrics.proxy;

import com.soulgalore.jdbcmetrics.Driver;

public abstract class AbstractDriverTest {

  public static final String URL_JDBC_METRICS =
      "jdbc:jdbcmetrics:realDriver:someFancyUrl:3306?doit";
  public static final String URL_JDBC_METRICS_SPECIFIED_DRIVER =
      "jdbc:jdbcmetrics?driver=org.postgresql.Driver:realDriver:someFancyUrl:3306?doit";
  public static final String URL_KNOWN_DRIVER = "jdbc:realDriver:someFancyUrl:3306?doit";
  public static final String URL_UNKNOWN = "jdbc:someDriver:someFancyUrl:3306?doit";

  public Driver driver = MockDriverHelper.driver;
  public java.sql.Driver underlayingDriver = MockDriverHelper.underlayingDriver;

}
