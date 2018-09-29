package com.justindriggers.vulkan.surface;

import com.justindriggers.vulkan.devices.physical.PhysicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.instance.VulkanInstance;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.surface.models.PresentMode;
import com.justindriggers.vulkan.surface.models.SurfaceFormat;
import com.justindriggers.vulkan.surface.models.capabilities.SurfaceCapabilities;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.glfw.GLFWVulkan.glfwCreateWindowSurface;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.KHRSurface.vkDestroySurfaceKHR;
import static org.lwjgl.vulkan.KHRSurface.vkGetPhysicalDeviceSurfaceCapabilitiesKHR;
import static org.lwjgl.vulkan.KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR;
import static org.lwjgl.vulkan.KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR;

public class Surface extends DisposablePointer {

    private final VulkanInstance vulkanInstance;

    public Surface(final VulkanInstance vulkanInstance,
                   final long windowHandle) {
        super(createSurface(vulkanInstance, windowHandle));
        this.vulkanInstance = vulkanInstance;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroySurfaceKHR(vulkanInstance.unwrap(), address, null);
    }

    public SurfaceCapabilities getCapabilities(final PhysicalDevice physicalDevice) {
        final SurfaceCapabilities result;

        final VkSurfaceCapabilitiesKHR vkSurfaceCapabilities = VkSurfaceCapabilitiesKHR.calloc();

        try {
            VulkanFunction.execute(() -> vkGetPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice.unwrap(),
                    getAddress(), vkSurfaceCapabilities));

            result = new SurfaceCapabilities(vkSurfaceCapabilities);
        } finally {
            vkSurfaceCapabilities.free();
        }

        return result;
    }

    public int getFormatCount(final PhysicalDevice physicalDevice) {
        final int result;

        final IntBuffer formatCount = memAllocInt(1);

        try {
            VulkanFunction.execute(() -> vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.unwrap(), getAddress(),
                    formatCount, null));

            result = formatCount.get(0);
        } finally {
            memFree(formatCount);
        }

        return result;
    }

    public List<SurfaceFormat> getFormats(final PhysicalDevice physicalDevice) {
        final List<SurfaceFormat> result;

        final int formatCount = getFormatCount(physicalDevice);

        if (formatCount > 0) {
            final IntBuffer formatCountBuffer = memAllocInt(1);
            formatCountBuffer.put(formatCount).flip();

            final VkSurfaceFormatKHR.Buffer formatBuffer = VkSurfaceFormatKHR.calloc(formatCount);

            try {
                VulkanFunction.execute(() -> vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.unwrap(), getAddress(),
                        formatCountBuffer, formatBuffer));

                result = IntStream.range(0, formatCount)
                        .mapToObj(formatBuffer::get)
                        .map(SurfaceFormat::new)
                        .collect(Collectors.toList());
            } finally {
                memFree(formatCountBuffer);

                formatBuffer.free();
            }
        } else {
            result = Collections.emptyList();
        }

        return result;
    }

    public int getPresentModeCount(final PhysicalDevice physicalDevice) {
        final int result;

        final IntBuffer presentModeCount = memAllocInt(1);

        try {
            VulkanFunction.execute(() -> vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice.unwrap(),
                    getAddress(), presentModeCount, null));

            result = presentModeCount.get(0);
        } finally {
            memFree(presentModeCount);
        }

        return result;
    }

    public Set<PresentMode> getPresentModes(final PhysicalDevice physicalDevice) {
        final Set<PresentMode> result;

        final int presentModeCount = getPresentModeCount(physicalDevice);

        if (presentModeCount > 0) {
            final IntBuffer presentModeCountBuffer = memAllocInt(1);
            presentModeCountBuffer.put(presentModeCount).flip();

            final IntBuffer presentModesBuffer = memAllocInt(presentModeCount);

            try {
                VulkanFunction.execute(() -> vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice.unwrap(),
                        getAddress(), presentModeCountBuffer, presentModesBuffer));

                result = IntStream.range(0, presentModeCount)
                        .map(presentModesBuffer::get)
                        .mapToObj(presentMode -> HasValue.getByValue(presentMode, PresentMode.class))
                        .collect(Collectors.toCollection(() -> EnumSet.noneOf(PresentMode.class)));
            } finally {
                memFree(presentModeCountBuffer);
                memFree(presentModesBuffer);
            }
        } else {
            result = Collections.emptySet();
        }

        return result;
    }

    private static long createSurface(final VulkanInstance vulkanInstance,
                                      final long windowHandle) {
        final long result;

        final LongBuffer surfaceHandle = memAllocLong(1);

        try {
            VulkanFunction.execute(() -> glfwCreateWindowSurface(vulkanInstance.unwrap(), windowHandle, null,
                    surfaceHandle));

            result = surfaceHandle.get(0);
        } finally {
            memFree(surfaceHandle);
        }

        return result;
    }
}
