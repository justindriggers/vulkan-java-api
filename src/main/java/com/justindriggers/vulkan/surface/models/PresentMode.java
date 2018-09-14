package com.justindriggers.vulkan.surface.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.KHRSurface;

public enum PresentMode implements HasValue<Integer> {

    IMMEDIATE(KHRSurface.VK_PRESENT_MODE_IMMEDIATE_KHR),
    MAILBOX(KHRSurface.VK_PRESENT_MODE_MAILBOX_KHR),
    FIFO(KHRSurface.VK_PRESENT_MODE_FIFO_KHR),
    FIFO_RELAXED(KHRSurface.VK_PRESENT_MODE_FIFO_RELAXED_KHR);

    private final int value;

    PresentMode(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
