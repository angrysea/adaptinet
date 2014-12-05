package com.db.ess.synthesis.processor.tasks;

public interface NonDaoTask<T> {
    public void run(T response) throws Exception;    	
}
