package net.lyxnx.chestshop.storage;

/**
 * An enum to represent the available backend types.
 */
public enum Backend {

    /**
     * Backend to represent SQLite.
     */
    SQLITE("sqlite"),
    /**
     * Backend to represent MySQL.
     */
    MYSQL("mysql");

    private final String backend;

    Backend(final String backend) {
        this.backend = backend;
    }

    /**
     * Returns this {@link Backend} in string format.
     * @return The string format of this {@link Backend}.
     */
    @Override
    public String toString() {
        return backend;
    }

    /**
     * Returns the {@link Backend} represented by the input string.
     * @param backend The {@link String} version of a {@link Backend}.
     * @return The {@link Backend} represented by the input string or <code>null</code>
     * if the query does not match.
     */
    public static Backend getBackend(final String backend) {
        for(final Backend end : values()) {
            if(end.toString().equalsIgnoreCase(backend)) {
                return end;
            }
        }
        return null;
    }
}