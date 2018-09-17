package com.justindriggers.vulkan.devices.logical;

import com.justindriggers.vulkan.models.pointers.DisposablePointer;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.memByteBuffer;
import static org.lwjgl.vulkan.VK10.vkUnmapMemory;

public class MappedDeviceMemory extends DisposablePointer {

    private final LogicalDevice device;
    private final DeviceMemory deviceMemory;

    MappedDeviceMemory(final LogicalDevice device,
                       final DeviceMemory deviceMemory,
                       final long address) {
        super(address);
        this.device = device;
        this.deviceMemory = deviceMemory;
    }

    @Override
    protected void dispose(final long address) {
        vkUnmapMemory(device.unwrap(), deviceMemory.getAddress());
        deviceMemory.unmap();
    }

    public void write(final byte[] data, final long offset) {
        final ByteBuffer buffer = memByteBuffer(getAddress() + offset, data.length);
        buffer.put(data);
    }
}
