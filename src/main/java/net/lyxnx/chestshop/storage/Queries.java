package net.lyxnx.chestshop.storage;

/**
 * Class for constant SQL queries.<br>
 * This class cannot be instantiated.
 * <p />
 * If a query is not in the {@link Queries.SQLite} class, it will be in the parent class
 * because the syntax doesn't differ between backends.
 */
public class Queries {


    private Queries() {}

    public static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users(" +
            "uuid VARCHAR(36) NOT NULL, " +
            "name VARCHAR(16) NOT NULL, " +
            "last_name VARCHAR(16) NOT NULL, " +
            "INDEX uid(uuid)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    public static final String GET_LAST_USER_NAME = "SELECT last_name FROM users WHERE uuid=?;";

    public static final String UPDATE_USER = "INSERT INTO users(uuid, name, last_name) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE name=? last_name=?;";

    public static final String CREATE_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS items(" +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "code VARCHAR(255) NOT NULL, " +
            "PRIMARY KEY (id), " +
            "UNIQUE INDEX uq_code(code)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    public static final String GET_ITEM_CODE = "SELECT code FROM items WHERE id=?;";

    public static final String ADD_ITEM_CODE = "INSERT INTO items(code) VALUES(?);";

    public static class SQLite {

        private SQLite() {}

        public static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users(" +
                "uuid VARCHAR(36) NOT NULL, " +
                "name VARCHAR(16) NOT NULL, " +
                "last_name VARHAR(16) NOT NULl); " +
                "CREATE INDEX IF NOT EXISTS uid ON users(uuid);";

        public static final String UPDATE_USER = "INSERT OR REPLACE INTO users(uuid, name, last_name) VALUES(?, ?, ?);";

        public static final String CREATE_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS items(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "code VARCHAR NOT NULL, " +
                "UNIQUE (code));";
    }
}