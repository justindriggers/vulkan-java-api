package com.justindriggers.vulkan.pipeline.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VK10;

public enum PipelineCreateFlag implements Maskable {

    DISABLE_OPTIMIZATION(VK10.VK_PIPELINE_CREATE_DISABLE_OPTIMIZATION_BIT),
    ALLOW_DERIVATIVES(VK10.VK_PIPELINE_CREATE_ALLOW_DERIVATIVES_BIT),
    DERIVATIVE(VK10.VK_PIPELINE_CREATE_DERIVATIVE_BIT);

    private final int bit;

    PipelineCreateFlag(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
