package com.justindriggers.vulkan.pipeline.models.rasterization;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VK10;

public enum CullMode implements Maskable {

    NONE(VK10.VK_CULL_MODE_NONE),
    FRONT(VK10.VK_CULL_MODE_FRONT_BIT),
    BACK(VK10.VK_CULL_MODE_BACK_BIT),
    FRONT_AND_BACK(VK10.VK_CULL_MODE_FRONT_AND_BACK);

    private final int bit;

    CullMode(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
