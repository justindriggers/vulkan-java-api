package com.justindriggers.vulkan.image;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.image.models.ImageAspect;
import com.justindriggers.vulkan.image.models.ImageViewType;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Format;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import org.lwjgl.vulkan.VkImageViewCreateInfo;

import java.nio.LongBuffer;
import java.util.Set;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_COMPONENT_SWIZZLE_IDENTITY;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateImageView;
import static org.lwjgl.vulkan.VK10.vkDestroyImageView;

public class ImageView extends DisposablePointer {

    private final LogicalDevice device;

    public ImageView(final LogicalDevice device,
                     final Image image,
                     final ImageViewType viewType,
                     final Format format,
                     final Set<ImageAspect> aspects,
                     final int mipLevelCount,
                     final int layerCount) {
        super(createImageView(device, image, viewType, format, aspects, mipLevelCount, layerCount));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyImageView(device.unwrap(), address, null);
    }

    private static long createImageView(final LogicalDevice device,
                                        final Image image,
                                        final ImageViewType viewType,
                                        final Format format,
                                        final Set<ImageAspect> aspects,
                                        final int mipLevelCount,
                                        final int layerCount) {
        final long result;

        final LongBuffer imageView = memAllocLong(1);

        final VkImageViewCreateInfo imageViewCreateInfo = VkImageViewCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO)
                .image(image.getAddress())
                .viewType(HasValue.getValue(viewType))
                .format(HasValue.getValue(format));

        imageViewCreateInfo.components()
                .r(VK_COMPONENT_SWIZZLE_IDENTITY)
                .g(VK_COMPONENT_SWIZZLE_IDENTITY)
                .b(VK_COMPONENT_SWIZZLE_IDENTITY)
                .a(VK_COMPONENT_SWIZZLE_IDENTITY);

        imageViewCreateInfo.subresourceRange()
                .aspectMask(Maskable.toBitMask(aspects))
                .baseMipLevel(0)
                .levelCount(mipLevelCount)
                .baseArrayLayer(0)
                .layerCount(layerCount);

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
