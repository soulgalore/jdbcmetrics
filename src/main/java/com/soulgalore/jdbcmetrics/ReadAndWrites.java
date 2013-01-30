package com.soulgalore.jdbcmetrics;

import com.yammer.metrics.core.Meter;



public class ReadAndWrites {
			

	final Meter meterWrites;
	final Meter meterReads;
	private int reads = 0;
	private int writes = 0;

	public ReadAndWrites(Meter meterReads, Meter meterWrites) {

		this.meterReads = meterReads;
		this.meterWrites = meterWrites;
	}
	
	public void incReads() {
		meterReads.mark();
		reads++;
	}
	
	public void incWrites() {
		meterWrites.mark();
		writes++;
	}
	
	public int getWrites() {
		return writes;
	}
	
	public int getReads() {
		return reads;
	}
	
	
}
