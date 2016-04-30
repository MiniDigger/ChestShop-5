package net.lyxnx.chestshop.commands;

import org.bukkit.command.CommandSender;

public class ItemInfo extends CSCommand {

    public ItemInfo() {
        super("iteminfo", "chestshop.iteminfo", "Displays an item's info.", "/iteminfo [id]", "iinfo");
    }

    @Override
    public void run(CommandSender sender, String[] args) {

    }
}