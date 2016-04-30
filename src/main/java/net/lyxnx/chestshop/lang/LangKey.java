package net.lyxnx.chestshop.lang;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class LangKey {
    @Getter
    private final String parentName;
    @Getter
    private final String name;
    @Getter
    private final String defaultValue;

    public LangKey(final String parentName, final String name, final String defaultValue) {
        this.parentName = parentName;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getFullName() {
        return getParentName() + "." + getName();
    }

    public static class ChestShop extends LangKeyProvider {
        public static final String NAME = "lang";
        public static final LangKey VAULT_NOT_FOUND = new LangKey(NAME, "vault_not_found", "Vault dependency not found. Disabling ChestShop...");
        public static final LangKey INIT_CONFIG = new LangKey(NAME, "init_config", "Initialising ChestShop config...");
        public static final LangKey CONFIG_SAVE_ERROR = new LangKey(NAME,"config_save_error","Error saving config to %0%");
        public static final LangKey NO_PERMISSION = new LangKey(NAME,"no_permission","You don't have permission to execute %0%");

        @Override
        public List<LangKey> values() {
            final List<LangKey> result = new ArrayList<>();

            result.add(VAULT_NOT_FOUND);
            result.add(INIT_CONFIG);
            result.add(CONFIG_SAVE_ERROR);
            result.add(NO_PERMISSION);

            return result;
        }
    }

    public static class Lang extends LangKeyProvider {
        public static final String NAME = "lang";
        public static final LangKey COULD_NOT_LOAD_FILE = new LangKey(NAME, "could_not_load_file", "Could not load langfile %0%!");
        public static final LangKey LOAD = new LangKey(NAME, "load", "Loaded %0% langs");
        public static final LangKey LOAD_STORAGE = new LangKey(NAME, "load_storage", "Loaded %0% keys in lang %1%");
        public static final LangKey SAVE_DEFAULT = new LangKey(NAME, "save_default", "Saving default lang to %0%");

        @Override
        public List<LangKey> values() {
            final List<LangKey> result = new ArrayList<>();

            result.add(LOAD);
            result.add(COULD_NOT_LOAD_FILE);
            result.add(LOAD_STORAGE);
            result.add(SAVE_DEFAULT);

            return result;
        }
    }

    public static class TEST extends LangKeyProvider {
        public static final String NAME = "test";

        public static final LangKey TEST = new LangKey(NAME, "test", "Just a jUnit test %0%");
        public static final LangKey TEST_VARS = new LangKey(NAME, "test_vars", "Just %0% a %1% bunch %2% of %3% vars %4%");

        @Override
        public List<LangKey> values() {
            final List<LangKey> result = new ArrayList<>();

            result.add(TEST);
            result.add(TEST_VARS);

            return result;
        }
    }

    //TODO its probably better to cache this...
    public static List<LangKey> values() {
        final List<LangKey> result = new ArrayList<>();

        result.addAll(new ChestShop().values());
        result.addAll(new Lang().values());
        result.addAll(new TEST().values());

        return result;
    }

    public static LangKey valueOf(final String key) {
        for (final LangKey k : values()) {
            if (k.getFullName().equals(key)) {
                return k;
            }
        }
        return null;
    }
}