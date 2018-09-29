package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.buffer.Buffer;
import com.justindriggers.vulkan.command.CommandBuffer;

import java.nio.LongBuffer;
import java.util.List;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.vkCmdBindVertexBuffers;

public class BindVertexBuffersCommand implements Command {

    private final List<Buffer> vertexBuffers;

    public BindVertexBuffersCommand(final List<Buffer> vertexBuffers) {
        this.vertexBuffers = vertexBuffers;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        final LongBuffer vertexBufferHandles = memAllocLong(vertexBuffers.size());
        final LongBuffer offsets = memAllocLong(vertexBuffers.size());

        int currentOffset = 0;

        for (final Buffer vertexBuffer : vertexBuffers) {
            vertexBufferHandles.put(vertexBuffer.getAddress());
            offsets.put(currentOffset);

            currentOffset += vertexBuffer.getSize();
        }

        vertexBufferHandles.flip();
        offsets.flip();

        try {
            vkCmdBindVertexBuffers(commandBuffer.unwrap(), 0, vertexBufferHandles, offsets);
        } finally {
            memFree(vertexBufferHandles);
            memFree(offsets);
        }
    }
}
