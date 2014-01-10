package com.soulgalore.jdbcmetrics;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import javax.naming.RefAddr;
import javax.naming.Reference;

import org.junit.Test;

public class WhenDataSourceIsCreatedByFactory {

  DataSourceFactory factory = new DataSourceFactory();

  @Test
  public void refStringShouldReturnNullWhenMissing() {
    Reference ref = mock(Reference.class);
    when(ref.get("test")).thenReturn(null);
    assertThat(factory.getRefString(ref, "test"), nullValue());
  }

  @Test
  public void refStringShouldReturnValue() {
    Reference ref = mock(Reference.class);
    RefAddr refAddr = mock(RefAddr.class);
    when(refAddr.getContent()).thenReturn("hello");
    when(ref.get("test")).thenReturn(refAddr);
    assertThat(factory.getRefString(ref, "test"), is("hello"));
  }

  @Test
  public void shouldCreateDataSourceByDefault() throws Exception {
    Reference ref = mock(Reference.class);
    assertThat(factory.getObjectInstance(ref, null, null, null), instanceOf(DataSource.class));
  }

  @Test
  public void shouldCreateConnectionPoolDataSource() throws Exception {
    Reference ref = mock(Reference.class);
    RefAddr refAddr = mock(RefAddr.class);
    when(refAddr.getContent()).thenReturn(ConnectionPoolDataSource.class.getName());
    when(ref.get(DataSourceFactory.CLASS_NAME)).thenReturn(refAddr);
    assertThat(factory.getObjectInstance(ref, null, null, null),
        instanceOf(ConnectionPoolDataSource.class));
  }

  @Test
  public void shouldCreateXADataSource() throws Exception {
    Reference ref = mock(Reference.class);
    RefAddr refAddr = mock(RefAddr.class);
    when(refAddr.getContent()).thenReturn(XADataSource.class.getName());
    when(ref.get(DataSourceFactory.CLASS_NAME)).thenReturn(refAddr);
    assertThat(factory.getObjectInstance(ref, null, null, null), instanceOf(XADataSource.class));
  }

}
