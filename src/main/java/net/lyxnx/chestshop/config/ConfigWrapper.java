package net.lyxnx.chestshop.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigWrapper {

    private final JavaPlugin plugin;
    private final String fileName, filePath;

    private File file;
    private FileConfiguration config;

    /**
     * Creates an instance of a new file to load, reload, get values from or to create.
     * @param plugin The main class which extends {@link JavaPlugin}.
     * @param fileName The name of the file. (File extensions (eg .yml) can be used).
     * @param filePath The path of the file. ('/' will be replaced with {@link File#separator}).
     */
    public ConfigWrapper(final JavaPlugin plugin, final String fileName, final String filePath) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    /**
     * Creates the file with a logging message or header.
     * @param message The message to log when creating the file.
     * @param header The header to add to the top of the file.
     */
    public void createNewFile(final String message, final String header) {
        reloadConfig();
        saveConfig();
        loadConfig(header);

        if(message != null) {
            plugin.getLogger().info(message);
        }
    }

    /**
     * Returns this instance as a {@link FileConfiguration}.
     * @return {@link FileConfiguration} representation of this instance.
     */
    public FileConfiguration getConfig() {
        if(config == null) {
            reloadConfig();
        }
        return config;
    }

    /**
     * Initially loads the config file with an optional header.
     * @param header The header to add to the top of the config file.
     */
    public void loadConfig(final String header) {
        config.options().header(header);
        config.options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Reloads the config file.<br>
     * This method simply overwrites the current file in memory.<br>
     * {@link #saveConfig()} should be used first.
     */
    public void reloadConfig() {
        if(file == null) {
            file = new File(plugin.getDataFolder() + filePath.replace("/", File.separator), fileName);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves the current instance to a config file.
     */
    public void saveConfig() {
        if(config == null || file == null) {
            return;
        }

        try {
            getConfig().save(file);
        } catch(final IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Error saving config to " + file, ex);
        }
    }
}