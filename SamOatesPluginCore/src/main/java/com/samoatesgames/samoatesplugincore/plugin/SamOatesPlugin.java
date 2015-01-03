/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samoatesgames.samoatesplugincore.plugin;

import com.samoatesgames.samoatesplugincore.SamOatesPluginCore;
import com.samoatesgames.samoatesplugincore.commands.PluginCommandManager;
import com.samoatesgames.samoatesplugincore.logger.PluginLogger;
import java.util.logging.Level;
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
public class SamOatesPlugin extends JavaPlugin implements Listener {
    
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
        m_commandManager = SamOatesPluginCore.createPluginCommandManager(this);
        m_logger = new PluginLogger(this);
    }
    
    /**
     * Called when the plugin is enabled
     */
    @Override
    public void onEnable() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(this, this);
    }
    
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
    public void LogInfo(String message) {
        m_logger.LogInfo(message);
    }
    
    /**
     * Log a message to the warning channel
     * @param message 
     */
    public void LogWarning(String message) {
        m_logger.LogWarning(message);
    }
    
    /**
     * Log a message to the error channel
     * @param message 
     */
    public void LogError(String message) {
        m_logger.LogError(message);
    }
    
}
