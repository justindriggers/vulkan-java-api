package com.justindriggers.vulkan.synchronize;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateSemaphore;
import static org.lwjgl.vulkan.VK10.vkDestroySemaphore;

public class Semaphore extends DisposablePointer {

    private final LogicalDevice device;

    public Semaphore(final LogicalDevice device) {
        super(createSemaphore(device));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroySemaphore(device.unwrap(), address, null);
    }

    private static long createSemaphore(final LogicalDevice device) {
        final LongBuffer semaphore = memAllocLong(1);

        final VkSemaphoreCreateInfo semaphoreCreateInfo = VkSemaphoreCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO);

        try {
            VulkanFunction.execute(() -> vkCreateSemaphore(device.unwrap(), semaphoreCreateInfo, null, semaphore));

            return semaphore.get(0);
        } finally {
            semaphoreCreateInfo.free();
        }
    }
}
