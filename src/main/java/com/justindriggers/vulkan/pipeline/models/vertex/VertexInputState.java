package com.justindriggers.vulkan.pipeline.models.vertex;

import com.justindriggers.vulkan.pipeline.models.vertex.builder.VertexInputStateBuilder;

import java.util.List;

public class VertexInputState {

    private final List<VertexInputBinding> vertexInputBindings;
    private final List<VertexInputAttribute> vertexInputAttributes;

    public VertexInputState(final List<VertexInputBinding> vertexInputBindings,
                            final List<VertexInputAttribute> vertexInputAttributes) {
        this.vertexInputBindings = vertexInputBindings;
        this.vertexInputAttributes = vertexInputAttributes;
    }

    public List<VertexInputBinding> getVertexInputBindings() {
        return vertexInputBindings;
    }

    public List<VertexInputAttribute> getVertexInputAttributes() {
        return vertexInputAttributes;
    }

    public static VertexInputStateBuilder builder() {
        return new VertexInputStateBuilder();
    }
}
