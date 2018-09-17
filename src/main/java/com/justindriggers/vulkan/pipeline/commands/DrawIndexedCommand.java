package com.justindriggers.vulkan.pipeline.commands;

import com.justindriggers.vulkan.pipeline.CommandBuffer;

import static org.lwjgl.vulkan.VK10.vkCmdDrawIndexed;

public class DrawIndexedCommand implements Command {

    private final int indexCount;
    private final int instanceCount;
    private final int firstIndex;
    private final int vertexOffset;
    private final int firstInstance;

    public DrawIndexedCommand(final int indexCount,
                              final int instanceCount,
                              final int firstIndex,
                              final int vertexOffset,
                              final int firstInstance) {
        this.indexCount = indexCount;
        this.instanceCount = instanceCount;
        this.firstIndex = firstIndex;
        this.vertexOffset = vertexOffset;
        this.firstInstance = firstInstance;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        vkCmdDrawIndexed(commandBuffer.unwrap(), indexCount, instanceCount, firstIndex, vertexOffset, firstInstance);
    }
}
