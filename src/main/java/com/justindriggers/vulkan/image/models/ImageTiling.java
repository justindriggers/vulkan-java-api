package com.justindriggers.vulkan.image.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum ImageTiling implements HasValue<Integer> {

    OPTIMAL(VK10.VK_IMAGE_TILING_OPTIMAL),
    LINEAR(VK10.VK_IMAGE_TILING_LINEAR);

    private final int value;

    ImageTiling(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
