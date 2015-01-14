package uk.thecodingbadgers.bDatabaseManager;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.Database.SQLDatabase;
import uk.thecodingbadgers.bDatabaseManager.Database.SQLiteDatabase;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;


/**
 * @author The Coding Badgers
 *
 * bDatabaseManager is a utility plugin for Bukkit (a Minecraft server mod).
 * Allowing easy SQL and SQLite integration for other plugins.
 * 
 */
public class bDatabaseManager extends JavaPlugin
{
	
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onLoad()
	 */
	@Override
	public void onLoad() {
		DatabaseTable.addDefaultConversions();
	}
	
	/**
	 * DatabaseType specifies all available database formats currently supported
	 * by bDatabaseManager. The enum is used during the database creation method.
	 */
	public enum DatabaseType {
		SQLite,
		SQL
	};
	
	
	/**
	 * The list of all active databases currently created on the server
	 */
	static private ArrayList<BukkitDatabase> DATABASES = new ArrayList<BukkitDatabase>();

	
	/**
	 * @param name			Name of the database.
	 * @param owner			The plugin which the database belongs too.
	 * @param type			The type of database to create.
	 * @return				The newly created database, or null if creation failed.
	 */
	static public BukkitDatabase createDatabase(String name, JavaPlugin owner, DatabaseType type) {		
		return createDatabase(name, owner, type, 20);
	}
	
	
	/**
	 * @param name			Name of the database.
	 * @param owner			The plugin which the database belongs too.
	 * @param type			The type of database to create.
	 * @param updateTime	The rate at which database queries are processed in seconds.
	 * @return				The newly created database, or null if creation failed.
	 */
	static public BukkitDatabase createDatabase(String name, JavaPlugin owner, DatabaseType type, int updateTime) {
				
		BukkitDatabase database = null;
		
		switch (type) {
		case SQLite:
			database = new SQLiteDatabase(name, owner, updateTime);
			Utilities.outputDebug("The plugin '" + owner.getName() + "' regestered an SQLite database called '" + name + "'");
			break;
			
		case SQL:
			database = new SQLDatabase(name, owner, updateTime);
			Utilities.outputDebug("The plugin '" + owner.getName() + "' regestered an SQL database called '" + name + "'");
			break;
			
		default:
			Utilities.outputError("The plugin '" + owner.getName() + "' tried to create a database of type '" + type.name() + "' which is not yet supported.");
			return null;
		}
		
		DATABASES.add(database);
		
		return database;
	}

	
	/**
	 * @param name			The name of the database to find.
	 * @param owner			The name of the plugin that the database was created by.
	 * @return				The database should it exist, else null is returned.
	 */
	static public BukkitDatabase findDatabase(String name, String pluginName) {
		
		for (BukkitDatabase dbase : DATABASES) {
			
			if (name.equalsIgnoreCase(dbase.getName())) {
				if (pluginName.equalsIgnoreCase(dbase.getOwnerName())) {
					return dbase;
				}				
			}
			
		}
		
		return null;
	}
	
}
