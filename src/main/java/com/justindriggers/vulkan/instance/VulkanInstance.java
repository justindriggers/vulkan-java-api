package com.justindriggers.vulkan.instance;

import com.justindriggers.vulkan.devices.physical.PhysicalDevice;
import com.justindriggers.vulkan.instance.models.MessageSeverity;
import com.justindriggers.vulkan.instance.models.MessageType;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposableReferencePointer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkDebugUtilsMessengerCallbackEXTI;
import org.lwjgl.vulkan.VkDebugUtilsMessengerCreateInfoEXT;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkPhysicalDevice;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.system.MemoryUtil.memUTF8;
import static org.lwjgl.vulkan.EXTDebugUtils.VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT;
import static org.lwjgl.vulkan.EXTDebugUtils.vkCreateDebugUtilsMessengerEXT;
import static org.lwjgl.vulkan.EXTDebugUtils.vkDestroyDebugUtilsMessengerEXT;
import static org.lwjgl.vulkan.VK10.VK_MAKE_VERSION;
import static org.lwjgl.vulkan.VK10.VK_NULL_HANDLE;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateInstance;
import static org.lwjgl.vulkan.VK10.vkDestroyInstance;
import static org.lwjgl.vulkan.VK10.vkEnumeratePhysicalDevices;

public class VulkanInstance extends DisposableReferencePointer<VkInstance> {

    private final AtomicLong debuggerHandle = new AtomicLong(VK_NULL_HANDLE);

    public VulkanInstance(final Set<String> requestedExtensions,
                          final Set<String> validationLayers) {
        super(createVulkanInstance(requestedExtensions, validationLayers), Pointer::address);
    }

    @Override
    protected void dispose(final VkInstance instance, final long address) {
        Optional.of(debuggerHandle)
                .map(handle -> handle.getAndSet(VK_NULL_HANDLE))
                .filter(handle -> handle != VK_NULL_HANDLE)
                .ifPresent(handle -> vkDestroyDebugUtilsMessengerEXT(instance, handle, null));

        vkDestroyInstance(instance, null);
    }

    public void enableDebugging(final Set<MessageSeverity> messageSeverities,
                                final Set<MessageType> messageTypes,
                                final VkDebugUtilsMessengerCallbackEXTI callback) {
        final LongBuffer handleBuffer = memAllocLong(1);

        final VkDebugUtilsMessengerCreateInfoEXT debugUtilsMessengerCreateInfo = VkDebugUtilsMessengerCreateInfoEXT.calloc()
                .sType(VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT)
                .messageSeverity(Maskable.toBitMask(messageSeverities))
                .messageType(Maskable.toBitMask(messageTypes))
                .pfnUserCallback(callback)
                .pUserData(NULL);

        try {
            VulkanFunction.execute(() -> vkCreateDebugUtilsMessengerEXT(unwrap(), debugUtilsMessengerCreateInfo, null,
                    handleBuffer));

            debuggerHandle.set(handleBuffer.get(0));
        } finally {
            memFree(handleBuffer);

            debugUtilsMessengerCreateInfo.free();
        }
    }

    public int getPhysicalDeviceCount() {
        final int result;

        final IntBuffer physicalDeviceCount = memAllocInt(1);

        try {
            VulkanFunction.execute(() -> vkEnumeratePhysicalDevices(unwrap(), physicalDeviceCount, null));

            result = physicalDeviceCount.get(0);
        } finally {
            memFree(physicalDeviceCount);
        }

        return result;
    }

    public List<PhysicalDevice> getPhysicalDevices() {
        final List<PhysicalDevice> result;

        final int physicalDeviceCount = getPhysicalDeviceCount();

        if (physicalDeviceCount > 0) {
            final PointerBuffer physicalDevices = memAllocPointer(physicalDeviceCount);

            final IntBuffer physicalDeviceCountBuffer = memAllocInt(1);
            physicalDeviceCountBuffer.put(physicalDeviceCount).flip();

            try {
                VulkanFunction.execute(() -> vkEnumeratePhysicalDevices(unwrap(), physicalDeviceCountBuffer,
                        physicalDevices));

                result = IntStream.range(0, physicalDeviceCount)
                        .mapToObj(physicalDevices::get)
                        .map(handle -> new VkPhysicalDevice(handle, unwrap()))
                        .map(PhysicalDevice::new)
                        .collect(Collectors.toList());
            } finally {
                memFree(physicalDevices);
            }
        } else {
            result = Collections.emptyList();
        }

        return result;
    }

    private static VkInstance createVulkanInstance(final Set<String> requestedExtensions,
                                                   final Set<String> validationLayers) {
        final VkInstance result;

        final PointerBuffer instancePointer = memAllocPointer(1);

        final PointerBuffer enabledExtensions = getEnabledExtensions(requestedExtensions);
        final PointerBuffer enabledLayers = getEnabledLayers(validationLayers);

        final ByteBuffer applicationName = memUTF8("GLFW Vulkan Demo");
        final ByteBuffer engineName = memUTF8("Logical");

        final VkApplicationInfo applicationInfo = VkApplicationInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                .pApplicationName(applicationName)
                .pEngineName(engineName)
                .apiVersion(VK_MAKE_VERSION(1, 1, 0));

        final VkInstanceCreateInfo instanceCreateInfo = VkInstanceCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                .pApplicationInfo(applicationInfo)
                .ppEnabledExtensionNames(enabledExtensions)
                .ppEnabledLayerNames(enabledLayers);

        try {
            VulkanFunction.execute(() -> vkCreateInstance(instanceCreateInfo, null, instancePointer));

            final long instanceHandle = instancePointer.get(0);

            result = new VkInstance(instanceHandle, instanceCreateInfo);
        } finally {
            memFree(instancePointer);

            IntStream.range(0, enabledExtensions.capacity())
                    .mapToObj(enabledExtensions::get)
                    .map(MemoryUtil::memByteBufferNT1)
                    .forEach(MemoryUtil::memFree);

            memFree(enabledExtensions);

            IntStream.range(0, enabledLayers.capacity())
                    .mapToObj(enabledLayers::get)
                    .map(MemoryUtil::memByteBufferNT1)
                    .forEach(MemoryUtil::memFree);

            memFree(enabledLayers);
        }

        return result;
    }

    /**
     * Safely checks the Set of requested extensions, and combines them with the Set of required extensions returned by
     * {@link GLFWVulkan#glfwGetRequiredInstanceExtensions()}.
     *
     * According to the documentation for {@link GLFWVulkan#glfwGetRequiredInstanceExtensions()}:
     *
     * "You should check if any extensions you wish to enable are already in the returned array, as it is an error to
     * specify an extension more than once in the {@code VkInstanceCreateInfo} struct."
     *
     * Therefore, the array of required extensions is interpreted, and then added to the Set of requested extensions
     * (thus removing possible duplicates) prior to converting the Strings to ByteBuffers, and eventually returning a
     * PointerBuffer containing one instance of each unique String.
     *
     * According to the <a href="https://www.khronos.org/registry/vulkan/specs/1.1/styleguide.html#extensions-naming-conventions-name-strings">Vulkan style guide, section 3.3.1. Version, Extension and Layer Name Strings</a>,
     *
     * "The <name> portion of version, extension and layer names is a concise name describing its purpose or
     * functionality. The underscore (_) character is used as a delimiter between words. Every alphabetic character of
     * the name must be in lower case."
     *
     * It is assumed that requested extension names follow proper casing standards, such that two extensions with the
     * same alphabetical spelling, but different casing will be considered two separate extensions.
     *
     * @param requestedExtensions A nullable Set of requested extension Strings
     * @return a PointerBuffer containing an array of pointers to all requested and required extensions. The
     * PointerBuffer and the Strings that its contents point to must be disposed of manually.
     */
    private static PointerBuffer getEnabledExtensions(final Set<String> requestedExtensions) {
        final Set<String> requestedExtensionsSafe = Optional.ofNullable(requestedExtensions)
                .orElseGet(Collections::emptySet);

        final Set<String> allExtensions = new HashSet<>(requestedExtensionsSafe);

        final PointerBuffer requiredExtensionsBuffer = Optional.ofNullable(glfwGetRequiredInstanceExtensions())
                .orElseThrow(() -> new IllegalStateException("Unable to retrieve required Vulkan extensions"));

        IntStream.range(0, requiredExtensionsBuffer.limit())
                .mapToObj(requiredExtensionsBuffer::get)
                .map(MemoryUtil::memUTF8)
                .forEach(allExtensions::add);

        final PointerBuffer result = memAllocPointer(allExtensions.size());

        allExtensions.stream()
                .map(MemoryUtil::memUTF8)
                .forEach(result::put);

        result.flip();

        return result;
    }

    /**
     * Safely converts the Set of validation layer Strings into a PointerBuffer containing an array of pointers to the
     * validation layer Strings.
     *
     * @param validationLayers A nullable Set of validation layer Strings
     * @return a PointerBuffer containing an array of pointers to all validation layers. The PointerBuffer and the
     * Strings that its contents point to must be disposed of manually.
     */
    private static PointerBuffer getEnabledLayers(final Set<String> validationLayers) {
        final Set<String> validationLayersSafe = Optional.ofNullable(validationLayers)
                .orElseGet(Collections::emptySet);

        final PointerBuffer result = memAllocPointer(validationLayersSafe.size());

        validationLayersSafe.stream()
                .map(MemoryUtil::memUTF8)
                .forEach(result::put);

        result.flip();

        return result;
    }
}
