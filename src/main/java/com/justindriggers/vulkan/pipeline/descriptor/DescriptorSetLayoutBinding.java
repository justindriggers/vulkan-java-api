package com.justindriggers.vulkan.pipeline.descriptor;

import com.justindriggers.vulkan.pipeline.descriptor.models.DescriptorType;
import com.justindriggers.vulkan.pipeline.shader.ShaderStageType;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DescriptorSetLayoutBinding {

    private final int binding;
    private final DescriptorType descriptorType;
    private final int descriptorCount;
    private final Set<ShaderStageType> stageFlags;

    public DescriptorSetLayoutBinding(final int binding,
                                      final DescriptorType descriptorType,
                                      final int descriptorCount,
                                      final ShaderStageType... stageFlags) {
        this.binding = binding;
        this.descriptorType = descriptorType;
        this.descriptorCount = descriptorCount;

        this.stageFlags = Stream.of(stageFlags).collect(Collectors.toSet());
    }

    int getBinding() {
        return binding;
    }

    DescriptorType getDescriptorType() {
        return descriptorType;
    }

    int getDescriptorCount() {
        return descriptorCount;
    }

    Set<ShaderStageType> getStageFlags() {
        return stageFlags;
    }
}
