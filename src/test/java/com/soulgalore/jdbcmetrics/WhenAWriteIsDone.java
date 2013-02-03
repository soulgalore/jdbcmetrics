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
		rw.incWrites();
		assertThat(rw.getWrites(), is(1));
		rw.incWrites();
		rw.incWrites();
		assertThat(rw.getWrites(), is(3));
	}

}
