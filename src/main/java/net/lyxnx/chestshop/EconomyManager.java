package net.lyxnx.chestshop;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    @Getter
    private Economy economy;

    private static EconomyManager instance;

    public static EconomyManager getInstance() {
        if(instance == null) {
            instance = new EconomyManager();
        }
        return instance;
    }

    private EconomyManager() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }
}