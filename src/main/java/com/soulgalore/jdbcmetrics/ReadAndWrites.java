package com.soulgalore.jdbcmetrics;

import com.yammer.metrics.core.Meter;

public class ReadAndWrites {
			
	private int reads = 0;
	private int writes = 0;

	public ReadAndWrites() {
	}
	
	public void incReads() {
		reads++;
	}
	
	public void incWrites() {
		writes++;
	}
	
	public int getWrites() {
		return writes;
	}
	
	public int getReads() {
		return reads;
	}
	
	
}
