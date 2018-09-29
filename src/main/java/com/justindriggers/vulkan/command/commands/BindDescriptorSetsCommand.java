package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.command.CommandBuffer;
import com.justindriggers.vulkan.models.pointers.Pointer;
import com.justindriggers.vulkan.pipeline.PipelineLayout;
import com.justindriggers.vulkan.pipeline.descriptor.DescriptorSet;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Collection;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_PIPELINE_BIND_POINT_GRAPHICS;
import static org.lwjgl.vulkan.VK10.vkCmdBindDescriptorSets;

public class BindDescriptorSetsCommand implements Command {

    private final PipelineLayout pipelineLayout;
    private final Collection<DescriptorSet> descriptorSets;

    public BindDescriptorSetsCommand(final PipelineLayout pipelineLayout,
                                     final Collection<DescriptorSet> descriptorSets) {
        this.pipelineLayout = pipelineLayout;
        this.descriptorSets = descriptorSets;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        final LongBuffer descriptorSetHandles = memAllocLong(descriptorSets.size());

        descriptorSets.stream()
                .map(Pointer::getAddress)
                .forEach(descriptorSetHandles::put);

        descriptorSetHandles.flip();

        final IntBuffer dynamicOffsets = memAllocInt(0);

        try {
            vkCmdBindDescriptorSets(commandBuffer.unwrap(), VK_PIPELINE_BIND_POINT_GRAPHICS,
                    pipelineLayout.getAddress(), 0, descriptorSetHandles, dynamicOffsets);
        } finally {
            memFree(descriptorSetHandles);
            memFree(dynamicOffsets);
        }
    }
}
