package com.justindriggers.vulkan.buffer.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VK10;

public enum BufferUsage implements Maskable {

    TRANSFER_SRC(VK10.VK_BUFFER_USAGE_TRANSFER_SRC_BIT),
    TRANSFER_DST(VK10.VK_BUFFER_USAGE_TRANSFER_DST_BIT),
    UNIFORM_TEXEL_BUFFER(VK10.VK_BUFFER_USAGE_UNIFORM_TEXEL_BUFFER_BIT),
    STORAGE_TEXEL_BUFFER(VK10.VK_BUFFER_USAGE_STORAGE_TEXEL_BUFFER_BIT),
    UNIFORM_BUFFER(VK10.VK_BUFFER_USAGE_UNIFORM_BUFFER_BIT),
    STORAGE_BUFFER(VK10.VK_BUFFER_USAGE_STORAGE_BUFFER_BIT),
    INDEX_BUFFER(VK10.VK_BUFFER_USAGE_INDEX_BUFFER_BIT),
    VERTEX_BUFFER(VK10.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT),
    INDIRECT_BUFFER(VK10.VK_BUFFER_USAGE_INDIRECT_BUFFER_BIT);

    private final int bit;

    BufferUsage(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
