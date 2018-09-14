package com.justindriggers.vulkan.pipeline.commands;

import com.justindriggers.vulkan.pipeline.CommandBuffer;

import static org.lwjgl.vulkan.VK10.vkCmdDraw;

public class DrawCommand implements Command {

    private final int vertexCount;
    private final int instanceCount;
    private final int firstVertex;
    private final int firstInstance;

    public DrawCommand(final int vertexCount,
                       final int instanceCount,
                       final int firstVertex,
                       final int firstInstance) {
        this.vertexCount = vertexCount;
        this.instanceCount = instanceCount;
        this.firstVertex = firstVertex;
        this.firstInstance = firstInstance;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        vkCmdDraw(commandBuffer.unwrap(), vertexCount, instanceCount, firstVertex, firstInstance);
    }
}
