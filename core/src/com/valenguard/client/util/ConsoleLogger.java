package com.valenguard.client.util;

/**
 * Provides the console with formatted text.
 */
public enum ConsoleLogger {
    CHAT,
    NETWORK,
    ERROR,
    INFO,
    SERVER;

    @Override
    public String toString() {
        return "[" + super.toString() + "] ";
    }
}
