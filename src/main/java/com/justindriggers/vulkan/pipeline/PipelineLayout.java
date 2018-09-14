package com.justindriggers.vulkan.pipeline;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreatePipelineLayout;
import static org.lwjgl.vulkan.VK10.vkDestroyPipelineLayout;

public class PipelineLayout extends DisposablePointer {

    private final LogicalDevice device;

    public PipelineLayout(final LogicalDevice device) {
        super(createPipelineLayout(device));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyPipelineLayout(device.unwrap(), address, null);
    }

    private static long createPipelineLayout(final LogicalDevice device) {
        final long result;

        final LongBuffer pipelineLayout = memAllocLong(1);

        final VkPipelineLayoutCreateInfo layoutCreateInfo = VkPipelineLayoutCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO)
                .pSetLayouts(null);

        try {
            VulkanFunction.execute(() -> vkCreatePipelineLayout(device.unwrap(), layoutCreateInfo, null,
                    pipelineLayout));

            result = pipelineLayout.get(0);
        } finally {
            memFree(pipelineLayout);

            layoutCreateInfo.free();
        }

        return result;
    }
}
