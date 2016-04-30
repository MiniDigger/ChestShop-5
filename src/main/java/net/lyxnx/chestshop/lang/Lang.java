package net.lyxnx.chestshop.lang;

import net.lyxnx.chestshop.ChestShop;
import net.lyxnx.chestshop.TestCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Lang {

    // TODO Should we include JSON messages?

    public static String translate(final LangKey key, final LangType lang, final String... args) {
        String raw = LangHandler.getInstance().getLangStorage(lang).get(key);

        for (int i = 0; i < args.length; i++) {
            raw = raw.replace("%" + i + "%", args[i]);
        }

        return raw;
    }

    public static String translate(final LangKey key, final String... args) {
        return translate(key, LangHandler.getInstance().getDefaultLang(), args);
    }

    public static void msg(final CommandSender sender, final LangKey key, final LangType lang, final String... args) {
        sender.sendMessage(translate(key, lang, args));
    }

    public static void msg(final CommandSender sender, final LangKey key, final String... args) {
        sender.sendMessage(translate(key, args));
    }

    public static void console(final LangKey key, final String... args) {
        if (ChestShop.isUnitTesting()) {
            msg(TestCommandSender.getInstance(), key, args);
        } else {
            msg(Bukkit.getConsoleSender(), key, args);
        }
    }

    public static void console(final LangKey key, final LangType lang, final String... args) {
        if (ChestShop.isUnitTesting()) {
            msg(TestCommandSender.getInstance(), key, lang, args);
        } else {
            msg(Bukkit.getConsoleSender(), key, lang, args);
        }
    }

    public static void error(final Exception e) {
        e.printStackTrace();
        // TODO better error handeling
    }
}
