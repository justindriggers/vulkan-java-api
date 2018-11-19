package com.justindriggers.vulkan.pipeline.descriptor;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.pipeline.descriptor.models.DescriptorPoolFlag;
import com.justindriggers.vulkan.pipeline.descriptor.models.DescriptorType;
import org.lwjgl.vulkan.VkDescriptorPoolCreateInfo;
import org.lwjgl.vulkan.VkDescriptorPoolSize;
import org.lwjgl.vulkan.VkDescriptorSetAllocateInfo;

import java.nio.LongBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO;
import static org.lwjgl.vulkan.VK10.vkAllocateDescriptorSets;
import static org.lwjgl.vulkan.VK10.vkCreateDescriptorPool;
import static org.lwjgl.vulkan.VK10.vkDestroyDescriptorPool;
import static org.lwjgl.vulkan.VK10.vkFreeDescriptorSets;

public class DescriptorPool extends DisposablePointer {

    private final LogicalDevice device;
    private final DescriptorType type;

    public DescriptorPool(final LogicalDevice device,
                          final DescriptorType type,
                          final int size,
                          final DescriptorPoolFlag... flags) {
        super(createDescriptorPool(device, type, size, flags));
        this.device = device;
        this.type = type;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyDescriptorPool(device.unwrap(), address, null);
    }

    public List<DescriptorSet> allocateDescriptorSets(final List<DescriptorSetLayout> descriptorSetLayouts) {
        final List<DescriptorSet> result;

        final List<DescriptorSetLayout> descriptorSetLayoutsSafe = Optional.ofNullable(descriptorSetLayouts)
                .orElseGet(Collections::emptyList);

        final int size = descriptorSetLayoutsSafe.size();

        final LongBuffer descriptorSets = memAllocLong(size);

        final LongBuffer setLayouts = memAllocLong(size);
        descriptorSetLayoutsSafe.forEach(descriptorSetLayout -> setLayouts.put(descriptorSetLayout.getAddress()));
        setLayouts.flip();

        final VkDescriptorSetAllocateInfo descriptorSetAllocateInfo = VkDescriptorSetAllocateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO)
                .descriptorPool(getAddress())
                .pSetLayouts(setLayouts);

        try {
            VulkanFunction.execute(() -> vkAllocateDescriptorSets(device.unwrap(), descriptorSetAllocateInfo,
                    descriptorSets));

            result = IntStream.range(0, size)
                    .mapToLong(descriptorSets::get)
                    .mapToObj(handle -> new DescriptorSet(device, handle, type))
                    .collect(Collectors.toList());
        } finally {
            memFree(descriptorSets);
            memFree(setLayouts);

            descriptorSetAllocateInfo.free();
        }

        return result;
    }

    public void freeDescriptorSets(final Collection<DescriptorSet> descriptorSets) {
        final Collection<DescriptorSet> descriptorSetsSafe = Optional.ofNullable(descriptorSets)
                .orElseGet(Collections::emptySet);

        final LongBuffer descriptorSetAddresses = memAllocLong(descriptorSetsSafe.size());
        descriptorSetsSafe.forEach(descriptorSet -> descriptorSetAddresses.put(descriptorSet.getAddress()));
        descriptorSetAddresses.flip();

        try {
            VulkanFunction.execute(() -> vkFreeDescriptorSets(device.unwrap(), getAddress(), descriptorSetAddresses));
        } finally {
            memFree(descriptorSetAddresses);
        }
    }

    private static long createDescriptorPool(final LogicalDevice device,
                                             final DescriptorType type,
                                             final int size,
                                             final DescriptorPoolFlag... flags) {
        final long result;

        final LongBuffer descriptorPool = memAllocLong(1);

        final VkDescriptorPoolSize.Buffer descriptorPoolSizes = VkDescriptorPoolSize.calloc(1)
                .type(HasValue.getValue(type))
                .descriptorCount(size);

        final VkDescriptorPoolCreateInfo descriptorPoolCreateInfo = VkDescriptorPoolCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO)
                .pPoolSizes(descriptorPoolSizes)
                .maxSets(size)
                .flags(Maskable.toBitMask(flags));

        try {
            VulkanFunction.execute(() -> vkCreateDescriptorPool(device.unwrap(), descriptorPoolCreateInfo, null,
                    descriptorPool));

            result = descriptorPool.get(0);
        } finally {
            memFree(descriptorPool);

            descriptorPoolSizes.free();
            descriptorPoolCreateInfo.free();
        }

        return result;
    }
}
