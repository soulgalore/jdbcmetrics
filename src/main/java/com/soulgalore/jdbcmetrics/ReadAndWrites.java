package com.soulgalore.jdbcmetrics;


public class ReadAndWrites {

	private int reads = 0;
	private int writes = 0;

	public ReadAndWrites(int reads, int writes) {
		this.reads = reads;
		this.writes = writes;
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
