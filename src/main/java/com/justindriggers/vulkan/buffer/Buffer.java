package com.justindriggers.vulkan.buffer;

import com.justindriggers.vulkan.buffer.models.BufferUsage;
import com.justindriggers.vulkan.buffer.models.MemoryRequirements;
import com.justindriggers.vulkan.devices.logical.DeviceMemory;
import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;

import java.nio.LongBuffer;
import java.util.Set;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_SHARING_MODE_EXCLUSIVE;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkBindBufferMemory;
import static org.lwjgl.vulkan.VK10.vkCreateBuffer;
import static org.lwjgl.vulkan.VK10.vkDestroyBuffer;
import static org.lwjgl.vulkan.VK10.vkGetBufferMemoryRequirements;

public class Buffer extends DisposablePointer {

    private final LogicalDevice device;
    private final long size;

    public Buffer(final LogicalDevice device,
                  final long size,
                  final Set<BufferUsage> usages) {
        super(createBuffer(device, size, usages));
        this.device = device;
        this.size = size;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyBuffer(device.unwrap(), address, null);
    }

    public MemoryRequirements getMemoryRequirements() {
        final MemoryRequirements result;

        final VkMemoryRequirements memoryRequirements = VkMemoryRequirements.calloc();

        try {
            vkGetBufferMemoryRequirements(device.unwrap(), getAddress(), memoryRequirements);

            result = new MemoryRequirements(memoryRequirements);
        } finally {
            memoryRequirements.free();
        }

        return result;
    }

    public void bindMemory(final DeviceMemory deviceMemory) {
        VulkanFunction.execute(() -> vkBindBufferMemory(device.unwrap(), getAddress(), deviceMemory.getAddress(), 0L));
    }

    public long getSize() {
        return size;
    }

    private static long createBuffer(final LogicalDevice device,
                                     final long size,
                                     final Set<BufferUsage> usages) {
        final long result;

        final LongBuffer buffer = memAllocLong(1);

        final VkBufferCreateInfo bufferCreateInfo = VkBufferCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO)
                .size(size)
                .usage(Maskable.toBitMask(usages))
                .sharingMode(VK_SHARING_MODE_EXCLUSIVE);

        try {
            VulkanFunction.execute(() -> vkCreateBuffer(device.unwrap(), bufferCreateInfo, null, buffer));

            result = buffer.get(0);
        } finally {
            memFree(buffer);

            bufferCreateInfo.free();
        }

        return result;
    }
}
