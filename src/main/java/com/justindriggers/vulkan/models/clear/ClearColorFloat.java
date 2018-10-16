package com.justindriggers.vulkan.models.clear;

import org.lwjgl.vulkan.VkClearValue;

public class ClearColorFloat extends ClearColor {

    private final float r;
    private final float g;
    private final float b;
    private final float a;

    public ClearColorFloat(final float r, final float g, final float b, final float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public VkClearValue toStruct() {
        final VkClearValue result = VkClearValue.calloc();

        result.color()
                .float32(0, r)
                .float32(1, g)
                .float32(2, b)
                .float32(3, a);

        return result;
    }
}
