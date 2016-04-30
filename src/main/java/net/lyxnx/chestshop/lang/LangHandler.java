package net.lyxnx.chestshop.lang;

import lombok.Getter;
import lombok.Setter;
import net.lyxnx.chestshop.ChestShop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LangHandler {

    @Getter
    private static LangHandler instance = new LangHandler();

    @Getter
    @Setter
    private LangType defaultLang = new LangType("en_US");

    @Getter
    private LangStorage defaultLangStorage = new LangStorage(defaultLang);
    private List<LangStorage> langStorages = new ArrayList<>();
    private List<String> langs = new ArrayList<>();

    @Getter
    @Setter
    private File langFolder;

    public boolean disable() {
        instance = null;
        return true;
    }

    public boolean enable() {
        if (langFolder == null) {
            langFolder = new File(ChestShop.getInstance().getDataFolder(), "lang");
        }
        loadLangs();
        return true;
    }

    public void loadLangs() {
        if (langFolder == null) {
            return;
        }

        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        for (final File f : langFolder.listFiles((dir, name) -> {
            return name.endsWith(".lang");
        })) {
            final String lang = f.getName().replace(".lang", "");
            final LangStorage storage = new LangStorage(new LangType(lang));
            if (storage.load(f)) {
                langs.add(lang);
                langStorages.add(storage);
            } else {
                Lang.console(LangKey.Lang.COULD_NOT_LOAD_FILE, f.getName());
            }
        }

        Lang.console(LangKey.Lang.LOAD, langStorages.size() + "");

        if (langStorages.size() == 0) {
            defaultLangStorage.save(new File(langFolder, defaultLangStorage.getLang().key + ".lang"));
            Lang.console(LangKey.Lang.SAVE_DEFAULT, defaultLangStorage.getLang().key + ".lang");
        }
    }

    public LangStorage getLangStorage(final LangType lang) {
        if (!isLangLoaded(lang)) {
            return defaultLangStorage;
        }

        for (final LangStorage storage : langStorages) {
            if (storage.getLang().key.equals(lang.key)) {
                return storage;
            }
        }
        return defaultLangStorage;
    }

    public boolean isLangLoaded(final LangType lang) {
        return langs.contains(lang.key);
    }
}