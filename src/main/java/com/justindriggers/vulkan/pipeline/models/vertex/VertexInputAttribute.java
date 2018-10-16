package com.justindriggers.vulkan.pipeline.models.vertex;

import com.justindriggers.vulkan.models.Format;

public class VertexInputAttribute {

    private final int binding;
    private final int location;
    private final Format format;
    private final int offset;

    public VertexInputAttribute(final int binding,
                                final int location,
                                final Format format,
                                final int offset) {
        this.binding = binding;
        this.location = location;
        this.format = format;
        this.offset = offset;
    }

    public int getBinding() {
        return binding;
    }

    public int getLocation() {
        return location;
    }

    public Format getFormat() {
        return format;
    }

    public int getOffset() {
        return offset;
    }
}
