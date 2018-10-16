package com.justindriggers.vulkan.pipeline.models.rasterization;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum PolygonMode implements HasValue<Integer> {

    FILL(VK10.VK_POLYGON_MODE_FILL),
    LINE(VK10.VK_POLYGON_MODE_LINE),
    POINT(VK10.VK_POLYGON_MODE_POINT);

    private final int value;

    PolygonMode(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
