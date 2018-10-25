package com.justindriggers.glfw;

import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.instance.VulkanInstance;
import com.justindriggers.vulkan.surface.Surface;

import java.nio.LongBuffer;

import static org.lwjgl.glfw.GLFWVulkan.glfwCreateWindowSurface;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;

public class GLFWSurface extends Surface {

    GLFWSurface(final VulkanInstance vulkanInstance,
                final long windowHandle) {
        super(vulkanInstance, createSurface(vulkanInstance, windowHandle));
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
