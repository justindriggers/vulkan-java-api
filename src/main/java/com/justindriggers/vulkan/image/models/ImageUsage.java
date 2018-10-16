package com.justindriggers.vulkan.image.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VK10;

public enum ImageUsage implements Maskable {

    TRANSFER_SRC(VK10.VK_IMAGE_USAGE_TRANSFER_SRC_BIT),
    TRANSFER_DST(VK10.VK_IMAGE_USAGE_TRANSFER_DST_BIT),
    SAMPLED(VK10.VK_IMAGE_USAGE_SAMPLED_BIT),
    STORAGE(VK10.VK_IMAGE_USAGE_STORAGE_BIT),
    COLOR_ATTACHMENT(VK10.VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT),
    DEPTH_STENCIL_ATTACHMENT(VK10.VK_IMAGE_USAGE_DEPTH_STENCIL_ATTACHMENT_BIT),
    TRANSIENT_ATTACHMENT(VK10.VK_IMAGE_USAGE_TRANSIENT_ATTACHMENT_BIT),
    INPUT_ATTACHMENT(VK10.VK_IMAGE_USAGE_INPUT_ATTACHMENT_BIT);

    private final int bit;

    ImageUsage(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
