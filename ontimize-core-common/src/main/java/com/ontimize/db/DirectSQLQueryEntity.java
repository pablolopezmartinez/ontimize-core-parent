package com.ontimize.db;

public interface DirectSQLQueryEntity extends Entity {

	/**
	 * Execute the specified sql statement against the database
	 *
	 * @param sql
	 *            SQL to execute
	 * @param sessionId
	 *            User session identifier
	 * @return
	 * @throws Exception
	 */
	public EntityResult execute(String sql, int sessionId) throws Exception;

}