package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.command.CommandBuffer;

public interface Command {

    void execute(final CommandBuffer commandBuffer);
}
