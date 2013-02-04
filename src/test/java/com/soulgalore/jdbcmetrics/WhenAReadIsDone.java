package com.soulgalore.jdbcmetrics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WhenAReadIsDone {

	private ReadAndWrites rw;

	@Before
	public void setUp() throws Exception {

		rw = new ReadAndWrites();
	}

	@Test
	public void theNumberOfReadsShouldBeIncreased() {
		rw.incReads();
		assertThat("The number of reads should be increased",rw.getReads(), is(1));
		rw.incReads();
		rw.incReads();
		assertThat("The number of reads should be increased", rw.getReads(), is(3));
		rw.clear();
		assertThat("The reads should be cleared", rw.getReads(), is(0));
		
	}

}
