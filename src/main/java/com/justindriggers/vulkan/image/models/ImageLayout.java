package com.justindriggers.vulkan.image.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.KHRSharedPresentableImage;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VK11;

public enum ImageLayout implements HasValue<Integer> {

    UNDEFINED(VK10.VK_IMAGE_LAYOUT_UNDEFINED),
    PREINITIALIZED(VK10.VK_IMAGE_LAYOUT_PREINITIALIZED),
    GENERAL(VK10.VK_IMAGE_LAYOUT_GENERAL),
    COLOR_ATTACHMENT_OPTIMAL(VK10.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL),
    DEPTH_STENCIL_ATTACHMENT_OPTIMAL(VK10.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL),
    DEPTH_STENCIL_READ_ONLY_OPTIMAL(VK10.VK_IMAGE_LAYOUT_DEPTH_STENCIL_READ_ONLY_OPTIMAL),
    DEPTH_READ_ONLY_STENCIL_ATTACHMENT_OPTIMAL(VK11.VK_IMAGE_LAYOUT_DEPTH_READ_ONLY_STENCIL_ATTACHMENT_OPTIMAL),
    DEPTH_ATTACHMENT_STENCIL_READ_ONLY_OPTIMAL(VK11.VK_IMAGE_LAYOUT_DEPTH_ATTACHMENT_STENCIL_READ_ONLY_OPTIMAL),
    SHADER_READ_ONLY_OPTIMAL(VK10.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL),
    TRANSFER_SRC_OPTIMAL(VK10.VK_IMAGE_LAYOUT_TRANSFER_SRC_OPTIMAL),
    TRANSFER_DST_OPTIMAL(VK10.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL),
    PRESENT_SRC(KHRSwapchain.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR),
    SHARED_PRESENT(KHRSharedPresentableImage.VK_IMAGE_LAYOUT_SHARED_PRESENT_KHR);

    private final int value;

    ImageLayout(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}