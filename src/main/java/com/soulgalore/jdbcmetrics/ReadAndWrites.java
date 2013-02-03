package com.soulgalore.jdbcmetrics;

/**
 * Hold read & writes per request. The class is not thread safe but is used within a ThreadLocal.
 *
 */
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
	
	public void clear() {
		reads = 0;
		writes = 0;
	}
	
}
