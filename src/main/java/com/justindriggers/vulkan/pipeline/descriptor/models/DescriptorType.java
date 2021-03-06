package com.justindriggers.vulkan.pipeline.descriptor.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum DescriptorType implements HasValue<Integer> {

    SAMPLER(VK10.VK_DESCRIPTOR_TYPE_SAMPLER),
    COMBINED_IMAGE_SAMPLER(VK10.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER),
    SAMPLED_IMAGE(VK10.VK_DESCRIPTOR_TYPE_SAMPLED_IMAGE),
    STORAGE_IMAGE(VK10.VK_DESCRIPTOR_TYPE_STORAGE_IMAGE),
    UNIFORM_TEXEL_BUFFER(VK10.VK_DESCRIPTOR_TYPE_UNIFORM_TEXEL_BUFFER),
    STORAGE_TEXEL_BUFFER(VK10.VK_DESCRIPTOR_TYPE_STORAGE_TEXEL_BUFFER),
    UNIFORM_BUFFER(VK10.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER),
    STORAGE_BUFFER(VK10.VK_DESCRIPTOR_TYPE_STORAGE_BUFFER),
    UNIFORM_BUFFER_DYNAMIC(VK10.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER_DYNAMIC),
    STORAGE_BUFFER_DYNAMIC(VK10.VK_DESCRIPTOR_TYPE_STORAGE_BUFFER_DYNAMIC),
    INPUT_ATTACHMENT(VK10.VK_DESCRIPTOR_TYPE_INPUT_ATTACHMENT);

    private final int value;

    DescriptorType(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
