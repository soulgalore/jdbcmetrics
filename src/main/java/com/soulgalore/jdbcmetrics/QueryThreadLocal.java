package com.soulgalore.jdbcmetrics;

public abstract class QueryThreadLocal {

	private static final ThreadLocal<ReadAndWrites> nrOfQueries = new ThreadLocal<ReadAndWrites>();
	
	public static ReadAndWrites getNrOfQueries(){
        return nrOfQueries.get();
    }
	
	 public static void addRead() {
		 if (nrOfQueries.get()==null)
			 nrOfQueries.set(new ReadAndWrites(1,0));
		 else nrOfQueries.get().incReads();
	 }
	 
	 public static void addWrite() {
		 if (nrOfQueries.get()==null)
			 nrOfQueries.set(new ReadAndWrites(0,1));
		 else nrOfQueries.get().incWrites();
	 }

    public static void removeNrOfQueries(){
        nrOfQueries.remove();
    }

	
}
