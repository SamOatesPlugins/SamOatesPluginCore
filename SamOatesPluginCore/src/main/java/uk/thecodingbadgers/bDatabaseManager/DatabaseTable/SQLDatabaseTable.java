package uk.thecodingbadgers.bDatabaseManager.DatabaseTable;

import uk.thecodingbadgers.bDatabaseManager.Database.SQLDatabase;

public class SQLDatabaseTable extends DatabaseTable {
	
	public SQLDatabaseTable(SQLDatabase database, String name) {
		m_name = name;
		m_database = database;
	}

}
