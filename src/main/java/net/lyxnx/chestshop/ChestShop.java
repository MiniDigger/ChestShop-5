package net.lyxnx.chestshop;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestShop extends JavaPlugin {
    
    @Getter
    private static ChestShop instance;

    @Getter
    private ConfigWrapper config;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}