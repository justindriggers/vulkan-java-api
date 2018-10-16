package com.justindriggers.vulkan.swapchain.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum AttachmentLoadOperation implements HasValue<Integer> {

    LOAD(VK10.VK_ATTACHMENT_LOAD_OP_LOAD),
    CLEAR(VK10.VK_ATTACHMENT_LOAD_OP_CLEAR),
    DONT_CARE(VK10.VK_ATTACHMENT_LOAD_OP_DONT_CARE);

    private final int value;

    AttachmentLoadOperation(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
