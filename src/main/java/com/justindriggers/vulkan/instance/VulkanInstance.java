package com.justindriggers.vulkan.instance;

import com.justindriggers.utilities.StringUtils;
import com.justindriggers.vulkan.devices.physical.PhysicalDevice;
import com.justindriggers.vulkan.instance.models.ApplicationInfo;
import com.justindriggers.vulkan.instance.models.MessageSeverity;
import com.justindriggers.vulkan.instance.models.MessageType;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposableReferencePointer;
import org.lwjgl.PointerBuffer;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
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

    public VulkanInstance(final ApplicationInfo applicationInfo,
                          final Set<String> extensions,
                          final Set<String> validationLayers) {
        super(createVulkanInstance(applicationInfo, extensions, validationLayers), Pointer::address);
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

    private static VkInstance createVulkanInstance(final ApplicationInfo applicationInfo,
                                                   final Set<String> requestedExtensions,
                                                   final Set<String> validationLayers) {
        final VkInstance result;

        final PointerBuffer instancePointer = memAllocPointer(1);

        final PointerBuffer enabledExtensions = StringUtils.getPointerBufferFromStrings(requestedExtensions);
        final PointerBuffer enabledLayers = StringUtils.getPointerBufferFromStrings(validationLayers);

        final ByteBuffer applicationName = Optional.ofNullable(applicationInfo.getApplicationName())
                .map(MemoryUtil::memUTF8)
                .orElse(null);

        final ByteBuffer engineName = Optional.ofNullable(applicationInfo.getEngineName())
                .map(MemoryUtil::memUTF8)
                .orElse(null);

        final int apiVersion = Optional.ofNullable(applicationInfo.getApiVersion())
                .map(version -> VK_MAKE_VERSION(version.getMajor(), version.getMinor(), version.getPatch()))
                .orElse(0);

        final VkApplicationInfo vkApplicationInfo = VkApplicationInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                .pApplicationName(applicationName)
                .applicationVersion(applicationInfo.getApplicationVersion())
                .pEngineName(engineName)
                .engineVersion(applicationInfo.getEngineVersion())
                .apiVersion(apiVersion);

        final VkInstanceCreateInfo instanceCreateInfo = VkInstanceCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                .pApplicationInfo(vkApplicationInfo)
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

            memFree(applicationName);
            memFree(engineName);

            vkApplicationInfo.free();
            instanceCreateInfo.free();
        }

        return result;
    }
}
