package net.lyxnx.chestshop;

import net.lyxnx.chestshop.encoding.Base62;
import net.lyxnx.chestshop.encoding.Base64;
import net.lyxnx.chestshop.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemUtils {

    private final Yaml yaml;
    private final ItemStack item;

    public ItemUtils(final ItemStack item) {
        this.item = item;
        this.yaml = new Yaml(new YamlBukkit(), new YamlRepresenter(), new DumperOptions());
    }

    public String getItemCode() {
        try {
            ItemStack clone = new ItemStack(item);
            clone.setAmount(1);
            clone.setDurability((short) 0);

            String code = Base64.encodeObject(yaml.dump(clone));

            Item i = getItem(code);

            if(i != null) {
                return Base62.encode(i.getId());
            }

            i = new Item(code);

            addItem(i);

            return Base62.encode(i.getId());

        } catch(final Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public boolean isPresent(final String code) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try(Connection conn = ChestShop.getInstance().getStorage().getConnection()) {
            ps = conn.prepareStatement("SELECT id FROM items WHERE code=?;");
            ps.setString(1, code);
            rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Storage.close(rs, ps);
        }
        return false;
    }

    public Item getItem(final String code) {
        Item item = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try(Connection conn = ChestShop.getInstance().getStorage().getConnection()) {
            ps = conn.prepareStatement("SELECT * FROM items WHERE code=?;");
            ps.setString(1, code);
            rs = ps.executeQuery();
            if(rs.next()) {
                item = new Item();
                item.setId(rs.getInt("id"));
                item.setCode(code);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Storage.close(rs, ps);
        }
        return item;
    }

    public void addItem(final Item item) {
        Bukkit.getScheduler().runTaskAsynchronously(ChestShop.getInstance(), () -> {
            PreparedStatement ps = null;
            try (Connection conn = ChestShop.getInstance().getStorage().getConnection()) {
                ps = conn.prepareStatement("INSERT INTO items(code) VALUES(?);");
                ps.setString(1, item.getCode());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Storage.close(ps);
            }
        });
    }

    private class YamlBukkit extends YamlConstructor {
        public YamlBukkit() {
            this.yamlConstructors.put(new Tag(Tag.PREFIX + "org.bukkit.inventory.ItemStack"), yamlConstructors.get(Tag.MAP));
        }
    }
}