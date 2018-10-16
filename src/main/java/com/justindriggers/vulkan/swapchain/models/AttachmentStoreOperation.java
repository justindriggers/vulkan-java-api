package com.justindriggers.vulkan.swapchain.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum AttachmentStoreOperation implements HasValue<Integer> {

    STORE(VK10.VK_ATTACHMENT_STORE_OP_STORE),
    DONT_CARE(VK10.VK_ATTACHMENT_STORE_OP_DONT_CARE);

    private final int value;

    AttachmentStoreOperation(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
