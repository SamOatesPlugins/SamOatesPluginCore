package uk.thecodingbadgers.bDatabaseManager.DatabaseTable;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.Utilities;

public abstract class DatabaseTable {
	
	/**
	 * 
	 */
	protected BukkitDatabase m_database = null;
	
	/**
	 * 
	 */
	protected String m_name = null;
	
	/**
	 * 
	 */
	private static HashMap<String, String> TYPECONVERSION = new HashMap<String, String>();
	
	
	/**
	 * @param name
	 * @param layout
	 * @return
	 */
	public boolean create(Class<?> layout) {
		
		Field[] publicFields = layout.getFields();

		if (publicFields.length == 0) {
			Utilities.outputError("The given table layout '" + layout.getName() + "' for the table '" + m_name + "' has no public fields.");
			return false;
		}
				
		String createTable = "CREATE TABLE `" + m_name + "` (";
		for (Field field : publicFields) {			
			final String fieldName = field.getName();
			final String fieldType = convertType(field.getType());	
			
			if (fieldType == null) {
				Utilities.outputError("The given table layout '" + layout.getName() + "' for the table '" + m_name + "' has unknown type '" + field.getType().getSimpleName() + "'.");
				return false;
			}
			
			createTable += fieldName + " " + fieldType + ",";			
		}
		createTable = createTable.substring(0, createTable.length() - 1);
		createTable += ")";

		m_database.query(createTable, true);
		
		Utilities.outputDebug("Created table `" + m_name + "` for plugin " + m_database.getOwnerName());
		return true;
	}
	
	
	/**
	 * @param data
	 * @param layout
	 * @param instant
	 * @return
	 */
	public boolean insert(DatabaseTableData data, Class<?> layout, boolean instant) {
		
		Field[] publicFields = layout.getFields();
		
		String fields = "(";
		String values = "VALUES (";

		for (Field field : publicFields) {
			
			fields += field.getName() + ",";
			try {
				if (field.getType().isEnum()) {
					values += "\"" + ((Enum<?>)field.get(data)).ordinal() + "\",";
				}
				values += "\"" + field.get(data).toString() + "\",";
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		fields = fields.substring(0, fields.length() - 1) + ") ";
		values = values.substring(0, values.length() - 1) + ");";
		
		String insertQuery = "INSERT INTO `" + m_name + "` " + fields + values;
		
		m_database.query(insertQuery, instant);
		
		return true;
	}
	
	
	/**
	 * @param what
	 * @return
	 */
	public ResultSet select(String what) {
		String selectQuery = "SELECT `" + what + "` FROM '" + m_name + "'";
		return m_database.queryResult(selectQuery);
	}
		
	
	/**
	 * @param what
	 * @param where
	 * @return
	 */
	public ResultSet select(String what, String where) {
		String selectQuery = "SELECT `" + what + "` FROM '" + m_name + "' WHERE " + where;
		return m_database.queryResult(selectQuery);
	}
    
    /**
	 * @param where
	 * @return
	 */
	public ResultSet selectAll(String where) {
		String selectQuery = "SELECT * FROM '" + m_name + "' WHERE " + where;
		return m_database.queryResult(selectQuery);
	}
	
	
	/**
	 * @param data
	 * @param layout
	 * @param where
	 * @param instant
	 */
	public void update(DatabaseTableData data, Class<?> layout, String where, boolean instant) {
        
        if (!this.exists(where)) {
            this.insert(data, layout, true);
            return;
        }
        
		Field[] publicFields = layout.getFields();
		
		String fields = "";
		
		for (Field field : publicFields) {
			try {
				fields += field.getName() + "='" + field.get(data).toString() + "',";
			} 
			catch (Exception e) {}
		}
		
		fields = fields.substring(0, fields.length() - 1);
		
		String updateQuery = "UPDATE `" + m_name + "` SET " + fields + " WHERE " + where;		
		m_database.query(updateQuery, instant);	
	}
    
    /**
     * 
     * @param where
     * @return 
     */
    public boolean exists(String where) {
        String query = "SELECT * FROM '" + m_name + "' WHERE " + where;
        ResultSet result = m_database.queryResult(query);        
        if (result == null) {
            return false;
        }

        try {
            boolean exists = result.next();
            return exists;
        }
        catch (SQLException ex) {
            return false;
        }
    }
	
	/**
	 * @param class1
	 * @return
	 */
	protected String convertType(Class<?> clazz) {	
		if (clazz.isEnum()) {
			return "INT";
		}
		
		return TYPECONVERSION.get(clazz.getSimpleName().toLowerCase());
	}
	
	
	/**
	 * 
	 */
	public static void addDefaultConversions() {
		addTypeConversion("int", "INT");
		addTypeConversion("long", "BIGINT");
		addTypeConversion("double", "DOUBLE");
		addTypeConversion("float", "FLOAT");
		addTypeConversion("char", "VARCHAR");
		addTypeConversion("string", "TEXT");
	}
	
	
	/**
	 * @param javaType
	 * @param databaseType
	 */
	public static void addTypeConversion(String javaType, String databaseType) {
		if (TYPECONVERSION.containsKey(javaType)) {
			TYPECONVERSION.remove(javaType);
		}
		TYPECONVERSION.put(javaType, databaseType);
	}

}
