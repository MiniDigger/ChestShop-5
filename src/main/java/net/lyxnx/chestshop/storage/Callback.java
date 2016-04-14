package net.lyxnx.chestshop.storage;

/**
 * Interface used for making sure things happened. Eg a database connection.
 * @param <T> The type of value to base the check upon.
 */
public interface Callback<T> {
    /**
     * Use this to represent the check being completed.
     * @param result The result to be checked against.
     */
    void done(final T result);
}