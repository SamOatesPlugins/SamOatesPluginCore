/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samoatesgames.samoatesplugincore.commands;

import org.bukkit.command.CommandSender;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public abstract class BasicCommandHandler implements ICommandHandler {

    /**
     * The base permission for this command
     */
    private final String m_basePermission;
    
    /**
     * Class constructor
     * @param basePermission 
     */
    public BasicCommandHandler(String basePermission) {
        m_basePermission = basePermission;
    }
    
    /**
     * Public access to the commands base permission
     * @return 
     */
    public String getPermission() {
        return m_basePermission;
    }
    
    /**
     * 
     * @param manager
     * @param sender
     * @param arguments
     * @return 
     */
    public abstract boolean execute(PluginCommandManager manager, CommandSender sender, String[] arguments);
}
