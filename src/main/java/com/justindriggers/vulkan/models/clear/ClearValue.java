package com.justindriggers.vulkan.models.clear;

import org.lwjgl.vulkan.VkClearValue;

public abstract class ClearValue {

    public abstract VkClearValue toStruct();
}
