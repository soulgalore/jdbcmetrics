package com.soulgalore.jdbcmetrics;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.hamcrest.Matchers.is;

public class WhenTheQueryThredLocalIsUsed {

	@Test
	public void theNumberOfReadsShouldBeUpdated() {
		QueryThreadLocal.init();
		QueryThreadLocal.addRead();
		try {
		assertThat(QueryThreadLocal.getNrOfQueries().getReads(),is(1));
		}
		finally {
		QueryThreadLocal.removeNrOfQueries();
		}
	}

	@Test
	public void theNumberOfWritesShouldBeUpdated() {
		QueryThreadLocal.init();
		QueryThreadLocal.addWrite();
		try {
		assertThat(QueryThreadLocal.getNrOfQueries().getWrites(),is(1));
		}
		finally {
		QueryThreadLocal.removeNrOfQueries();
		}
	}
}
