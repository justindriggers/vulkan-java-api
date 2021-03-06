package com.justindriggers.vulkan.swapchain;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.image.ImageView;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Extent2D;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import org.lwjgl.vulkan.VkFramebufferCreateInfo;

import java.nio.LongBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateFramebuffer;
import static org.lwjgl.vulkan.VK10.vkDestroyFramebuffer;

public class Framebuffer extends DisposablePointer {

    private final LogicalDevice device;

    public Framebuffer(final LogicalDevice device,
                       final RenderPass renderPass,
                       final List<ImageView> attachments,
                       final Extent2D imageExtent) {
        super(createFramebuffer(device, renderPass, attachments, imageExtent));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyFramebuffer(device.unwrap(), address, null);
    }

    private static long createFramebuffer(final LogicalDevice device,
                                          final RenderPass renderPass,
                                          final List<ImageView> attachments,
                                          final Extent2D imageExtent) {
        final long result;

        final List<ImageView> attachmentsSafe = Optional.ofNullable(attachments)
                .orElseGet(Collections::emptyList);

        final LongBuffer attachmentBuffer = memAllocLong(attachmentsSafe.size());

        attachmentsSafe.stream()
                .mapToLong(ImageView::getAddress)
                .forEachOrdered(attachmentBuffer::put);

        attachmentBuffer.flip();

        final LongBuffer framebuffer = memAllocLong(1);

        final VkFramebufferCreateInfo framebufferCreateInfo = VkFramebufferCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO)
                .renderPass(renderPass.getAddress())
                .pAttachments(attachmentBuffer)
                .width(imageExtent.getWidth())
                .height(imageExtent.getHeight())
                .layers(1);

        try {
            VulkanFunction.execute(() -> vkCreateFramebuffer(device.unwrap(), framebufferCreateInfo, null,
                    framebuffer));

            result = framebuffer.get(0);
        } finally {
            memFree(attachmentBuffer);
            memFree(framebuffer);

            framebufferCreateInfo.free();
        }

        return result;
    }
}
