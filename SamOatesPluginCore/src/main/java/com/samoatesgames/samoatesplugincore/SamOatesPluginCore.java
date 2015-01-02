package com.samoatesgames.samoatesplugincore;

import com.samoatesgames.samoatesplugincore.commands.PluginCommandManager;
import com.samoatesgames.samoatesplugincore.plugin.SamOatesPlugin;

/**
 * The main class for creating helper interfaces
 * @author Sam Oates <sam@samoatesgames.com>
 */
public final class SamOatesPluginCore {
    
    /**
     * 
     * @param plugin
     * @return 
     */
    public static PluginCommandManager createPluginCommandManager(SamOatesPlugin plugin) {
        PluginCommandManager commandManager = new PluginCommandManager(plugin);
        return commandManager;
    }
    
}
