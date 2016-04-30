package net.lyxnx.chestshop.storage;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.lyxnx.chestshop.ChestShop;
import net.lyxnx.chestshop.lang.Lang;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Storage {

    private String hostname, port, database, username, password;

    @Getter
    private boolean sqlite;

    private Connection sqliteConn;

    private HikariDataSource ds;

    /**
     * Constructor used for MySQL.
     * @param hostname The host of the MySQL database. (Usually localhost/127.0.0.1).
     * @param port The port of the MySQL database. (Usually 3306).
     * @param database The name of the database to use.
     * @param username The username of the MySQL database to use. (Usually root (or an account with similar privileges)).
     * @param password The password of the user.
     */
    public Storage(final String hostname, final String port, final String database, final String username, final String password) {
        this.hostname = Preconditions.checkNotNull(hostname);
        this.port = Preconditions.checkNotNull(port);
        this.database = Preconditions.checkNotNull(database);
        this.username = Preconditions.checkNotNull(username);
        this.password = Preconditions.checkNotNull(password);

        ds = new HikariDataSource();
        ds.setUsername(this.username);
        ds.setPassword(this.password);
        ds.setJdbcUrl("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database);
        ds.setMaximumPoolSize(8);
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtSqlLimit", "2048");

        sqlite = false;
    }

    /**
     * Constructor used for SQLite.
     */
    public Storage() {
        sqlite = true;

        try {

            final File file = new File(ChestShop.getInstance().getDataFolder(), "chestshop.db");

            if(!ChestShop.getInstance().getDataFolder().exists()) {
                ChestShop.getInstance().getDataFolder().mkdirs();
            }

            file.createNewFile();

            Class.forName("org.sqlite.JDBC");
            sqliteConn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            sqliteConn.close();
        } catch(final Exception ex) {
            Lang.error(ex);
        }
    }

    /**
     * Returns the connection to the database if it exists.
     * @return The connection to the MySQL or SQLite database as a {@link java.sql.Connection}.
     */
    public Connection getConnection() {
        try {
            if(sqlite) {
                synchronized (sqliteConn) {
                    sqliteConn = DriverManager.getConnection("jdbc:sqlite:" + new File(ChestShop.getInstance().getDataFolder(), "chestshop.db").getAbsolutePath());
                    return sqliteConn;
                }
            }
            return ds.getConnection();
        } catch(final SQLException ex) {
            Lang.error(ex);
            return null;
        }
    }

    /**
     * Closes an SQL resource such as a {@link java.sql.Statement} or {@link java.sql.ResultSet}.
     * @param toClose An array of resources to close.
     */
    public static void close(final AutoCloseable... toClose) {
        for(final AutoCloseable ac : toClose) {
            if(ac != null) {
                try {
                    ac.close();
                } catch(final Exception ignored) {}
            }
        }
    }
}