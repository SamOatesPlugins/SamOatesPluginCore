/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samoatesgames.samoatesplugincore.plugin;

import com.samoatesgames.samoatesplugincore.commands.PluginCommandManager;
import com.samoatesgames.samoatesplugincore.configuration.PluginConfiguration;
import com.samoatesgames.samoatesplugincore.logger.PluginLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public abstract class SamOatesPlugin extends JavaPlugin implements Listener {
    
    /**
     * The name of this plugin
     */
    protected final String m_pluginName;
    
    /**
     * The title of this plugin (used on send message)
     */
    protected final String m_pluginTitle;
    
    /**
     * The chat color to use on text output when using the plugin title
     */
    protected final ChatColor m_pluginChatColor;
                
    /**
     * The plugins command handler
     */
    protected final PluginCommandManager m_commandManager;
    
    /**
     * The plugins logger
     */
    protected final PluginLogger m_logger;
    
    /**
     * The plugins configuration
     */
    protected final PluginConfiguration m_configuration;
    
    /**
     * Class constructor
     * @param pluginName 
     */
    public SamOatesPlugin(String pluginName) {
        this(pluginName, pluginName, ChatColor.GOLD);
    }
    
        /**
     * Class constructor
     * @param pluginName 
     * @param pluginTitle 
     * @param pluginChatColor 
     */
    public SamOatesPlugin(String pluginName, String pluginTitle, ChatColor pluginChatColor) {
        m_pluginName = pluginName;
        m_pluginTitle = pluginTitle;
        m_pluginChatColor = pluginChatColor;
        m_commandManager = new PluginCommandManager(this);
        m_logger = new PluginLogger(this);
        m_configuration = new PluginConfiguration(this);
    }

    /**
     * Called when the plugin is enabled
     */
    @Override
    public void onEnable() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(this, this);
        
        // Setup plugin configuration
        setupConfigurationSettings();
        m_configuration.loadPluginConfiguration();
    }
    
    /**
     * Register all configuration settings
     */
    public abstract void setupConfigurationSettings();
    
    /**
     * Send a message to a given sender
     * @param sender
     * @param message 
     */
    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(m_pluginChatColor + "[" + m_pluginTitle + "] " + ChatColor.WHITE + message);
    }
    
    /**
     * Check to see if a sender has a given permission
     * @param sender
     * @param permission
     * @return 
     */
    public boolean hasPermission(CommandSender sender, String permission) {
        
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        
        return sender.hasPermission(permission);
    }
    
    /**
     * Log a message to the info channel
     * @param message 
     */
    public void logInfo(String message) {
        m_logger.logInfo(message);
    }
    
    /**
     * Log a message to the warning channel
     * @param message 
     */
    public void logWarning(String message) {
        m_logger.logWarning(message);
    }
    
    /**
     * Log a message to the error channel
     * @param message 
     */
    public void logError(String message) {
        m_logger.logError(message);
    }
    
    /**
     * Log a message and exception to the error channel
     * @param message 
     * @param ex 
     */
    public void logException(String message, Exception ex) {
        m_logger.logException(message, ex);
    }
        
    /**
     * Register a setting with the configuration system
     * @param key
     * @param defaultValue 
     */
    public void registerSetting(String key, Object defaultValue) {
        m_configuration.registerSetting(key, defaultValue);
    }
    
    /**
     * 
     * @param <T>
     * @param key
     * @param defaultValue
     * @return 
     */
    public <T> T getSetting(String key, T defaultValue) {
        return m_configuration.getSetting(key, defaultValue);
    }
}
