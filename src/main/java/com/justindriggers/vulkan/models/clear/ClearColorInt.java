package com.justindriggers.vulkan.models.clear;

import org.lwjgl.vulkan.VkClearValue;

public class ClearColorInt extends ClearColor {

    private final int r;
    private final int g;
    private final int b;
    private final int a;

    public ClearColorInt(final int r, final int g, final int b, final int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public VkClearValue toStruct() {
        final VkClearValue result = VkClearValue.calloc();

        result.color()
                .int32(0, r)
                .int32(1, g)
                .int32(2, b)
                .int32(3, a);

        return result;
    }
}
