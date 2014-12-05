package com.db.ess.synthesis.dao.tasks;

public interface DaoTask<T> {
	 public void run(T response) throws Exception;    	
}
