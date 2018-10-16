package com.justindriggers.vulkan.pipeline.models.depth;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum StencilOperation implements HasValue<Integer> {

    KEEP(VK10.VK_STENCIL_OP_KEEP),
    ZERO(VK10.VK_STENCIL_OP_ZERO),
    REPLACE(VK10.VK_STENCIL_OP_REPLACE),
    INCREMENT_AND_CLAMP(VK10.VK_STENCIL_OP_INCREMENT_AND_CLAMP),
    DECREMENT_AND_CLAMP(VK10.VK_STENCIL_OP_DECREMENT_AND_CLAMP),
    INVERT(VK10.VK_STENCIL_OP_INVERT),
    INCREMENT_AND_WRAP(VK10.VK_STENCIL_OP_INCREMENT_AND_WRAP),
    DECREMENT_AND_WRAP(VK10.VK_STENCIL_OP_DECREMENT_AND_WRAP);

    private final int value;

    StencilOperation(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
