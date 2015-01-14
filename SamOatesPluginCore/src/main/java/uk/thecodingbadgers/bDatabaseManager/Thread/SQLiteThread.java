package uk.thecodingbadgers.bDatabaseManager.Thread;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import uk.thecodingbadgers.bDatabaseManager.Utilities;

/**
 * @author The Coding Badgers
 * 
 * The implementation of the SQLite execution thread.
 *
 */
public class SQLiteThread extends DatabaseThread {
	
	/**
	 * The file that represents the SQLite database file.
	 */
	private File m_databaseFile = null;
	
	
	/**
	 * @param database		The file that represents the SQLite database file.
	 * @param updateTime	The rate at which the query thread executes in seconds.
	 */
	public void setup(File database, int updateTime) {
		m_databaseFile = database;
		super.setup(updateTime);
	}
	
	
	/**
	 * @return	Get a connection to the SQLite database.
	 */
	private Connection getConnection() {
		
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			Utilities.outputError("Could not find sqlite drivers");
			return null;
		}
		
		try {
			return DriverManager.getConnection("jdbc:sqlite:" + m_databaseFile.getAbsolutePath());
		} catch (SQLException e) {
			return null;
		}
		
	}

	
	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Thread.DatabaseThread#executeQuieries()
	 */
	protected synchronized void executeQuieries() {
		
		if (!m_queries.isEmpty())
		{		
			String query = null;
			Statement statement = null;
			
			final Connection connection = getConnection();
			try {	
				if (connection == null) {
					Utilities.outputError("Could not connect to database.");
					return;
				}
			
				statement = connection.createStatement();
				
				Iterator<String> queryItr = m_queries.iterator();
				while (queryItr.hasNext()) {
					query = queryItr.next();
					statement.addBatch(query);	
				}		
				statement.executeBatch();

				statement.close();
				connection.close();
				m_queries.clear();
				
			} catch (SQLException e) {
				final String errorMessage = e.getMessage().toLowerCase();
				if (!(errorMessage.contains("locking") || errorMessage.contains("locked"))) {
					Utilities.outputError("Batch Exception: Exception whilst executing");
					Utilities.outputError(query);
					e.printStackTrace();
					m_queries.remove(query);
				}	
				try {
					statement.close();
					connection.close();
				} catch (SQLException ce) {}
			}		
		}				
	}
	

	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Thread.DatabaseThread#login(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public void login(String host, String dbname, String user, int port) {}
	
}
