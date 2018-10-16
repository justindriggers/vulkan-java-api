package com.justindriggers.vulkan.swapchain.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum SubpassContents implements HasValue<Integer> {

    INLINE(VK10.VK_SUBPASS_CONTENTS_INLINE),
    SECONDARY_COMMAND_BUFFERS(VK10.VK_SUBPASS_CONTENTS_SECONDARY_COMMAND_BUFFERS);

    private final int value;

    SubpassContents(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
