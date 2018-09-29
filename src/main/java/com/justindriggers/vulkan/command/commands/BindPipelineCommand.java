package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.command.CommandBuffer;
import com.justindriggers.vulkan.pipeline.GraphicsPipeline;

import static org.lwjgl.vulkan.VK10.VK_PIPELINE_BIND_POINT_GRAPHICS;
import static org.lwjgl.vulkan.VK10.vkCmdBindPipeline;

public class BindPipelineCommand implements Command {

    private final GraphicsPipeline graphicsPipeline;

    public BindPipelineCommand(final GraphicsPipeline graphicsPipeline) {
        this.graphicsPipeline = graphicsPipeline;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        vkCmdBindPipeline(commandBuffer.unwrap(), VK_PIPELINE_BIND_POINT_GRAPHICS, graphicsPipeline.getAddress());
    }
}
