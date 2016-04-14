package net.lyxnx.chestshop;

import lombok.Getter;
import net.lyxnx.chestshop.config.ConfigDefaults;
import net.lyxnx.chestshop.config.ConfigWrapper;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestShop extends JavaPlugin {
    
    @Getter
    private static ChestShop instance;

    @Getter
    private ConfigWrapper config;

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void loadConfig() {
        config = new ConfigWrapper(this, "config.yml", "");
        config.createNewFile("Initialising ChestShop config...", "ChestShop-5 Configuration");

        for(final ConfigDefaults def : ConfigDefaults.values()) {
            config.getConfig().addDefault(def.getPath().toUpperCase(), def.getDefault());
        }

        config.getConfig().options().copyDefaults(true);
        config.saveConfig();
    }
}