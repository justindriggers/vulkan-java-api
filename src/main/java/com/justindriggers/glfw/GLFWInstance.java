package com.justindriggers.glfw;

import com.justindriggers.vulkan.instance.VulkanInstance;
import com.justindriggers.vulkan.surface.Surface;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;

public class GLFWInstance {

    public GLFWInstance() {
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }
    }

    public Set<String> getRequiredVulkanInstanceExtensions() {
        final PointerBuffer requiredExtensionsBuffer = glfwGetRequiredInstanceExtensions();

        return Optional.ofNullable(requiredExtensionsBuffer)
                .map(buffer -> IntStream.range(0, buffer.capacity())
                        .mapToObj(buffer::get)
                        .map(MemoryUtil::memUTF8)
                        .collect(Collectors.toSet()))
                .orElseGet(Collections::emptySet);
    }

    public Surface createWindowSurface(final VulkanInstance vulkanInstance, final long windowHandle) {
        return new GLFWSurface(vulkanInstance, windowHandle);
    }
}
