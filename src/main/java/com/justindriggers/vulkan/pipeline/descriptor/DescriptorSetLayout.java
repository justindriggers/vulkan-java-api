package com.justindriggers.vulkan.pipeline.descriptor;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.pipeline.descriptor.models.DescriptorType;
import com.justindriggers.vulkan.pipeline.shader.ShaderStageType;
import org.lwjgl.vulkan.VkDescriptorSetLayoutBinding;
import org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateDescriptorSetLayout;
import static org.lwjgl.vulkan.VK10.vkDestroyDescriptorSetLayout;

public class DescriptorSetLayout extends DisposablePointer {

    private final LogicalDevice device;

    public DescriptorSetLayout(final LogicalDevice device,
                               final int binding,
                               final int descriptorCount,
                               final DescriptorType descriptorType,
                               final ShaderStageType... stageFlags) {
        super(createDescriptorSetLayout(device, binding, descriptorCount, descriptorType, stageFlags));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyDescriptorSetLayout(device.unwrap(), address, null);
    }

    private static long createDescriptorSetLayout(final LogicalDevice device,
                                                  final int binding,
                                                  final int descriptorCount,
                                                  final DescriptorType descriptorType,
                                                  final ShaderStageType... stageFlags) {
        final long result;

        final LongBuffer descriptorSetLayout = memAllocLong(1);

        final VkDescriptorSetLayoutBinding.Buffer descriptorSetLayoutBinding = VkDescriptorSetLayoutBinding.calloc(1)
                .binding(binding)
                .descriptorCount(descriptorCount)
                .descriptorType(HasValue.getValue(descriptorType))
                .stageFlags(Maskable.toBitMask(stageFlags));

        final VkDescriptorSetLayoutCreateInfo descriptorSetLayoutCreateInfo = VkDescriptorSetLayoutCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO)
                .pBindings(descriptorSetLayoutBinding);

        try {
            VulkanFunction.execute(() -> vkCreateDescriptorSetLayout(device.unwrap(), descriptorSetLayoutCreateInfo,
                    null, descriptorSetLayout));

            result = descriptorSetLayout.get(0);
        } finally {
            memFree(descriptorSetLayout);

            descriptorSetLayoutBinding.free();
            descriptorSetLayoutCreateInfo.free();
        }

        return result;
    }
}
