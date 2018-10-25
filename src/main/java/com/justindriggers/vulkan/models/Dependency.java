package com.justindriggers.vulkan.models;

import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VK11;

public enum Dependency implements Maskable {

    BY_REGION(VK10.VK_DEPENDENCY_BY_REGION_BIT),
    VIEW_LOCAL(VK11.VK_DEPENDENCY_VIEW_LOCAL_BIT),
    DEVICE_GROUP(VK11.VK_DEPENDENCY_DEVICE_GROUP_BIT);

    private final int bit;

    Dependency(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
