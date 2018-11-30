package com.justindriggers.vulkan.pipeline.descriptor;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import org.lwjgl.vulkan.VkDescriptorSetLayoutBinding;
import org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo;

import java.nio.LongBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateDescriptorSetLayout;
import static org.lwjgl.vulkan.VK10.vkDestroyDescriptorSetLayout;

public class DescriptorSetLayout extends DisposablePointer {

    private final LogicalDevice device;

    public DescriptorSetLayout(final LogicalDevice device,
                               final List<DescriptorSetLayoutBinding> bindings) {
        super(createDescriptorSetLayout(device, bindings));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyDescriptorSetLayout(device.unwrap(), address, null);
    }

    private static long createDescriptorSetLayout(final LogicalDevice device,
                                                  final List<DescriptorSetLayoutBinding> bindings) {
        final long result;

        final LongBuffer descriptorSetLayout = memAllocLong(1);

        final List<DescriptorSetLayoutBinding> bindingsSafe = Optional.ofNullable(bindings)
                .orElseGet(Collections::emptyList);

        final VkDescriptorSetLayoutBinding.Buffer descriptorSetLayoutBindings = VkDescriptorSetLayoutBinding.calloc(bindingsSafe.size());

        bindingsSafe.stream()
                .map(binding -> VkDescriptorSetLayoutBinding.calloc()
                        .binding(binding.getBinding())
                        .descriptorType(HasValue.getValue(binding.getDescriptorType()))
                        .descriptorCount(binding.getDescriptorCount())
                        .stageFlags(Maskable.toBitMask(binding.getStageFlags())))
                .forEachOrdered(descriptorSetLayoutBindings::put);

        descriptorSetLayoutBindings.flip();

        final VkDescriptorSetLayoutCreateInfo descriptorSetLayoutCreateInfo = VkDescriptorSetLayoutCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO)
                .pBindings(descriptorSetLayoutBindings);

        try {
            VulkanFunction.execute(() -> vkCreateDescriptorSetLayout(device.unwrap(), descriptorSetLayoutCreateInfo,
                    null, descriptorSetLayout));

            result = descriptorSetLayout.get(0);
        } finally {
            memFree(descriptorSetLayout);

            descriptorSetLayoutBindings.free();
            descriptorSetLayoutCreateInfo.free();
        }

        return result;
    }
}
