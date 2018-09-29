package com.justindriggers.vulkan.pipeline.models.vertex.builder;

import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputAttribute;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputBinding;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputRate;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputState;

import java.util.ArrayList;
import java.util.List;

public class VertexInputStateBuilder {

    private final List<VertexInputBinding> vertexInputBindings = new ArrayList<>();
    private final List<VertexInputAttribute> vertexInputAttributes = new ArrayList<>();

    public VertexInputBindingBuilder addBinding(final int binding) {
        return new VertexInputBindingBuilder(this, binding);
    }

    public VertexInputState build() {
        return new VertexInputState(vertexInputBindings, vertexInputAttributes);
    }

    VertexInputStateBuilder next(final int binding, final int stride, final List<VertexInputAttribute> attributes) {
        vertexInputBindings.add(new VertexInputBinding(binding, stride, VertexInputRate.VERTEX));
        vertexInputAttributes.addAll(attributes);
        return this;
    }
}
