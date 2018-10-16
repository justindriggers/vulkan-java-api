package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.command.CommandBuffer;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Rect2D;
import com.justindriggers.vulkan.models.clear.ClearValue;
import com.justindriggers.vulkan.swapchain.Framebuffer;
import com.justindriggers.vulkan.swapchain.RenderPass;
import com.justindriggers.vulkan.swapchain.models.SubpassContents;
import org.lwjgl.vulkan.VkClearValue;
import org.lwjgl.vulkan.VkRenderPassBeginInfo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO;
import static org.lwjgl.vulkan.VK10.vkCmdBeginRenderPass;

public class BeginRenderPassCommand implements Command {

    private final SubpassContents subpassContents;
    private final RenderPass renderPass;
    private final Framebuffer framebuffer;
    private final Rect2D renderArea;
    private final List<ClearValue> clearValues;

    public BeginRenderPassCommand(final SubpassContents subpassContents,
                                  final RenderPass renderPass,
                                  final Framebuffer framebuffer,
                                  final Rect2D renderArea,
                                  final List<ClearValue> clearValues) {
        this.subpassContents = subpassContents;
        this.renderPass = renderPass;
        this.framebuffer = framebuffer;
        this.renderArea = renderArea;
        this.clearValues = clearValues;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        final List<ClearValue> clearValuesSafe = Optional.ofNullable(clearValues)
                .orElseGet(Collections::emptyList);

        final VkClearValue.Buffer clearValuesBuffer = VkClearValue.calloc(clearValuesSafe.size());

        clearValuesSafe.stream()
                .map(ClearValue::toStruct)
                .forEachOrdered(clearValuesBuffer::put);

        clearValuesBuffer.flip();

        final VkRenderPassBeginInfo renderPassBeginInfo = VkRenderPassBeginInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO)
                .renderPass(renderPass.getAddress())
                .framebuffer(framebuffer.getAddress())
                .pClearValues(clearValuesBuffer);

        renderPassBeginInfo.renderArea().offset()
                .x(renderArea.getOffset().getX())
                .y(renderArea.getOffset().getY());

        renderPassBeginInfo.renderArea().extent()
                .width(renderArea.getExtent().getWidth())
                .height(renderArea.getExtent().getHeight());

        try {
            vkCmdBeginRenderPass(commandBuffer.unwrap(), renderPassBeginInfo, HasValue.getValue(subpassContents));
        } finally {
            clearValuesBuffer.free();
            renderPassBeginInfo.free();
        }
    }
}
