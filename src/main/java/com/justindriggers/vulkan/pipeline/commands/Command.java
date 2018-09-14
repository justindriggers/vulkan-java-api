package com.justindriggers.vulkan.pipeline.commands;

import com.justindriggers.vulkan.pipeline.CommandBuffer;

public interface Command {

    void execute(final CommandBuffer commandBuffer);
}
