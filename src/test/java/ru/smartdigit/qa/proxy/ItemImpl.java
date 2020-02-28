package ru.smartdigit.qa.proxy;

import java.util.Random;
import java.util.UUID;

/**
 * Source class implemented {@link Item}.
 */
public class ItemImpl implements Item {
    private final String name = getClass().getSimpleName();


    @Override
    public boolean hasName(String expected) {
        return expected.equals(name);
    }

    @Override
    public long getId() {
        return new Random().nextLong();
    }

    @Override
    public UUID getUUID() {
        return UUID.randomUUID();
    }

    @Override
    public String getName() {
        return name;
    }
}
