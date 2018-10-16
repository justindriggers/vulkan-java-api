package com.justindriggers.vulkan.pipeline.models.colorblend;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum BlendOperation implements HasValue<Integer> {

    ADD(VK10.VK_BLEND_OP_ADD),
    SUBTRACT(VK10.VK_BLEND_OP_SUBTRACT),
    REVERSE_SUBTRACT(VK10.VK_BLEND_OP_REVERSE_SUBTRACT),
    MIN(VK10.VK_BLEND_OP_MIN),
    MAX(VK10.VK_BLEND_OP_MAX);

    private final int value;

    BlendOperation(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
