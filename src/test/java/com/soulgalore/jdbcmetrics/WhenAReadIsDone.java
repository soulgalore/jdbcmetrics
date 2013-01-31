package com.soulgalore.jdbcmetrics;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
public class WhenAReadIsDone {

	private ReadAndWrites rw;
	private Meter read;
	private Meter write;
	@Before
	public void setUp() throws Exception {
		read = Metrics.newMeter(WhenAReadIsDone.class, "read", "jdbcread", TimeUnit.SECONDS);
		write = Metrics.newMeter(WhenAReadIsDone.class, "write", "jdbcwrite", TimeUnit.SECONDS);
		
		rw  = new ReadAndWrites(read,write);
	}

	@Test
	public void theNumberOfReadsShouldBeIncreased() {
		rw.incReads();
		assertThat(rw.getReads(), is(1));
		rw.incReads();
		rw.incReads();
		assertThat(rw.getReads(), is(3));
		assertThat(write.count() , is(new Long(0)));
		assertThat(rw.getWrites(),is(0));
		assertThat(read.count(),is(new Long(3)));
	
	}

}
