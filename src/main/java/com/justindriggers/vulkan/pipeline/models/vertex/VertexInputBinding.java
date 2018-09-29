package com.justindriggers.vulkan.pipeline.models.vertex;

public class VertexInputBinding {

    private final int binding;
    private final int stride;
    private final VertexInputRate rate;

    public VertexInputBinding(final int binding,
                              final int stride,
                              final VertexInputRate rate) {
        this.binding = binding;
        this.stride = stride;
        this.rate = rate;
    }

    public int getBinding() {
        return binding;
    }

    public int getStride() {
        return stride;
    }

    public VertexInputRate getRate() {
        return rate;
    }
}
