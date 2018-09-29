package com.justindriggers.vulkan.pipeline.models.vertex.builder;

import com.justindriggers.vulkan.models.ColorFormat;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputAttribute;

import java.util.ArrayList;
import java.util.List;

public class VertexInputBindingBuilder {

    private final List<VertexInputAttribute> vertexInputAttributes = new ArrayList<>();

    private final VertexInputStateBuilder parent;
    private final int binding;

    private int offset = 0;

    VertexInputBindingBuilder(final VertexInputStateBuilder parent, final int binding) {
        this.parent = parent;
        this.binding = binding;
    }

    public VertexInputBindingBuilder intLocation(final int location) {
        vertexInputAttributes.add(new VertexInputAttribute(binding, location, ColorFormat.R32_SINT, offset));
        offset += 4;
        return this;
    }

    public VertexInputBindingBuilder floatLocation(final int location) {
        vertexInputAttributes.add(new VertexInputAttribute(binding, location, ColorFormat.R32_SFLOAT, offset));
        offset += 4;
        return this;
    }

    public VertexInputBindingBuilder vec2Location(final int location) {
        vertexInputAttributes.add(new VertexInputAttribute(binding, location, ColorFormat.R32G32_SFLOAT, offset));
        offset += 2 * 4;
        return this;
    }

    public VertexInputBindingBuilder vec3Location(final int location) {
        vertexInputAttributes.add(new VertexInputAttribute(binding, location, ColorFormat.R32G32B32_SFLOAT, offset));
        offset += 3 * 4;
        return this;
    }

    public VertexInputBindingBuilder vec4Location(final int location) {
        vertexInputAttributes.add(new VertexInputAttribute(binding, location, ColorFormat.R32G32B32A32_SFLOAT, offset));
        offset += 4 * 4;
        return this;
    }

    public VertexInputStateBuilder finalizeBinding() {
        return parent.next(binding, offset, vertexInputAttributes);
    }
}
