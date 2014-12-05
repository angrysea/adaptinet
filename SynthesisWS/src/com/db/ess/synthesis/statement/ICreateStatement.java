package com.db.ess.synthesis.statement;

import java.sql.PreparedStatement;
import java.util.List;

public interface ICreateStatement 
{
	public List<PreparedStatement> createStatement()  throws Exception;
	
}
