package com.justindriggers.vulkan.pipeline.models.vertex;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum VertexInputRate implements HasValue<Integer> {

    VERTEX(VK10.VK_VERTEX_INPUT_RATE_VERTEX),
    INSTANCE(VK10.VK_VERTEX_INPUT_RATE_INSTANCE);

    private final int value;

    VertexInputRate(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
