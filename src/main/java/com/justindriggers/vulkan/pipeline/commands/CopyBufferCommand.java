package com.justindriggers.vulkan.pipeline.commands;

import com.justindriggers.vulkan.buffer.Buffer;
import com.justindriggers.vulkan.pipeline.CommandBuffer;
import org.lwjgl.vulkan.VkBufferCopy;

import static org.lwjgl.vulkan.VK10.vkCmdCopyBuffer;

public class CopyBufferCommand implements Command {

    private final Buffer sourceBuffer;
    private final Buffer destinationBuffer;
    private final long size;

    public CopyBufferCommand(final Buffer sourceBuffer,
                             final Buffer destinationBuffer,
                             final long size) {
        this.sourceBuffer = sourceBuffer;
        this.destinationBuffer = destinationBuffer;
        this.size = size;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        final VkBufferCopy.Buffer bufferCopy = VkBufferCopy.calloc(1)
                .srcOffset(0L)
                .dstOffset(0L)
                .size(size);

        vkCmdCopyBuffer(commandBuffer.unwrap(), sourceBuffer.getAddress(), destinationBuffer.getAddress(), bufferCopy);
    }
}
