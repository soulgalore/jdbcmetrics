package com.soulgalore.jdbcmetrics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WhenAWriteIsDone {

  private ReadAndWrites rw;

  @Before
  public void setUp() throws Exception {

    rw = new ReadAndWrites();
  }

  @Test
  public void theNumberOfWritesShouldBeIncreased() {

    rw.incWrites(5);
    assertThat("The writes should be increased", rw.getWrites(), is(1));
    rw.incWrites(4);
    rw.incWrites(2);
    assertThat("The writes should be increased", rw.getWrites(), is(3));
    assertThat("The total write time is right", (new Long(rw.getTotalWriteTime())).intValue(),
        is(11));
    rw.clear();
    assertThat("The writes should be cleared", rw.getWrites(), is(0));

  }

}
