package ru.smartdigit.qa.proxy;

import java.util.UUID;

/**
 * Proxy class for {@link ItemImpl}.
 */
public class ItemImplProxy implements Item {

    private final Item sourceItem;

    public ItemImplProxy(Item sourceItem) {
        this.sourceItem = sourceItem;
    }

    @Override
    public boolean hasName(String expected) {
        System.out.println("[STATIC PROXY] Invoke hasName " + expected);
        return sourceItem.hasName(expected);
    }

    @Override
    public long getId() {
        System.out.println("[STATIC PROXY] Invoke getId");
        return sourceItem.getId();
    }

    @Override
    public UUID getUUID() {
        System.out.println("[STATIC PROXY] Invoke getUUID");
        return sourceItem.getUUID();
    }

    @Override
    public String getName() {
        System.out.println("[STATIC PROXY] Invoke getName");
        return sourceItem.getName();
    }
}