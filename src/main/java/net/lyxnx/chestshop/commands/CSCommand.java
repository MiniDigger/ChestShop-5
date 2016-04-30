package net.lyxnx.chestshop.commands;

import net.lyxnx.chestshop.lang.Lang;
import net.lyxnx.chestshop.lang.LangKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Class to make commands easier and so that they do not have to be registered in the plugin.yml
 */
public abstract class CSCommand extends BukkitCommand {

    public CSCommand(final String name, final String permission, final String description, final String usage, final String... aliases) {
        super(name);
        this.setDescription(description);
        this.setAliases(Arrays.asList(aliases));
        this.setPermission(permission);
        this.setUsage(usage);
    }

    @Override
    public boolean execute(final CommandSender sender, final String alias, final String[] args) {
        if(!sender.hasPermission(this.getPermission())) {
            Lang.msg(sender, LangKey.ChestShop.NO_PERMISSION, getName());
            return true;
        }

        run(sender, args);
        return true;
    }

    public abstract void run(final CommandSender sender, final String[] args);

    public boolean isPlayer(final CommandSender sender) {
        return (sender instanceof Player);
    }
}