package uk.thecodingbadgers.bDatabaseManager.Database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.thecodingbadgers.bDatabaseManager.Utilities;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.SQLDatabaseTable;
import uk.thecodingbadgers.bDatabaseManager.Thread.SQLThread;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author The Coding Badgers
 *
 * The implementation of the SQL database.
 *
 */
public class SQLDatabase extends BukkitDatabase {
	
	/**
	 * SQL specific options
	 */
	public class SQLOptions {
		public String	host;
		public String 	databaseName;
		public String 	username;
		public String 	password;
		public int 		port;
	}
	
	/**
	 * The SQL database options
	 */
	private SQLOptions m_options = new SQLOptions();

	
	/**
	 * @param name			The name of the database.
	 * @param owner			The plugin which created the database.
	 * @param updateTime	The rate at which queries are executed in seconds.
	 */
	public SQLDatabase(String name, JavaPlugin owner, int updateTime) {
		super(name, owner, DatabaseType.SQL, updateTime);
		m_options.databaseName = name;
		
		SQLThread thread = new SQLThread();
		thread.setup(name, m_options, updateTime);
		m_thread = thread;
		m_thread.start();
	}
	
	
	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#login(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public boolean login(String host, String user, String password, int port) {
		m_options.host = host;
		m_options.username = user;
		m_options.password = password;
		m_options.port = port;
		
		m_thread.login(host, user, password, port);
		
		return getConnection() != null;
	}
	
	/**
	 * @return	An SQL connection to the database.
	 */
	private Connection getConnection() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			Utilities.outputError("Could not find sql drivers");
			return null;
		}
		
		try {
			Connection connection = DriverManager.getConnection(
				"jdbc:mysql://" + m_options.host + ":" + m_options.port + "/" +
				m_options.databaseName + "?" +
				"user=" + m_options.username +
				"&password=" + m_options.password
			);
			
			if (connection == null) {
				Utilities.outputError("Could not connect to database '" + m_databaseName + "' using '" + m_options.username + "'@'" + m_options.host + ":" + m_options.port + "' using password '" + (m_options.password.length() == 0 ? "NO" : "YES") + "'");
			}
			
			return connection;
	
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
			try {
				
				final Connection connection = getConnection();
				if (connection == null) {
					return;
				}
				
				m_statement = connection.createStatement();
				m_statement.executeUpdate(query);
				
				connection.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#queryResult(java.lang.String)
	 */
	public ResultSet queryResult(String query) {
				
		try {
			final Connection connection = getConnection();
			if (connection == null) {
				Utilities.outputError("Could not connect to database '" + m_databaseName + "' using '" + m_options.username + "'@'" + m_options.host + ":" + m_options.port + "' using password '" + (m_options.password.length() == 0 ? "NO" : "YES") + "'");
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
			return null;
		}
	}
			
	
	/* (non-Javadoc)
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#tableExists(java.lang.String)
	 */
	public boolean tableExists(String name) {
		
		try {
			final Connection connection = getConnection();
			if (connection == null) {
				Utilities.outputError("Could not connect to database '" + m_databaseName + "' using '" + m_options.username + "'@'" + m_options.host + ":" + m_options.port + "' using password '" + (m_options.password.length() == 0 ? "NO" : "YES") + "'");
				return false;
			}
			
			final DatabaseMetaData metaData = connection.getMetaData();
			final ResultSet tables = metaData.getTables(null, null, name, null);
			final boolean exists = tables.next();
			
			tables.close();
			connection.close();
			
			return exists;
			
		} catch (SQLException e) {
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
	 * @see uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase#createTable(java.lang.String, java.lang.Class)
	 */
	@Override
	public DatabaseTable createTable(String name, Class<?> layout) {
		
		DatabaseTable table = new SQLDatabaseTable(this, name);
		
		if (tableExists(name)) {
			return table;
		}

		if (!table.create(layout)) {
			return null;
		}
				
		return table;
	}

}
