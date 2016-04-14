package net.lyxnx.chestshop;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

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
    }});

    private final String path;
    private final Object def;
    private final FileConfiguration file = ChestShop.getInstance().getConfig().getConfig();

    ConfigDefaults(final String path, final Object def) {
        this.path = path;
        this.def = def;
    }

    public String getPath() {
        return path;
    }

    public Object getDefault() {
        return def;
    }

    public Object getConfigValue() {
        return file.get(this.path, this.def);
    }
}