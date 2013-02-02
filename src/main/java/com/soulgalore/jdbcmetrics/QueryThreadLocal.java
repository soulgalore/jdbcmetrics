package com.soulgalore.jdbcmetrics;

import com.yammer.metrics.core.Meter;

public abstract class QueryThreadLocal {

	private static final ThreadLocal<ReadAndWrites> nrOfQueries = new ThreadLocal<ReadAndWrites>();
	
	public static ReadAndWrites getNrOfQueries(){
        return nrOfQueries.get();
    }
	
	// TODO depending on this is executed first, change this! :)
	public static void setMeters(Meter readMeter, Meter writeMeter) {
		nrOfQueries.set(new ReadAndWrites(readMeter, writeMeter));
	}
	
	 public static void addRead() {
		 if (nrOfQueries.get()!=null) 
			 nrOfQueries.get().incReads();
	 }
	 
	 public static void addWrite() {
		 if (nrOfQueries.get()!=null)
			 nrOfQueries.get().incWrites();
	 }

    public static void removeNrOfQueries(){
    	 nrOfQueries.remove();
    }

	
}
