package com.justindriggers.vulkan.models.clear;

import org.lwjgl.vulkan.VkClearValue;

public class ClearDepthStencil extends ClearValue {

    private final float depth;
    private final int stencil;

    public ClearDepthStencil(final float depth, final int stencil) {
        this.depth = depth;
        this.stencil = stencil;
    }

    @Override
    public VkClearValue toStruct() {
        final VkClearValue result = VkClearValue.calloc();

        result.depthStencil()
                .depth(depth)
                .stencil(stencil);

        return result;
    }
}
