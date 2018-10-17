package com.justindriggers.vulkan.command.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum CommandBufferLevel implements HasValue<Integer> {

    PRIMARY(VK10.VK_COMMAND_BUFFER_LEVEL_PRIMARY),
    SECONDARY(VK10.VK_COMMAND_BUFFER_LEVEL_SECONDARY);

    private final int value;

    CommandBufferLevel(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
