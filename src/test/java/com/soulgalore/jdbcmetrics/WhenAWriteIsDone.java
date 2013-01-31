package com.soulgalore.jdbcmetrics;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
public class WhenAWriteIsDone {

	private ReadAndWrites rw;
	private Meter read;
	private Meter write;
	@Before
	public void setUp() throws Exception {
		read = Metrics.newMeter(WhenAWriteIsDone.class, "read", "jdbcread", TimeUnit.SECONDS);
		write = Metrics.newMeter(WhenAWriteIsDone.class, "write", "jdbcwrite", TimeUnit.SECONDS);
		
		rw  = new ReadAndWrites(read,write);
	}

	@Test
	public void theNumberOfWritesShouldBeIncreased() {
		rw.incWrites();
		assertThat(rw.getWrites(), is(1));
		rw.incWrites();
		rw.incWrites();
		assertThat(rw.getWrites(), is(3));
		assertThat(write.count() , is(new Long(3)));
		assertThat(rw.getReads(),is(0));
		assertThat(read.count(),is(new Long(0)));
	
	}

}
