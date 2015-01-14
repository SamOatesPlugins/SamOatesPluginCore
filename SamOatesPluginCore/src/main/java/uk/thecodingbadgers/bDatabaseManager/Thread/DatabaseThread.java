package uk.thecodingbadgers.bDatabaseManager.Thread;

import java.util.ArrayList;


/**
 * @author The Coding Badgers
 *
 * The database query execution thread.
 * Allowing blocking methods to not stall the server.
 *
 */
public abstract class DatabaseThread extends Thread {
	
	
	/**
	 * The current list of queries to be executed.
	 */
	protected ArrayList<String> 	m_queries = new ArrayList<String>();
	
	/**
	 * The current running state of the thread.
	 */
	protected boolean 				m_running = false;
	
	/**
	 * The rate at which the query thread executes in seconds.
	 */
	protected int 					m_updateTime = 20;
	
	
	/**
	 * Class constructor.
	 */
	public DatabaseThread() {}
	
	
	/**
	 * @param host		The host address where the database resides.
	 * @param user		The user account to login to the database with.
	 * @param password	The password of the database user account.
	 * @param port		The port of which the database is running on.
	 */
	public abstract void login(String host, String dbname, String user, int port);
	
	
	/**
	 * Execute all queries that currently exist.
	 */
	protected abstract void executeQuieries();
	
	
	/**
	 * Tell the thread to stop executing.
	 */
	public void kill() {
		m_running = false;
	}
	

	/**
	 * @param updateTime	The rate at which the query thread executes in seconds.
	 */
	public void setup(int updateTime) {
		m_running = true;
		m_updateTime = updateTime;
	}
	
	
	/**
	 * @param query		The query to add to the execution list.
	 */
	public synchronized void query(String query) {
		m_queries.add(query);
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run () {
		
		m_running = true;
		
		while (m_running) {
			
			synchronized(this) {
				executeQuieries();
			}
			
			try {
				Thread.sleep(m_updateTime * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		synchronized(this) {
			executeQuieries();
		}
	}
}
