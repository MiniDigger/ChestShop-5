package net.lyxnx.chestshop;

import net.lyxnx.chestshop.encoding.Base64;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Item {

    private final Yaml yaml;
    private final ItemStack item;

    public Item(final ItemStack item) {
        this.item = item;
        this.yaml = new Yaml(new YamlBukkit(), new YamlRepresenter(), new DumperOptions());
    }

    public String getItemCode() {
        ItemStack clone = new ItemStack(item);
        clone.setAmount(1);
        clone.setDurability((short) 0);

        try {
            String code = Base64.encodeObject(yaml.dump(clone));

            PreparedStatement ps = null;
            try(Connection conn = ChestShop.getInstance().getStorage().getConnection()) {
                ps = conn.prepareStatement("SELECT code FROM items WHERE code=?;");
                ps.setString(1, code);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class YamlBukkit extends YamlConstructor {
        public YamlBukkit() {
            this.yamlConstructors.put(new Tag(Tag.PREFIX + "org.bukkit.inventory.ItemStack"), yamlConstructors.get(Tag.MAP));
        }
    }
}