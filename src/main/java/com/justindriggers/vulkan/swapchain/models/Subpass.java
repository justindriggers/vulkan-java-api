package com.justindriggers.vulkan.swapchain.models;

import com.justindriggers.vulkan.pipeline.models.PipelineBindPoint;

import java.util.List;

public class Subpass {

    private final PipelineBindPoint pipelineBindPoint;
    private final List<ColorAttachment> colorAttachments;
    private final DepthStencilAttachment depthStencilAttachment;

    public Subpass(final PipelineBindPoint pipelineBindPoint,
                   final List<ColorAttachment> colorAttachments,
                   final DepthStencilAttachment depthStencilAttachment) {
        this.pipelineBindPoint = pipelineBindPoint;
        this.colorAttachments = colorAttachments;
        this.depthStencilAttachment = depthStencilAttachment;
    }

    public PipelineBindPoint getPipelineBindPoint() {
        return pipelineBindPoint;
    }

    public List<ColorAttachment> getColorAttachments() {
        return colorAttachments;
    }

    public DepthStencilAttachment getDepthStencilAttachment() {
        return depthStencilAttachment;
    }
}
