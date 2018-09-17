package com.justindriggers.vulkan.pipeline.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VK10;

public enum CommandBufferUsage implements Maskable {

    ONE_TIME_SUBMIT(VK10.VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT),
    RENDER_PASS_CONTINUE(VK10.VK_COMMAND_BUFFER_USAGE_RENDER_PASS_CONTINUE_BIT),
    SIMULTANEOUS_USE(VK10.VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT);

    private final int bit;

    CommandBufferUsage(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
