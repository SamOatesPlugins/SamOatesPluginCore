package uk.thecodingbadgers.bDatabaseManager.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import uk.thecodingbadgers.bDatabaseManager.Utilities;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.SQLiteDatabaseTable;
import uk.thecodingbadgers.bDatabaseManager.Thread.SQLiteThread;

/**
 * @author The Coding Badgers
 * 
 * The implementation of the SQLite database.
 *
 */
public class SQLiteDatabase extends BukkitDatabase {
	
	/**
	 * The SQLite database file.
	 */
	private File m_databaseFile = null;

	
	/**
	 * @param name			The name of the database.
	 * @param owner			The plugin which created the database.
	 * @param updateTime	The rate at which queries are executed in seconds.
	 */
	public SQLiteDatabase(String name, JavaPlugin owner, int updateTime) {
		super(name, owner, DatabaseType.SQLite, updateTime);		
				
		if (!createDatabase()) {
			Utilities.outputError("Could not create database '" + m_databaseName + "'");
			return;
		}
		
		m_thread = new SQLiteThread();
		((SQLiteThread)m_thread).setup(m_databaseFile, updateTime);
		m_thread.start();
	}
	
	/**
	 * @return	create the sqlite file on the hard drive
	 */
	private boolean createDatabase() {
		
		Utilities.outputDebug("Loading " + m_plugin.getDataFolder() + File.separator + m_databaseName + ".sqlite");
		
		m_databaseFile = new File(m_plugin.getDataFolder() + File.separator + m_databaseName + ".sqlite");
		
		if (m_databaseFile.exists()) {
			return true;
		}
		
		if (!m_plugin.getDataFolder().exists()) {
			m_plugin.getDataFolder().mkdir();
		}
		
		try {
			if (!m_databaseFile.createNewFile()) {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * @return	Get a connection to the sqlite database
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
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#query(java.lang.String, boolean)
	 */
	public void query(String query, boolean instant) {
		
		if (!instant) {
			m_thread.query(query);
		} else {
			
			final Connection connection = getConnection();
			try {
				if (connection == null) {
					Utilities.outputError("Could not connect to database '" + m_databaseName + "'");
					return;
				}
				
				m_statement = connection.createStatement();
				m_statement.executeUpdate(query);
				
				connection.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					m_statement.close();
					connection.close();
				} catch (SQLException ce) {}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#queryResult(java.lang.String)
	 */
	public ResultSet queryResult(String query) {
		
		final Connection connection = getConnection();
		try {
			
			if (connection == null) {
				Utilities.outputError("Could not connect to database '" + m_databaseName + "'");
				return null;
			}
			
			if (m_statement != null) {
				m_statement.close();
				m_statement = null;
			}
			
			m_statement = connection.createStatement();
			ResultSet result = m_statement.executeQuery(query);
			
			if (result == null) {
				m_statement.close();
				m_statement = null;
				connection.close();
			}

			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				m_statement.close();
				connection.close();
			} catch (SQLException ce) {}
		}
		
		return null;		
	}
			
	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#tableExists(java.lang.String)
	 */
	public boolean tableExists(String name) {
		
		final Connection connection = getConnection();
		ResultSet tables = null;
		
		try {
			
			if (connection == null) {
				Utilities.outputError("Could not connect to database '" + m_databaseName + "'");
				return false;
			}
			
			final DatabaseMetaData metaData = connection.getMetaData();
			tables = metaData.getTables(null, null, name, null);
			final boolean exists = tables.next();
			
			tables.close();
			connection.close();
			
			return exists;
			
		} catch (SQLException e) {
			try {
				tables.close();
				connection.close();
			} catch (SQLException ce) {
			}
			return false;
		}		
		
	}

	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {

	}

	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#login(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean login(String host, String user, String password, int port) {
		return true;
	}

	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#createTable(java.lang.String, java.lang.Class)
	 */
	@Override
	public DatabaseTable createTable(String name, Class<?> layout) {
		
		DatabaseTable table = new SQLiteDatabaseTable(this, name);
		
		if (tableExists(name)) {
			return table;
		}

		if (!table.create(layout)) {
			return null;
		}
				
		return table;
	}

}
