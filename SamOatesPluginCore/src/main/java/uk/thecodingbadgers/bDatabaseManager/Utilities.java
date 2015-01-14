package uk.thecodingbadgers.bDatabaseManager;

import org.bukkit.Bukkit;


/**
 * @author The Coding Badgers
 *
 * Any commonly used methods
 *
 */
public class Utilities {
	
	
	/**
	 * @param message		The message to output as an error to the console
	 */
	public static void outputError(String message) {
		Bukkit.getLogger().severe("[BDM] " + message + ".");
	}
	
	
	/**
	 * @param message		The message to output to the console
	 */
	public static void outputDebug(String message) {
		Bukkit.getLogger().info("[BDM] " + message + ".");
	}

}
