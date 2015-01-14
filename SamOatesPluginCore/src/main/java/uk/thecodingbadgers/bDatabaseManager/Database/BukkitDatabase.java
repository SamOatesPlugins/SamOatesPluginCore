package uk.thecodingbadgers.bDatabaseManager.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;
import uk.thecodingbadgers.bDatabaseManager.Thread.DatabaseThread;

import org.bukkit.plugin.java.JavaPlugin;


/**
 * @author The Coding Badgers
 * 
 * The base abstract database class.
 *
 */
public abstract class BukkitDatabase {
	
	/**
	 * The thread which will process the queries.
	 */
	protected DatabaseThread 	m_thread = null;
	
	/**
	 * The plugin which the database belongs too.
	 */
	protected JavaPlugin 		m_plugin = null;
	
	/**
	 * The SQL statement, used in both SQL and SQLite
	 */
	protected Statement 		m_statement = null;
	
	/**
	 * The name of the database
	 */
	protected String 			m_databaseName = null;
	
	/**
	 * The rate at which the query thread executes in seconds
	 */
	protected int 				m_updateTime = 20;
	
	
	/**
	 * @param name				The name of the database
	 * @param owner				The plugin which created the database
	 * @param type				The type of database this represents
	 * @param updateTime		The rate at which the query thread executes in seconds
	 */
	public BukkitDatabase(String name, JavaPlugin owner, DatabaseType type, int updateTime) {
		m_plugin = owner;
		m_databaseName = name;
		m_updateTime = updateTime;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected abstract void finalize() throws Throwable;

	
	/**
	 * @param host				The host address where the database resides.
	 * @param user				The user account to login to the database with.
	 * @param password			The password of the database user account.
	 * @param port				The port of which the database is running on.
	 * @return					True if login was successful, false otherwise.
	 */
	public abstract boolean login(String host, String user, String password, int port);
	
	
	/**
	 * @param query				The database query to execute.
	 * @param instant			Should the command be processed instantly, or be executed by the processing thread.
	 */
	public abstract void query(String query, boolean instant);
	
	
	/**
	 * @param query				Execute a query which returns a result.
	 * @return					A ResultSet containing the result of the database query.
	 */
	public abstract ResultSet queryResult(String query);
	
	
	/**
	 * @param name				The name of a table to check.
	 * @return					True if a table with the given name exists, false otherwise.
	 */
	public abstract boolean tableExists(String name);
	
	
	/**
	 * @return A new database table, or the table should it already exist. Should creation fail, null is returned.
	 */
	public abstract DatabaseTable createTable(String name, Class<?> layout);
	
	/**
	 * @param query				Add a query to the database thread.
	 */
	public void query(String query) {
		query(query, false);
	}
	
	
	/**
	 * @param result			Safely release a ResultSet after it has been used.
	 */
	public void freeResult(ResultSet result) {
		
		try {
			
			if (result != null) {
				result.close();
				result = null;
			}
			
			if (m_statement != null) {
				Connection connection = m_statement.getConnection();
				m_statement.close();
				connection.close();
				m_statement = null;
				connection = null;				
			}			
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 	Release all database resources
	 */
	public void freeDatabase() {
		
		try {
			if (m_thread != null) {
				m_thread.kill();
				m_thread = null;			
			}
			
			if (m_statement != null) {
				m_statement.close();
				m_statement = null;
			}	
		} catch (SQLException e) {}
		
	}
	
	
	/**
	 * @return	Get the name of the database.
	 */
	public String getName() {
		return m_databaseName;
	}
	
	
	/**
	 * @return	Get the name of the plugin which this database was created by.
	 */
	public String getOwnerName() {
		return m_plugin.getName();
	}

}
