package com.justindriggers.vulkan.synchronize;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.synchronize.models.FenceCreationFlag;
import org.lwjgl.vulkan.VkFenceCreateInfo;

import java.nio.LongBuffer;
import java.util.Optional;
import java.util.Set;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_FENCE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateFence;
import static org.lwjgl.vulkan.VK10.vkDestroyFence;
import static org.lwjgl.vulkan.VK10.vkResetFences;
import static org.lwjgl.vulkan.VK10.vkWaitForFences;

public class Fence extends DisposablePointer {

    private final LogicalDevice device;

    public Fence(final LogicalDevice device,
                 final Set<FenceCreationFlag> fenceCreationFlags) {
        super(createFence(device, fenceCreationFlags));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyFence(device.unwrap(), address, null);
    }

    public void waitForSignal() {
        VulkanFunction.execute(() -> vkWaitForFences(device.unwrap(), getAddress(), true, Long.MAX_VALUE));
    }

    public void reset() {
        VulkanFunction.execute(() -> vkResetFences(device.unwrap(), getAddress()));
    }

    private static long createFence(final LogicalDevice device,
                                    final Set<FenceCreationFlag> fenceCreationFlags) {
        final long result;

        final LongBuffer fence = memAllocLong(1);

        final int flags = Optional.ofNullable(fenceCreationFlags)
                .map(Maskable::toBitMask)
                .orElse(0);

        final VkFenceCreateInfo fenceCreateInfo = VkFenceCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_FENCE_CREATE_INFO)
                .flags(flags);

        try {
            VulkanFunction.execute(() -> vkCreateFence(device.unwrap(), fenceCreateInfo, null, fence));

            result = fence.get(0);
        } finally {
            memFree(fence);

            fenceCreateInfo.free();
        }

        return result;
    }
}
