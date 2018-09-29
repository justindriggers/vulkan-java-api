package com.justindriggers.vulkan.pipeline;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.pipeline.descriptor.DescriptorSetLayout;
import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;

import java.nio.LongBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreatePipelineLayout;
import static org.lwjgl.vulkan.VK10.vkDestroyPipelineLayout;

public class PipelineLayout extends DisposablePointer {

    private final LogicalDevice device;

    public PipelineLayout(final LogicalDevice device,
                          final List<DescriptorSetLayout> descriptorSetLayouts) {
        super(createPipelineLayout(device, descriptorSetLayouts));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyPipelineLayout(device.unwrap(), address, null);
    }

    private static long createPipelineLayout(final LogicalDevice device,
                                             final List<DescriptorSetLayout> descriptorSetLayouts) {
        final long result;

        final LongBuffer pipelineLayout = memAllocLong(1);

        final List<DescriptorSetLayout> descriptorSetLayoutsSafe = Optional.ofNullable(descriptorSetLayouts)
                .orElseGet(Collections::emptyList);

        final LongBuffer setLayouts = memAllocLong(descriptorSetLayoutsSafe.size());
        descriptorSetLayoutsSafe.forEach(descriptorSetLayout -> setLayouts.put(descriptorSetLayout.getAddress()));
        setLayouts.flip();

        final VkPipelineLayoutCreateInfo layoutCreateInfo = VkPipelineLayoutCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO)
                .pSetLayouts(setLayouts);

        try {
            VulkanFunction.execute(() -> vkCreatePipelineLayout(device.unwrap(), layoutCreateInfo, null,
                    pipelineLayout));

            result = pipelineLayout.get(0);
        } finally {
            memFree(pipelineLayout);
            memFree(setLayouts);

            layoutCreateInfo.free();
        }

        return result;
    }
}
