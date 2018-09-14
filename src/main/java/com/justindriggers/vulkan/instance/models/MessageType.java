package com.justindriggers.vulkan.instance.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.EXTDebugUtils;

public enum MessageType implements Maskable {

    GENERAL(EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_TYPE_GENERAL_BIT_EXT),
    VALIDATION(EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_TYPE_VALIDATION_BIT_EXT),
    PERFORMANCE(EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_TYPE_PERFORMANCE_BIT_EXT);

    private final int bit;

    MessageType(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
