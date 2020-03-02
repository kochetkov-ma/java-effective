package ru.iopump.qa.proxy;

import java.util.UUID;

/**
 * Interface for proxy pattern.
 */
public interface Item {
    boolean hasName(String expected);
    long getId();
    UUID getUUID();
    String getName();
}
