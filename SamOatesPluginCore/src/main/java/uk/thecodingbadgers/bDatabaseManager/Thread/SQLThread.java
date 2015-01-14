package uk.thecodingbadgers.bDatabaseManager.Thread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import uk.thecodingbadgers.bDatabaseManager.Utilities;
import uk.thecodingbadgers.bDatabaseManager.Database.SQLDatabase;

/**
 * @author The Coding Badgers
 * 
 * The implementation of the SQL execution thread.
 *
 */
public class SQLThread extends DatabaseThread {
	
	/**
	 * The options of the SQL database.
	 */
	SQLDatabase.SQLOptions m_options = null;;
	
	
	/**
	 * @param database		The name of the database.
	 * @param options		The options associated with the database.
	 * @param updateTime	The rate at which the query thread executes in seconds.
	 */
	public void setup(String database, SQLDatabase.SQLOptions options, int updateTime) {
		super.setup(updateTime);
		m_options = options;
		m_options.databaseName = database;
	}
	
	
	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Thread.DatabaseThread#login(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public void login(String host, String user, String password, int port) {
		m_options.host = host;
		m_options.username = user;
		m_options.password = password;
		m_options.port = port;
	}
	
	
	/**
	 * @return 	Get a connection to the SQL database.
	 */
	private Connection getConnection() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			Utilities.outputError("Could not find sql drivers");
			return null;
		}
		
		try {
			return DriverManager.getConnection(
				"jdbc:mysql://" + m_options.host + ":" + m_options.port + "/" +
				m_options.databaseName + "?" +
				"user=" + m_options.username +
				"&password=" + m_options.password
			);
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
			
			Connection connection = getConnection();
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

}
