package com.justindriggers.vulkan.swapchain;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.surface.models.format.ColorFormat;
import com.justindriggers.vulkan.swapchain.models.Image;
import org.lwjgl.vulkan.VkImageViewCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_COMPONENT_SWIZZLE_IDENTITY;
import static org.lwjgl.vulkan.VK10.VK_IMAGE_ASPECT_COLOR_BIT;
import static org.lwjgl.vulkan.VK10.VK_IMAGE_VIEW_TYPE_2D;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateImageView;
import static org.lwjgl.vulkan.VK10.vkDestroyImageView;

public class ImageView extends DisposablePointer {

    private final LogicalDevice device;

    public ImageView(final LogicalDevice device,
                     final Image image,
                     final ColorFormat format) {
        super(createImageView(device, image, format));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyImageView(device.unwrap(), address, null);
    }

    private static long createImageView(final LogicalDevice device,
                                        final Image image,
                                        final ColorFormat format) {
        final long result;

        final LongBuffer imageView = memAllocLong(1);

        final VkImageViewCreateInfo imageViewCreateInfo = VkImageViewCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO)
                .image(image.getAddress())
                .viewType(VK_IMAGE_VIEW_TYPE_2D)
                .format(HasValue.getValue(format));

        imageViewCreateInfo.components()
                .r(VK_COMPONENT_SWIZZLE_IDENTITY)
                .g(VK_COMPONENT_SWIZZLE_IDENTITY)
                .b(VK_COMPONENT_SWIZZLE_IDENTITY)
                .a(VK_COMPONENT_SWIZZLE_IDENTITY);

        imageViewCreateInfo.subresourceRange()
                .aspectMask(VK_IMAGE_ASPECT_COLOR_BIT)
                .baseMipLevel(0)
                .levelCount(1)
                .baseArrayLayer(0)
                .layerCount(1);

        try {
            VulkanFunction.execute(() -> vkCreateImageView(device.unwrap(), imageViewCreateInfo, null, imageView));

            result = imageView.get(0);
        } finally {
            memFree(imageView);

            imageViewCreateInfo.free();
        }

        return result;
    }
}
