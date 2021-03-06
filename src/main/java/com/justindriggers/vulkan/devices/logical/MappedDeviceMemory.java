package com.justindriggers.vulkan.devices.logical;

import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import org.lwjgl.vulkan.VkMappedMemoryRange;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.memByteBuffer;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_MAPPED_MEMORY_RANGE;
import static org.lwjgl.vulkan.VK10.VK_WHOLE_SIZE;
import static org.lwjgl.vulkan.VK10.vkFlushMappedMemoryRanges;
import static org.lwjgl.vulkan.VK10.vkUnmapMemory;

public class MappedDeviceMemory extends DisposablePointer {

    private final LogicalDevice device;
    private final DeviceMemory deviceMemory;
    private final boolean isCoherent;

    MappedDeviceMemory(final LogicalDevice device,
                       final DeviceMemory deviceMemory,
                       final long address,
                       final boolean isCoherent) {
        super(address);
        this.device = device;
        this.deviceMemory = deviceMemory;
        this.isCoherent = isCoherent;
    }

    @Override
    protected void dispose(final long address) {
        vkUnmapMemory(device.unwrap(), deviceMemory.getAddress());
        deviceMemory.unmap();
    }

    public ByteBuffer getBuffer() {
        return memByteBuffer(getAddress(), Math.toIntExact(deviceMemory.getSize()));
    }

    public void flush() {
        if (!isCoherent) {
            final VkMappedMemoryRange vkMappedMemoryRange = VkMappedMemoryRange.calloc()
                    .sType(VK_STRUCTURE_TYPE_MAPPED_MEMORY_RANGE)
                    .memory(deviceMemory.getAddress())
                    .offset(0L)
                    .size(VK_WHOLE_SIZE);

            try {
                VulkanFunction.execute(() -> vkFlushMappedMemoryRanges(device.unwrap(), vkMappedMemoryRange));
            } finally {
                vkMappedMemoryRange.free();
            }
        }
    }
}
