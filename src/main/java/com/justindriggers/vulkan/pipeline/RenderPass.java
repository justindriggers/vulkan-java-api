package com.justindriggers.vulkan.pipeline;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.pipeline.models.PipelineStage;
import com.justindriggers.vulkan.surface.models.format.ColorFormat;
import org.lwjgl.vulkan.VkAttachmentDescription;
import org.lwjgl.vulkan.VkAttachmentReference;
import org.lwjgl.vulkan.VkRenderPassCreateInfo;
import org.lwjgl.vulkan.VkSubpassDependency;
import org.lwjgl.vulkan.VkSubpassDescription;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.KHRSwapchain.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR;
import static org.lwjgl.vulkan.VK10.VK_ACCESS_COLOR_ATTACHMENT_READ_BIT;
import static org.lwjgl.vulkan.VK10.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT;
import static org.lwjgl.vulkan.VK10.VK_ATTACHMENT_LOAD_OP_CLEAR;
import static org.lwjgl.vulkan.VK10.VK_ATTACHMENT_LOAD_OP_DONT_CARE;
import static org.lwjgl.vulkan.VK10.VK_ATTACHMENT_STORE_OP_DONT_CARE;
import static org.lwjgl.vulkan.VK10.VK_ATTACHMENT_STORE_OP_STORE;
import static org.lwjgl.vulkan.VK10.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL;
import static org.lwjgl.vulkan.VK10.VK_IMAGE_LAYOUT_UNDEFINED;
import static org.lwjgl.vulkan.VK10.VK_PIPELINE_BIND_POINT_GRAPHICS;
import static org.lwjgl.vulkan.VK10.VK_SAMPLE_COUNT_1_BIT;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_SUBPASS_EXTERNAL;
import static org.lwjgl.vulkan.VK10.vkCreateRenderPass;
import static org.lwjgl.vulkan.VK10.vkDestroyRenderPass;

public class RenderPass extends DisposablePointer {

    private final LogicalDevice device;

    public RenderPass(final LogicalDevice device,
                      final ColorFormat colorFormat) {
        super(createRenderPass(device, colorFormat));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyRenderPass(device.unwrap(), address, null);
    }

    private static long createRenderPass(final LogicalDevice device,
                                         final ColorFormat colorFormat) {
        final long result;

        final LongBuffer renderPass = memAllocLong(1);

        final VkAttachmentReference.Buffer colorAttachmentReference = VkAttachmentReference.calloc(1)
                .attachment(0)
                .layout(VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);

        final VkSubpassDescription.Buffer subpass = VkSubpassDescription.calloc(1)
                .pipelineBindPoint(VK_PIPELINE_BIND_POINT_GRAPHICS)
                .colorAttachmentCount(1)
                .pColorAttachments(colorAttachmentReference);

        final VkAttachmentDescription.Buffer attachmentDescription = VkAttachmentDescription.calloc(1)
                .format(HasValue.getValue(colorFormat))
                .samples(VK_SAMPLE_COUNT_1_BIT)
                .loadOp(VK_ATTACHMENT_LOAD_OP_CLEAR)
                .storeOp(VK_ATTACHMENT_STORE_OP_STORE)
                .stencilLoadOp(VK_ATTACHMENT_LOAD_OP_DONT_CARE)
                .stencilStoreOp(VK_ATTACHMENT_STORE_OP_DONT_CARE)
                .initialLayout(VK_IMAGE_LAYOUT_UNDEFINED)
                .finalLayout(VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);

        final VkSubpassDependency.Buffer subpassDependency = VkSubpassDependency.calloc(1)
                .srcSubpass(VK_SUBPASS_EXTERNAL)
                .dstSubpass(0)
                .srcStageMask(Maskable.toBitMask(PipelineStage.COLOR_ATTACHMENT_OUTPUT))
                .srcAccessMask(0)
                .dstStageMask(Maskable.toBitMask(PipelineStage.COLOR_ATTACHMENT_OUTPUT))
                .dstAccessMask(VK_ACCESS_COLOR_ATTACHMENT_READ_BIT | VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT);

        final VkRenderPassCreateInfo renderPassCreateInfo = VkRenderPassCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO)
                .pAttachments(attachmentDescription)
                .pSubpasses(subpass)
                .pDependencies(subpassDependency);

        try {
            VulkanFunction.execute(() -> vkCreateRenderPass(device.unwrap(), renderPassCreateInfo, null, renderPass));

            result = renderPass.get(0);
        } finally {
            memFree(renderPass);

            colorAttachmentReference.free();
            subpass.free();
            attachmentDescription.free();
            subpassDependency.free();
            renderPassCreateInfo.free();
        }

        return result;
    }
}
