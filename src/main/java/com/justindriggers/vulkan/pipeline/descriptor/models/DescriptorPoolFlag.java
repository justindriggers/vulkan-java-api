package com.justindriggers.vulkan.pipeline.descriptor.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.EXTDescriptorIndexing;
import org.lwjgl.vulkan.VK10;

public enum DescriptorPoolFlag implements Maskable {

    FREE_DESCRIPTOR_SET(VK10.VK_DESCRIPTOR_POOL_CREATE_FREE_DESCRIPTOR_SET_BIT),
    UPDATE_AFTER_BIND(EXTDescriptorIndexing.VK_DESCRIPTOR_POOL_CREATE_UPDATE_AFTER_BIND_BIT_EXT);

    private final int bit;

    DescriptorPoolFlag(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
