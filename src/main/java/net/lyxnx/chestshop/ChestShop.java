package net.lyxnx.chestshop;

import lombok.Getter;
import net.lyxnx.chestshop.config.ConfigDefaults;
import net.lyxnx.chestshop.config.ConfigWrapper;
import net.lyxnx.chestshop.storage.Backend;
import net.lyxnx.chestshop.storage.Callback;
import net.lyxnx.chestshop.storage.Queries;
import net.lyxnx.chestshop.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ChestShop extends JavaPlugin {

    @Getter
    private static ChestShop instance;

    @Getter
    private ConfigWrapper config;

    @Getter
    private Backend backend;

    @Getter
    private Storage storage;

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();

        loadDatabase(result -> {
            if(result) {
                Statement stmt = null;
                try(Connection conn = storage.getConnection()) {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(storage.isSqlite() ? Queries.SQLite.CREATE_ITEMS_TABLE : Queries.CREATE_ITEMS_TABLE);
                    stmt.executeUpdate(storage.isSqlite() ? Queries.SQLite.CREATE_USERS_TABLE : Queries.CREATE_USERS_TABLE);
                    stmt.close();
                } catch(final SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    Storage.close(stmt);
                }
            }
        });
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

    private void loadDatabase(final Callback<Boolean> state) {
        backend = Backend.getBackend(config.getConfig().getString("BACKEND.TYPE"));

        if (backend == Backend.MYSQL) {
            final String hostname = config.getConfig().getString("BACKEND.HOSTNAME");
            final String port = config.getConfig().getString("BACKEND.PORT");
            final String database = config.getConfig().getString("BACKEND.DATABASE");
            final String username = config.getConfig().getString("BACKEND.USERNAME");
            final String password = config.getConfig().getString("BACKEND.PASSWORD");

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
                } catch(final SQLException ex) {
                    ex.printStackTrace();
                }
            });
        } else {
            backend = Backend.SQLITE; // Set this just in case the config value for the backend returned null.
            storage = new Storage();
            state.done(true);
        }
    }
}