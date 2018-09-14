package com.justindriggers.vulkan.synchronize.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VK10;

public enum FenceCreationFlag implements Maskable {

    SIGNALED(VK10.VK_FENCE_CREATE_SIGNALED_BIT);

    private final int bit;

    FenceCreationFlag(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
