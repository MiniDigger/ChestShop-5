package net.lyxnx.chestshop;

import lombok.Getter;
import lombok.Setter;
import net.lyxnx.chestshop.config.ConfigDefaults;
import net.lyxnx.chestshop.config.ConfigWrapper;
import net.lyxnx.chestshop.lang.Lang;
import net.lyxnx.chestshop.lang.LangHandler;
import net.lyxnx.chestshop.lang.LangKey;
import net.lyxnx.chestshop.storage.Backend;
import net.lyxnx.chestshop.storage.Callback;
import net.lyxnx.chestshop.storage.Queries;
import net.lyxnx.chestshop.storage.Storage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ChestShop extends JavaPlugin {

    @Getter
    private static ChestShop instance;
    @Getter
    @Setter
    private static boolean unitTesting;

    @Getter
    private ConfigWrapper chestShopConfig;

    @Getter
    private Backend backend;

    @Getter
    private Storage storage;

    @Getter
    private Economy economy;

    @Getter
    private LangHandler lang;

    @Override
    public void onEnable() {
        instance = this;

        lang = new LangHandler();
        lang.enable();

        loadConfig();

        loadDatabase(result -> {
            if (result) {
                Statement stmt = null;
                try (Connection conn = storage.getConnection()) {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(storage.isSqlite() ? Queries.SQLite.CREATE_ITEMS_TABLE : Queries.CREATE_ITEMS_TABLE);
                    stmt.executeUpdate(storage.isSqlite() ? Queries.SQLite.CREATE_USERS_TABLE : Queries.CREATE_USERS_TABLE);
                    stmt.close();
                } catch (final SQLException ex) {
                    Lang.error(ex);
                } finally {
                    Storage.close(stmt);
                }
            }
        });

        if (!setupEconomy()) {
            getLogger().severe(Lang.translate(LangKey.ChestShop.VAULT_NOT_FOUND));
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        instance = null;
        lang.disable();
    }

    private void loadConfig() {
        chestShopConfig = new ConfigWrapper(this, "config.yml", "");
        chestShopConfig.createNewFile(Lang.translate(LangKey.ChestShop.INIT_CONFIG), "ChestShop-5 Configuration");

        for (final ConfigDefaults def : ConfigDefaults.values()) {
            chestShopConfig.getConfig().addDefault(def.getPath().toUpperCase(), def.getDefault());
        }

        chestShopConfig.getConfig().options().copyDefaults(true);
        chestShopConfig.saveConfig();
    }

    private void loadDatabase(final Callback<Boolean> state) {
        backend = Backend.getBackend(chestShopConfig.getConfig().getString("BACKEND.TYPE"));

        if (backend == Backend.MYSQL) {
            final String hostname = chestShopConfig.getConfig().getString("BACKEND.HOSTNAME");
            final String port = chestShopConfig.getConfig().getString("BACKEND.PORT");
            final String database = chestShopConfig.getConfig().getString("BACKEND.DATABASE");
            final String username = chestShopConfig.getConfig().getString("BACKEND.USERNAME");
            final String password = chestShopConfig.getConfig().getString("BACKEND.PASSWORD");

            // Do this async to prevent the main thread being blocked.
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                try {
                    storage = new Storage(hostname, port, database, username, password);

                    final Connection c = storage.getConnection();
                    if (c != null) {
                        c.close();
                        state.done(true);
                        return;
                    }
                } catch (final SQLException ex) {
                    Lang.error(ex);
                }
            });
        } else {
            backend = Backend.SQLITE; // Set this just in case the config value for the backend returned null.
            storage = new Storage();
            state.done(true);
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
}