package com.justindriggers.vulkan.devices.logical;

import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import org.lwjgl.PointerBuffer;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.vkFreeMemory;
import static org.lwjgl.vulkan.VK10.vkMapMemory;

public class DeviceMemory extends DisposablePointer {

    private final AtomicReference<MappedDeviceMemory> mappedDeviceMemory = new AtomicReference<>(null);

    private final LogicalDevice device;

    DeviceMemory(final LogicalDevice device,
                 final long address) {
        super(address);
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        Optional.ofNullable(mappedDeviceMemory.getAndSet(null))
                .ifPresent(MappedDeviceMemory::close);

        vkFreeMemory(device.unwrap(), address, null);
    }

    public MappedDeviceMemory map(final long size, final long offset) {
        return mappedDeviceMemory.updateAndGet(currentlyMappedDeviceMemory -> {
            if (currentlyMappedDeviceMemory != null) {
                throw new IllegalStateException("Device memory is already mapped");
            }

            final MappedDeviceMemory result;

            final PointerBuffer data = memAllocPointer(1);

            try {
                VulkanFunction.execute(() -> vkMapMemory(device.unwrap(), getAddress(), offset, size, 0, data));

                result = new MappedDeviceMemory(device, this, data.get(0));
            } finally {
                memFree(data);
            }

            return result;
        });
    }

    void unmap() {
        mappedDeviceMemory.set(null);
    }
}
