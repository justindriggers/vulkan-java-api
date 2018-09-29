package com.justindriggers.vulkan.pipeline.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum PipelineBindPoint implements HasValue<Integer> {

    GRAPHICS(VK10.VK_PIPELINE_BIND_POINT_GRAPHICS),
    COMPUTE(VK10.VK_PIPELINE_BIND_POINT_COMPUTE);

    private final int value;

    PipelineBindPoint(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
