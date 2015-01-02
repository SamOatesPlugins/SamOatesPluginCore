package com.samoatesgames.samoatesplugincore.commands;

import com.samoatesgames.samoatesplugincore.plugin.SamOatesPlugin;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class PluginCommandManager {
    
    /**
     * The plugin which owns this command manager
     */
    private final SamOatesPlugin m_plugin;
    
    /**
     * All registered commands
     */
    private final Map<String, ICommandHandler> m_commands = new HashMap<String, ICommandHandler>();
    
    /**
     * Class constructor
     * @param plugin    The plugin which owns this command manager
     */
    public PluginCommandManager(SamOatesPlugin plugin) {
        m_plugin = plugin;
    }
    
    /**
     * Registers a handler and its command to the command manager
     * @param command
     * @param handler 
     */
    public void registerCommandHandler(String command, ICommandHandler handler) {
        
        command = command.toLowerCase();
        if (m_commands.containsKey(command)) {
            // TODO Log warning of overwrite
        }
        
        m_commands.put(command, handler);
    }
    
    /**
     * Forward a send message command to the owner plugin
     * @param sender
     * @param message 
     */
    public void sendMessage(CommandSender sender, String message) {
        m_plugin.sendMessage(sender, message);
    }
    
    /**
     * Forward a has permission request to the owner plugin
     * @param sender
     * @param permission
     * @return 
     */
    public boolean hasPermission(CommandSender sender, String permission) {
        return m_plugin.hasPermission(sender, permission);
    }
    
    /**
     * Public access to the owner plugin
     * @return 
     */
    public SamOatesPlugin getPlugin() {
        return m_plugin;
    }
 
    /**
     * Handle commands.
     * If we have a registered command with that name execute it.
     * @param sender    The sender of the command
     * @param cmd       The command being executed
     * @param label     The commands label
     * @param args      The arguments passed with the command
     * @return          True if handled, false otherwise
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        final String command = label.toLowerCase();
        if (m_commands.containsKey(command)) {
            ICommandHandler commandHandler = m_commands.get(command);
            return commandHandler.execute(this, sender, args);
        }
        
        return false;
    }
    
}
