package com.justindriggers.vulkan.devices.logical;

import com.justindriggers.vulkan.devices.physical.PhysicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.pointers.DisposableReferencePointer;
import com.justindriggers.vulkan.queue.Queue;
import com.justindriggers.vulkan.queue.QueueFamily;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkQueue;

import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkCreateDevice;
import static org.lwjgl.vulkan.VK10.vkDestroyDevice;
import static org.lwjgl.vulkan.VK10.vkDeviceWaitIdle;
import static org.lwjgl.vulkan.VK10.vkGetDeviceQueue;

public class LogicalDevice extends DisposableReferencePointer<VkDevice> {

    // TODO Support features
    public LogicalDevice(final PhysicalDevice physicalDevice,
                         final Map<QueueFamily, List<Float>> queueFamilyQueuePriorities,
                         final Set<String> extensions) {
        super(createLogicalDevice(physicalDevice, queueFamilyQueuePriorities, extensions), Pointer::address);
    }

    @Override
    protected void dispose(final VkDevice device, final long address) {
        vkDestroyDevice(device, null);
    }

    public Queue getQueue(final QueueFamily queueFamily, final int queueIndex) {
        final Queue result;

        final PointerBuffer queueBuffer = memAllocPointer(1);

        try {
            vkGetDeviceQueue(unwrap(), queueFamily.getIndex(), queueIndex, queueBuffer);

            final long queueHandle = queueBuffer.get(0);
            final VkQueue queue = new VkQueue(queueHandle, unwrap());

            result = new Queue(queue);
        } finally {
            memFree(queueBuffer);
        }

        return result;
    }

    public void waitIdle() {
        VulkanFunction.execute(() -> vkDeviceWaitIdle(unwrap()));
    }

    private static VkDevice createLogicalDevice(final PhysicalDevice physicalDevice,
                                                final Map<QueueFamily, List<Float>> queueFamilyQueuePriorities,
                                                final Set<String> extensions) {
        final VkDevice result;

        final VkDeviceQueueCreateInfo.Buffer queueCreateInfos = VkDeviceQueueCreateInfo.calloc(
                queueFamilyQueuePriorities.size());

        queueFamilyQueuePriorities.forEach((queueFamily, queuePriorities) -> {
            final FloatBuffer queuePrioritiesBuffer = memAllocFloat(queuePriorities.size());
            queuePriorities.forEach(queuePrioritiesBuffer::put);
            queuePrioritiesBuffer.flip();

            final VkDeviceQueueCreateInfo.Buffer queueCreateInfo = VkDeviceQueueCreateInfo.calloc(1)
                    .sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO)
                    .queueFamilyIndex(queueFamily.getIndex())
                    .pQueuePriorities(queuePrioritiesBuffer);

            queueCreateInfos.put(queueCreateInfo);
        });

        queueCreateInfos.flip();

        final PointerBuffer enabledExtensions = getEnabledExtensions(extensions);
        final PointerBuffer devicePointer = memAllocPointer(1);

        final VkDeviceCreateInfo deviceCreateInfo = VkDeviceCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
                .pNext(NULL)
                .pQueueCreateInfos(queueCreateInfos)
                .ppEnabledExtensionNames(enabledExtensions);

        try {
            final int createDeviceResult = vkCreateDevice(physicalDevice.unwrap(), deviceCreateInfo, null,
                    devicePointer);

            if (createDeviceResult != VK_SUCCESS) {
                throw new AssertionError("Failed to create device");
            }

            final long deviceHandle = devicePointer.get(0);

            result = new VkDevice(deviceHandle, physicalDevice.unwrap(), deviceCreateInfo);
        } finally {
            queueCreateInfos.free();

            IntStream.range(0, enabledExtensions.capacity())
                    .mapToObj(enabledExtensions::get)
                    .map(MemoryUtil::memByteBufferNT1)
                    .forEach(MemoryUtil::memFree);

            memFree(enabledExtensions);

            memFree(devicePointer);

            deviceCreateInfo.free();
        }

        return result;
    }

    private static PointerBuffer getEnabledExtensions(final Set<String> extensions) {
        final Set<String> extensionsSafe = Optional.ofNullable(extensions)
                .orElseGet(Collections::emptySet);

        final PointerBuffer result = memAllocPointer(extensionsSafe.size());

        extensionsSafe.stream()
                .map(MemoryUtil::memUTF8)
                .forEach(result::put);

        result.flip();

        return result;
    }
}
