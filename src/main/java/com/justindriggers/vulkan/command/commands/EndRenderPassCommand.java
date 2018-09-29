package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.command.CommandBuffer;

import static org.lwjgl.vulkan.VK10.vkCmdEndRenderPass;

public class EndRenderPassCommand implements Command {

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        vkCmdEndRenderPass(commandBuffer.unwrap());
    }
}
