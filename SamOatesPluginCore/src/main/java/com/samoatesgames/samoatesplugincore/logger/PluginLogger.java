package com.samoatesgames.samoatesplugincore.logger;

import com.samoatesgames.samoatesplugincore.plugin.SamOatesPlugin;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class PluginLogger {
    
    /**
     * The owner plugin of this logger
     */
    private final SamOatesPlugin m_plugin;
    
    /**
     * The actual logger instance
     */
    private final Logger m_logger;
    
    /**
     * Class constructor
     * @param plugin 
     */
    public PluginLogger(SamOatesPlugin plugin) {
        m_plugin = plugin;
        m_logger = m_plugin.getLogger();
    }
    
    /**
     * Log a message to the info channel
     * @param message 
     */
    public void LogInfo(String message) {
        m_logger.log(Level.INFO, message);
    }
    
    /**
     * Log a message to the warning channel
     * @param message 
     */
    public void LogWarning(String message) {
        m_logger.log(Level.WARNING, message);
    }
    
    /**
     * Log a message to the error channel
     * @param message 
     */
    public void LogError(String message) {
        m_logger.log(Level.SEVERE, message);
    }
}
