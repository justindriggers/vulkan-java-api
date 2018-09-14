package com.justindriggers.vulkan.queue;

import com.justindriggers.vulkan.devices.physical.PhysicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.surface.Surface;

import java.nio.IntBuffer;
import java.util.Objects;
import java.util.Set;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR;
import static org.lwjgl.vulkan.VK10.VK_FALSE;
import static org.lwjgl.vulkan.VK10.VK_TRUE;

public class QueueFamily {

    private final PhysicalDevice physicalDevice;
    private final int index;
    private final Set<QueueCapability> capabilities;
    private final int queueCount;

    public QueueFamily(final PhysicalDevice physicalDevice,
                       final int index,
                       final Set<QueueCapability> capabilities,
                       final int queueCount) {
        this.physicalDevice = physicalDevice;
        this.index = index;
        this.capabilities = capabilities;
        this.queueCount = queueCount;
    }

    public int getIndex() {
        return index;
    }

    public Set<QueueCapability> getCapabilities() {
        return capabilities;
    }

    public int getQueueCount() {
        return queueCount;
    }

    public boolean supportsSurfacePresentation(final Surface surface) {
        final boolean result;

        final IntBuffer supportsPresentation = memAllocInt(1);

        try {
            VulkanFunction.execute(() -> vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice.unwrap(),
                    index, surface.getAddress(), supportsPresentation));

            final int supportsPresentationResult = supportsPresentation.get(0);

            if (supportsPresentationResult == VK_TRUE) {
                result = true;
            } else if (supportsPresentationResult == VK_FALSE) {
                result = false;
            } else {
                throw new IllegalStateException("Unexpected result from vkGetPhysicalDeviceSurfaceSupportKHR: "
                        + supportsPresentationResult);
            }
        } finally {
            memFree(supportsPresentation);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QueueFamily that = (QueueFamily) o;

        return index == that.index
                && Objects.equals(physicalDevice, that.physicalDevice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                index,
                physicalDevice
        );
    }
}
