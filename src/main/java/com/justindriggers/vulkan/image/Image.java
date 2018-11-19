package com.justindriggers.vulkan.image;

import com.justindriggers.vulkan.buffer.models.MemoryRequirements;
import com.justindriggers.vulkan.devices.logical.DeviceMemory;
import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.image.models.ImageCreateFlag;
import com.justindriggers.vulkan.image.models.ImageLayout;
import com.justindriggers.vulkan.image.models.ImageTiling;
import com.justindriggers.vulkan.image.models.ImageType;
import com.justindriggers.vulkan.image.models.ImageUsage;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Extent3D;
import com.justindriggers.vulkan.models.Format;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.SampleCount;
import com.justindriggers.vulkan.models.SharingMode;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.queue.QueueFamily;
import org.lwjgl.vulkan.VkExtent3D;
import org.lwjgl.vulkan.VkImageCreateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Set;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkBindImageMemory;
import static org.lwjgl.vulkan.VK10.vkCreateImage;
import static org.lwjgl.vulkan.VK10.vkDestroyImage;
import static org.lwjgl.vulkan.VK10.vkGetImageMemoryRequirements;

public class Image extends DisposablePointer {

    private final LogicalDevice device;
    private final boolean isUserManaged;

    public Image(final LogicalDevice device,
                 final ImageType imageType,
                 final Format format,
                 final Extent3D extent,
                 final int mipLevels,
                 final int arrayLayers,
                 final SampleCount samples,
                 final ImageTiling tiling,
                 final Set<ImageUsage> usages,
                 final SharingMode sharingMode,
                 final Set<QueueFamily> queueFamilies,
                 final ImageLayout initialLayout,
                 final ImageCreateFlag... flags) {
        super(createImage(device, imageType, format, extent, mipLevels, arrayLayers, samples, tiling, usages,
                sharingMode, queueFamilies, initialLayout, flags));
        this.device = device;
        isUserManaged = true;
    }

    Image(final long handle) {
        super(handle);
        device = null;
        isUserManaged = false;
    }

    @Override
    protected void dispose(final long address) {
        if (isUserManaged) {
            vkDestroyImage(device.unwrap(), address, null);
        } else {
            throw new UnsupportedOperationException("Only user-managed images may be disposed of manually.");
        }
    }

    public MemoryRequirements getMemoryRequirements() {
        final MemoryRequirements result;

        final VkMemoryRequirements memoryRequirements = VkMemoryRequirements.calloc();

        try {
            vkGetImageMemoryRequirements(device.unwrap(), getAddress(), memoryRequirements);

            result = new MemoryRequirements(memoryRequirements);
        } finally {
            memoryRequirements.free();
        }

        return result;
    }

    public void bindMemory(final DeviceMemory deviceMemory) {
        VulkanFunction.execute(() -> vkBindImageMemory(device.unwrap(), getAddress(), deviceMemory.getAddress(), 0L));
    }

    private static long createImage(final LogicalDevice device,
                                    final ImageType imageType,
                                    final Format format,
                                    final Extent3D extent,
                                    final int mipLevels,
                                    final int arrayLayers,
                                    final SampleCount samples,
                                    final ImageTiling tiling,
                                    final Set<ImageUsage> usages,
                                    final SharingMode sharingMode,
                                    final Set<QueueFamily> queueFamilies,
                                    final ImageLayout initialLayout,
                                    final ImageCreateFlag... flags) {
        final long result;

        final LongBuffer image = memAllocLong(1);

        final IntBuffer queueFamilyIndices = memAllocInt(queueFamilies.size());

        queueFamilies.stream()
                .mapToInt(QueueFamily::getIndex)
                .forEach(queueFamilyIndices::put);

        queueFamilyIndices.flip();

        final VkExtent3D extent3D = VkExtent3D.calloc()
                .width(extent.getWidth())
                .height(extent.getHeight())
                .depth(extent.getDepth());

        final VkImageCreateInfo imageCreateInfo = VkImageCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO)
                .flags(Maskable.toBitMask(flags))
                .imageType(HasValue.getValue(imageType))
                .format(HasValue.getValue(format))
                .extent(extent3D)
                .mipLevels(mipLevels)
                .arrayLayers(arrayLayers)
                .samples(Maskable.toBit(samples))
                .tiling(HasValue.getValue(tiling))
                .usage(Maskable.toBitMask(usages))
                .sharingMode(HasValue.getValue(sharingMode))
                .pQueueFamilyIndices(queueFamilyIndices)
                .initialLayout(HasValue.getValue(initialLayout));

        try {
            VulkanFunction.execute(() -> vkCreateImage(device.unwrap(), imageCreateInfo, null, image));

            result = image.get(0);
        } finally {
            memFree(image);
            memFree(queueFamilyIndices);

            extent3D.free();
            imageCreateInfo.free();
        }

        return result;
    }
}
