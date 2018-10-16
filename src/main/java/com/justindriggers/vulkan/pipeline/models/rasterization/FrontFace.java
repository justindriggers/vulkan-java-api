package com.justindriggers.vulkan.pipeline.models.rasterization;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum FrontFace implements HasValue<Integer> {

    COUNTER_CLOCKWISE(VK10.VK_FRONT_FACE_COUNTER_CLOCKWISE),
    CLOCKWISE(VK10.VK_FRONT_FACE_CLOCKWISE);

    private final int value;

    FrontFace(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
