package com.justindriggers.vulkan.pipeline.commands;

import com.justindriggers.vulkan.pipeline.CommandBuffer;

import static org.lwjgl.vulkan.VK10.vkCmdEndRenderPass;

public class EndRenderPassCommand implements Command {

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        vkCmdEndRenderPass(commandBuffer.unwrap());
    }
}
