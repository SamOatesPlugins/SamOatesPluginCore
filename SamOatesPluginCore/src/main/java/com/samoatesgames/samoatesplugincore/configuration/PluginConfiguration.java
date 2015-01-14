package com.samoatesgames.samoatesplugincore.configuration;

import com.samoatesgames.samoatesplugincore.plugin.SamOatesPlugin;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class PluginConfiguration {
    
    /**
     * The owner plugin of this configuration
     */
    private final SamOatesPlugin m_plugin;
    
    /**
     * All registered settings
     */
    private final Map<String, Object> m_registeredSettings = new HashMap<String, Object>();
    
    /**
     * Class constructor
     * @param plugin 
     */
    public PluginConfiguration(SamOatesPlugin plugin) {
        m_plugin = plugin;
    }
    
    /**
     * Register a setting with the configuration system
     * @param key
     * @param defaultValue 
     */
    public void registerSetting(String key, Object defaultValue) {
        m_registeredSettings.remove(key);
        m_registeredSettings.put(key, defaultValue);
    }
    
    /**
     * Load a given configuration file
     * @param configFile
     */
    public void loadPluginConfiguration(File configFile) {
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        
        // Merge the registered config values and the loaded config values
        Map<String, Object> mergedSettings = new HashMap<String, Object>();
        for (Entry<String, Object> setting : m_registeredSettings.entrySet()) {
            Object mergedValue = config.get(setting.getKey(), setting.getValue());
            mergedSettings.put(setting.getKey(), mergedValue);
        }
        
        // Copy the merged values to the registered map, which should now 
        // contain all registered and loaded settings and values
        m_registeredSettings.clear();
        m_registeredSettings.putAll(mergedSettings);
        
        // Save out the config with the correct settings
        for (Entry<String, Object> setting : m_registeredSettings.entrySet()) {
            config.set(setting.getKey(), setting.getValue());
        }
        
        try {
            config.save(configFile);     
        }
        catch (Exception ex) {
            m_plugin.logException("Failed to save plugin configuration '" + configFile.getAbsolutePath() + "'", ex);
        }
    }
    
    /**
     * Load the plugin configuration, merging registered and loaded values
     */
    public void loadPluginConfiguration() {        
        File pluginConfig = new File(m_plugin.getDataFolder(), "config.yml");
        loadPluginConfiguration(pluginConfig);
    }
    
    /**
     * Get the current value of a current setting
     * @param <T>
     * @param key
     * @param defaultValue
     * @return 
     */
    public <T> T getSetting(String key, T defaultValue) {
        
        if (!m_registeredSettings.containsKey(key)) {
            m_plugin.logError("No config setting with the key '" + key + "' is registered.");
            return defaultValue;
        }
        
        Object value = m_registeredSettings.get(key);
        
        try {
            return (T)value;
        }
        catch (Exception ex) {
            m_plugin.logException("The setting '" + key + "' is not of the correct type.", ex);
            return defaultValue;
        }
    }
    
    /**
     * Set the current value of a setting
     * @param <T>
     * @param key
     * @param value
     */
    public <T> void setSetting(String key, T value) {
        
        if (!m_registeredSettings.containsKey(key)) {
            m_plugin.logError("No config setting with the key '" + key + "' is registered.");
            return;
        }
        
        m_registeredSettings.remove(key);
        m_registeredSettings.put(key, value);
    }
    
    /**
     * 
     */
    public void saveSettings() {
        File pluginConfig = new File(m_plugin.getDataFolder(), "config.yml");
        saveSettings(pluginConfig);        
    }
    
    /**
     * 
     * @param configFile 
     */
    public void saveSettings(File configFile) {
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        
        for (Entry<String, Object> setting : m_registeredSettings.entrySet()) {
            config.set(setting.getKey(), setting.getValue());
        }
        
        try {
            config.save(configFile);     
        }
        catch (Exception ex) {
            m_plugin.logException("Failed to save plugin configuration '" + configFile.getAbsolutePath() + "'", ex);
        }        
    }
}
