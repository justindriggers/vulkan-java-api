package com.justindriggers.vulkan.devices.physical.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum PhysicalDeviceType implements HasValue<Integer> {

    OTHER(VK10.VK_PHYSICAL_DEVICE_TYPE_OTHER),
    INTEGRATED_GPU(VK10.VK_PHYSICAL_DEVICE_TYPE_INTEGRATED_GPU),
    DISCRETE_GPU(VK10.VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU),
    VIRTUAL_GPU(VK10.VK_PHYSICAL_DEVICE_TYPE_VIRTUAL_GPU),
    CPU(VK10.VK_PHYSICAL_DEVICE_TYPE_CPU);

    private final int value;

    PhysicalDeviceType(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
