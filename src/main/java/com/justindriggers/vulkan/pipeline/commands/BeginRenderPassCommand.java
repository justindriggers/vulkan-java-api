package com.justindriggers.vulkan.pipeline.commands;

import com.justindriggers.vulkan.models.Extent2D;
import com.justindriggers.vulkan.pipeline.CommandBuffer;
import com.justindriggers.vulkan.pipeline.RenderPass;
import com.justindriggers.vulkan.swapchain.Framebuffer;
import org.lwjgl.vulkan.VkClearValue;
import org.lwjgl.vulkan.VkRenderPassBeginInfo;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO;
import static org.lwjgl.vulkan.VK10.VK_SUBPASS_CONTENTS_INLINE;
import static org.lwjgl.vulkan.VK10.vkCmdBeginRenderPass;

public class BeginRenderPassCommand implements Command {

    private final RenderPass renderPass;
    private final Framebuffer framebuffer;
    private final Extent2D imageExtent;

    public BeginRenderPassCommand(final RenderPass renderPass,
                                  final Framebuffer framebuffer,
                                  final Extent2D imageExtent) {
        this.renderPass = renderPass;
        this.framebuffer = framebuffer;
        this.imageExtent = imageExtent;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        final VkClearValue.Buffer clearValues = VkClearValue.calloc(2);

        clearValues.get(0).color()
                .float32(0, 0.0f)
                .float32(1, 0.0f)
                .float32(2, 0.0f)
                .float32(3, 1.0f);

        final VkRenderPassBeginInfo renderPassBeginInfo = VkRenderPassBeginInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO)
                .renderPass(renderPass.getAddress())
                .framebuffer(framebuffer.getAddress())
                .pClearValues(clearValues);

        renderPassBeginInfo.renderArea().offset()
                .x(0)
                .y(0);

        renderPassBeginInfo.renderArea().extent()
                .width(imageExtent.getWidth())
                .height(imageExtent.getHeight());

        try {
            vkCmdBeginRenderPass(commandBuffer.unwrap(), renderPassBeginInfo, VK_SUBPASS_CONTENTS_INLINE);
        } finally {
            clearValues.free();
            renderPassBeginInfo.free();
        }
    }
}
