package com.samoatesgames.samoatesplugincore.commands;

import org.bukkit.command.CommandSender;

/**
 * The interface for all command handlers
 * @author Sam Oates <sam@samoatesgames.com>
 */
public interface ICommandHandler {
    
    /**
     * Execute this command
     * @param manager
     * @param sender
     * @param arguments
     * @return 
     */
    boolean execute(PluginCommandManager manager, CommandSender sender, String[] arguments);
    
}
