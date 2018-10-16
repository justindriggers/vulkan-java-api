package com.justindriggers.vulkan.image.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum ImageViewType implements HasValue<Integer> {

    ONE_DIMENSIONAL(VK10.VK_IMAGE_VIEW_TYPE_1D),
    TWO_DIMENSIONAL(VK10.VK_IMAGE_VIEW_TYPE_2D),
    THREE_DIMENSIONAL(VK10.VK_IMAGE_VIEW_TYPE_3D),
    CUBE(VK10.VK_IMAGE_VIEW_TYPE_CUBE),
    ONE_DIMENSIONAL_ARRAY(VK10.VK_IMAGE_VIEW_TYPE_1D_ARRAY),
    TWO_DIMENSIONAL_ARRAY(VK10.VK_IMAGE_VIEW_TYPE_2D_ARRAY),
    CUBE_ARRAY(VK10.VK_IMAGE_VIEW_TYPE_CUBE_ARRAY);

    private final int value;

    ImageViewType(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
