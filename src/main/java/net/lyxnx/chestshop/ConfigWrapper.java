package net.lyxnx.chestshop;

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

    public ConfigWrapper(final JavaPlugin plugin, final String fileName, final String filePath) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void createNewFile(final String message, final String header) {
        reloadConfig();
        saveConfig();
        loadConfig(header);

        if(message != null) {
            plugin.getLogger().info(message);
        }
    }

    public FileConfiguration getConfig() {
        if(config == null) {
            reloadConfig();
        }
        return config;
    }

    public void loadConfig(final String header) {
        config.options().header(header);
        config.options().copyDefaults(true);
        saveConfig();
    }

    public void reloadConfig() {
        if(file == null) {
            file = new File(plugin.getDataFolder() + filePath.replace("/", File.separator), fileName);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

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