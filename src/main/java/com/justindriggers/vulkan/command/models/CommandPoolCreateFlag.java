package com.justindriggers.vulkan.command.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VK11;

public enum CommandPoolCreateFlag implements Maskable {

    TRANSIENT(VK10.VK_COMMAND_POOL_CREATE_TRANSIENT_BIT),
    RESET_COMMAND_BUFFER(VK10.VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT),
    PROTECTED(VK11.VK_COMMAND_POOL_CREATE_PROTECTED_BIT);

    private final int bit;

    CommandPoolCreateFlag(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
