package net.lyxnx.chestshop.config;

import net.lyxnx.chestshop.ChestShop;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

/**
 * An enum with the default config values.
 */
public enum ConfigDefaults {

    CURRENCIES("CURRENCIES", new HashMap<String, String>() {{
        put("USD", "$");
        put("GBP", "£");
        put("EUR", "€");
    }}),
    CONVERSIONS("CURRENCIES.CONVERSIONS", new HashMap<String, Double>() {{
        put("GBP", 1.41D);
        put("EUR", 1.13D);
        put("USD", 1D);
    }}),
    BACKEND("BACKEND.TYPE", "sqlite"),
    BACKEND_HOSTNAME("BACKEND.HOSTNAME", "localhost"),
    BACKEND_PORT("BACKEND.PORT", "3306"),
    BACKEND_DATABASE("BACKEND.DATABASE", "chestshop"),
    BACKEND_USERNAME("BACKEND.USERNAME", "root"),
    BACKEND_PASSWORD("BACKEND.PASSWORD", "password");

    private final String path;
    private final Object def;
    private final FileConfiguration file = ChestShop.getInstance().getConfig().getConfig();

    ConfigDefaults(final String path, final Object def) {
        this.path = path;
        this.def = def;
    }

    /**
     * Gets the path of this {@link Object}.
     * @return The path of this {@link Object}.
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the default value of this {@link Object}.
     * @return The default value for this {@link Object}.
     */
    public Object getDefault() {
        return def;
    }

    /**
     * Returns this as a value from the config file. (config.yml).<br>
     * Use this if you want the raw type. Eg {@link HashMap}.<br>
     * Use {@link ConfigWrapper#getConfig()} for the actual value.
     * @return {@link Object} from the config file.
     */
    public Object getConfigValue() {
        return file.get(this.path, this.def);
    }
}