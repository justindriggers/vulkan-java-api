package com.justindriggers.vulkan.swapchain;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.image.Image;
import com.justindriggers.vulkan.image.SwapchainImage;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.ColorSpace;
import com.justindriggers.vulkan.models.Extent2D;
import com.justindriggers.vulkan.models.Format;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.models.pointers.Pointer;
import com.justindriggers.vulkan.queue.QueueFamily;
import com.justindriggers.vulkan.surface.Surface;
import com.justindriggers.vulkan.surface.models.PresentMode;
import com.justindriggers.vulkan.surface.models.capabilities.SurfaceTransform;
import com.justindriggers.vulkan.synchronize.Fence;
import com.justindriggers.vulkan.synchronize.Semaphore;
import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.KHRSurface.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR;
import static org.lwjgl.vulkan.KHRSwapchain.VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR;
import static org.lwjgl.vulkan.KHRSwapchain.vkAcquireNextImageKHR;
import static org.lwjgl.vulkan.KHRSwapchain.vkCreateSwapchainKHR;
import static org.lwjgl.vulkan.KHRSwapchain.vkDestroySwapchainKHR;
import static org.lwjgl.vulkan.KHRSwapchain.vkGetSwapchainImagesKHR;
import static org.lwjgl.vulkan.VK10.VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT;
import static org.lwjgl.vulkan.VK10.VK_NULL_HANDLE;
import static org.lwjgl.vulkan.VK10.VK_SHARING_MODE_CONCURRENT;
import static org.lwjgl.vulkan.VK10.VK_SHARING_MODE_EXCLUSIVE;

public class Swapchain extends DisposablePointer {

    private final LogicalDevice device;

    public Swapchain(final LogicalDevice device,
                     final Surface surface,
                     final int minImageCount,
                     final Format imageFormat,
                     final ColorSpace imageColorSpace,
                     final Extent2D imageExtent,
                     final SurfaceTransform preTransform,
                     final PresentMode presentMode,
                     final QueueFamily graphicsQueueFamily,
                     final QueueFamily presentationQueueFamily) {
        super(createSwapchain(device, surface, minImageCount, imageFormat, imageColorSpace, imageExtent, preTransform,
                presentMode, graphicsQueueFamily, presentationQueueFamily));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroySwapchainKHR(device.unwrap(), address, null);
    }

    public int getImageCount() {
        final int result;

        final IntBuffer imageCount = memAllocInt(1);

        try {
            VulkanFunction.execute(() -> vkGetSwapchainImagesKHR(device.unwrap(), getAddress(), imageCount, null));

            result = imageCount.get(0);
        } finally {
            memFree(imageCount);
        }

        return result;
    }

    public List<Image> getImages() {
        final List<Image> result;

        final int imageCount = getImageCount();

        if (imageCount > 0) {
            final IntBuffer imageCountBuffer = memAllocInt(1);
            imageCountBuffer.put(imageCount).flip();

            final LongBuffer images = memAllocLong(imageCount);

            try {
                VulkanFunction.execute(() -> vkGetSwapchainImagesKHR(device.unwrap(), getAddress(), imageCountBuffer,
                        images));

                result = IntStream.range(0, imageCount)
                        .mapToLong(images::get)
                        .mapToObj(SwapchainImage::new)
                        .collect(Collectors.toList());
            } finally {
                memFree(imageCountBuffer);
                memFree(images);
            }
        } else {
            result = Collections.emptyList();
        }

        return result;
    }

    public int acquireNextImageIndex(final Semaphore semaphore,
                                     final Fence fence) {
        synchronized (this) {
            final int result;

            final IntBuffer nextImageIndex = memAllocInt(1);

            final long semaphoreHandle = Optional.ofNullable(semaphore)
                    .map(Pointer::getAddress)
                    .orElse(VK_NULL_HANDLE);

            final long fenceHandle = Optional.ofNullable(fence)
                    .map(Pointer::getAddress)
                    .orElse(VK_NULL_HANDLE);

            try {
                VulkanFunction.execute(() -> vkAcquireNextImageKHR(device.unwrap(), getAddress(), Long.MAX_VALUE,
                        semaphoreHandle, fenceHandle, nextImageIndex));

                result = nextImageIndex.get(0);
            } finally {
                memFree(nextImageIndex);
            }

            return result;
        }
    }

    private static long createSwapchain(final LogicalDevice device,
                                        final Surface surface,
                                        final int minImageCount,
                                        final Format imageFormat,
                                        final ColorSpace imageColorSpace,
                                        final Extent2D imageExtent,
                                        final SurfaceTransform preTransform,
                                        final PresentMode presentMode,
                                        final QueueFamily graphicsQueueFamily,
                                        final QueueFamily presentationQueueFamily) {
        final long result;

        final LongBuffer handleBuffer = memAllocLong(1);

        final IntBuffer queueFamilyIndices = memAllocInt(2);
        queueFamilyIndices.put(graphicsQueueFamily.getIndex())
                .put(presentationQueueFamily.getIndex())
                .flip();

        final VkSwapchainCreateInfoKHR swapchainCreateInfo = VkSwapchainCreateInfoKHR.calloc()
                .sType(VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR)
                .surface(surface.getAddress())
                .minImageCount(minImageCount)
                .imageFormat(imageFormat.getValue())
                .imageColorSpace(imageColorSpace.getValue())
                .imageArrayLayers(1)
                .imageUsage(VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT)
                .preTransform(preTransform.getBitValue())
                .compositeAlpha(VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR)
                .presentMode(presentMode.getValue())
                .clipped(true)
                .oldSwapchain(VK_NULL_HANDLE);

        swapchainCreateInfo.imageExtent()
                .width(imageExtent.getWidth())
                .height(imageExtent.getHeight());

        if (!graphicsQueueFamily.equals(presentationQueueFamily)) {
            swapchainCreateInfo.imageSharingMode(VK_SHARING_MODE_CONCURRENT)
                    .pQueueFamilyIndices(queueFamilyIndices);
        } else {
            swapchainCreateInfo.imageSharingMode(VK_SHARING_MODE_EXCLUSIVE)
                    .pQueueFamilyIndices(null);
        }

        try {
            VulkanFunction.execute(() -> vkCreateSwapchainKHR(device.unwrap(), swapchainCreateInfo, null,
                    handleBuffer));

            result = handleBuffer.get(0);
        } finally {
            memFree(handleBuffer);
            memFree(queueFamilyIndices);

            swapchainCreateInfo.free();
        }

        return result;
    }
}
