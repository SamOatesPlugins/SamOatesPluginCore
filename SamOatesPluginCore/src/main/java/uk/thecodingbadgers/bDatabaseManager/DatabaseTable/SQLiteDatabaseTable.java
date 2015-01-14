package uk.thecodingbadgers.bDatabaseManager.DatabaseTable;

import uk.thecodingbadgers.bDatabaseManager.Database.SQLiteDatabase;

public class SQLiteDatabaseTable extends DatabaseTable {
	
	public SQLiteDatabaseTable(SQLiteDatabase database, String name) {
		m_name = name;
		m_database = database;
	}

}
