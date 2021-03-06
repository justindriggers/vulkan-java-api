package com.justindriggers.vulkan.devices.logical;

import com.justindriggers.utilities.StringUtils;
import com.justindriggers.vulkan.devices.physical.PhysicalDevice;
import com.justindriggers.vulkan.devices.physical.models.MemoryType;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.pointers.DisposableReferencePointer;
import com.justindriggers.vulkan.queue.Queue;
import com.justindriggers.vulkan.queue.QueueFamily;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkMemoryAllocateInfo;
import org.lwjgl.vulkan.VkQueue;

import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO;
import static org.lwjgl.vulkan.VK10.vkAllocateMemory;
import static org.lwjgl.vulkan.VK10.vkCreateDevice;
import static org.lwjgl.vulkan.VK10.vkDestroyDevice;
import static org.lwjgl.vulkan.VK10.vkDeviceWaitIdle;
import static org.lwjgl.vulkan.VK10.vkGetDeviceQueue;

public class LogicalDevice extends DisposableReferencePointer<VkDevice> {

    // TODO Support features
    public LogicalDevice(final PhysicalDevice physicalDevice,
                         final Map<QueueFamily, List<Float>> queueFamilyQueuePriorities,
                         final Set<String> extensions) {
        super(createLogicalDevice(physicalDevice, queueFamilyQueuePriorities, extensions),
                org.lwjgl.system.Pointer::address);
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

    public DeviceMemory allocateMemory(final MemoryType memoryType,
                                       final long size) {
        final DeviceMemory result;

        final LongBuffer memory = memAllocLong(1);

        final VkMemoryAllocateInfo memoryAllocateInfo = VkMemoryAllocateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO)
                .allocationSize(size)
                .memoryTypeIndex(memoryType.getIndex());

        try {
            VulkanFunction.execute(() -> vkAllocateMemory(unwrap(), memoryAllocateInfo, null, memory));

            result = new DeviceMemory(this, memoryType, memory.get(0), size);
        } finally {
            memFree(memory);

            memoryAllocateInfo.free();
        }

        return result;
    }

    public void freeMemory(final DeviceMemory deviceMemory) {
        deviceMemory.close();
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

        final PointerBuffer enabledExtensions = StringUtils.getPointerBufferFromStrings(extensions);
        final PointerBuffer devicePointer = memAllocPointer(1);

        final VkDeviceCreateInfo deviceCreateInfo = VkDeviceCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
                .pQueueCreateInfos(queueCreateInfos)
                .ppEnabledExtensionNames(enabledExtensions);

        try {
            VulkanFunction.execute(() -> vkCreateDevice(physicalDevice.unwrap(), deviceCreateInfo, null,
                    devicePointer));

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
}
