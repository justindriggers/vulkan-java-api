package com.justindriggers.vulkan.pipeline.commands;

import com.justindriggers.vulkan.buffer.Buffer;
import com.justindriggers.vulkan.pipeline.CommandBuffer;

import static org.lwjgl.vulkan.VK10.VK_INDEX_TYPE_UINT32;
import static org.lwjgl.vulkan.VK10.vkCmdBindIndexBuffer;

public class BindIndexBufferCommand implements Command {

    private final Buffer indexBuffer;

    public BindIndexBufferCommand(final Buffer indexBuffer) {
        this.indexBuffer = indexBuffer;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        vkCmdBindIndexBuffer(commandBuffer.unwrap(), indexBuffer.getAddress(), 0, VK_INDEX_TYPE_UINT32); // TODO short/int/long
    }
}
