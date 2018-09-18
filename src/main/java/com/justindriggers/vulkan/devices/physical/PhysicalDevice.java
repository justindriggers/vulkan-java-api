package com.justindriggers.vulkan.devices.physical;

import com.justindriggers.vulkan.devices.physical.models.MemoryHeap;
import com.justindriggers.vulkan.devices.physical.models.MemoryProperty;
import com.justindriggers.vulkan.devices.physical.models.MemoryType;
import com.justindriggers.vulkan.devices.physical.models.PhysicalDeviceProperties;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Extension;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.ReferencePointer;
import com.justindriggers.vulkan.queue.QueueCapability;
import com.justindriggers.vulkan.queue.QueueFamily;
import org.lwjgl.system.Pointer;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkMemoryType;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.vkEnumerateDeviceExtensionProperties;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceMemoryProperties;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceProperties;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceQueueFamilyProperties;

public class PhysicalDevice extends ReferencePointer<VkPhysicalDevice> {

    public PhysicalDevice(final VkPhysicalDevice vkPhysicalDevice) {
        super(vkPhysicalDevice, Pointer::address);
    }

    public PhysicalDeviceProperties getProperties() {
        final PhysicalDeviceProperties result;

        final VkPhysicalDeviceProperties physicalDeviceProperties = VkPhysicalDeviceProperties.calloc();

        try {
            vkGetPhysicalDeviceProperties(unwrap(), physicalDeviceProperties);

            result = new PhysicalDeviceProperties(physicalDeviceProperties);
        } finally {
            physicalDeviceProperties.free();
        }

        return result;
    }

    public int getExtensionCount() {
        final int result;

        final IntBuffer extensionCount = memAllocInt(1);

        try {
            VulkanFunction.execute(() -> vkEnumerateDeviceExtensionProperties(unwrap(), (CharSequence) null,
                    extensionCount, null));

            result = extensionCount.get(0);
        } finally {
            memFree(extensionCount);
        }

        return result;
    }

    public Set<Extension> getExtensions() {
        final Set<Extension> result;

        final int extensionCount = getExtensionCount();

        if (extensionCount > 0) {
            final IntBuffer extensionCountBuffer = memAllocInt(1);
            extensionCountBuffer.put(extensionCount).flip();

            final VkExtensionProperties.Buffer extensionProperties = VkExtensionProperties.calloc(extensionCount);

            try {
                VulkanFunction.execute(() -> vkEnumerateDeviceExtensionProperties(unwrap(), (CharSequence) null,
                        extensionCountBuffer, extensionProperties));

                result = IntStream.range(0, extensionCount)
                        .mapToObj(extensionProperties::get)
                        .map(Extension::new)
                        .collect(Collectors.toSet());
            } finally {
                memFree(extensionCountBuffer);

                extensionProperties.free();
            }
        } else {
            result = Collections.emptySet();
        }

        return result;
    }

    public int getQueueFamilyCount() {
        final int result;

        final IntBuffer queueFamilyCount = memAllocInt(1);

        try {
            vkGetPhysicalDeviceQueueFamilyProperties(unwrap(), queueFamilyCount, null);

            result = queueFamilyCount.get(0);
        } finally {
            memFree(queueFamilyCount);
        }

        return result;
    }

    public Set<QueueFamily> getQueueFamilies() {
        final Set<QueueFamily> result;

        final int queueFamilyCount = getQueueFamilyCount();

        if (queueFamilyCount > 0) {
            final IntBuffer queueFamilyCountBuffer = memAllocInt(1);
            queueFamilyCountBuffer.put(queueFamilyCount).flip();

            final VkQueueFamilyProperties.Buffer queueFamilyProperties = VkQueueFamilyProperties.calloc(queueFamilyCount);

            try {
                vkGetPhysicalDeviceQueueFamilyProperties(unwrap(), queueFamilyCountBuffer, queueFamilyProperties);

                result = IntStream.range(0, queueFamilyCount)
                        .mapToObj(queueFamilyIndex -> {
                            final VkQueueFamilyProperties current = queueFamilyProperties.get(queueFamilyIndex);

                            final Set<QueueCapability> capabilities = Maskable.fromBitMask(current.queueFlags(),
                                    QueueCapability.class);

                            return new QueueFamily(this, queueFamilyIndex, capabilities, current.queueCount());
                        })
                        .collect(Collectors.toSet());
            } finally {
                memFree(queueFamilyCountBuffer);

                queueFamilyProperties.free();
            }
        } else {
            result = Collections.emptySet();
        }

        return result;
    }

    public List<MemoryType> getMemoryTypes() {
        final List<MemoryType> result;

        final VkPhysicalDeviceMemoryProperties physicalDeviceMemoryProperties = VkPhysicalDeviceMemoryProperties.calloc();

        try {
            vkGetPhysicalDeviceMemoryProperties(unwrap(), physicalDeviceMemoryProperties);

            final List<MemoryHeap> heaps = IntStream.range(0, physicalDeviceMemoryProperties.memoryHeapCount())
                    .mapToObj(physicalDeviceMemoryProperties::memoryHeaps)
                    .map(MemoryHeap::new)
                    .collect(Collectors.toList());

            result = IntStream.range(0, physicalDeviceMemoryProperties.memoryTypeCount())
                    .mapToObj(i -> {
                        final VkMemoryType vkMemoryType = physicalDeviceMemoryProperties.memoryTypes(i);
                        final Set<MemoryProperty> properties = Maskable.fromBitMask(vkMemoryType.propertyFlags(),
                                MemoryProperty.class);
                        final MemoryHeap heap = heaps.get(vkMemoryType.heapIndex());
                        return new MemoryType(i, properties, heap);
                    })
                    .collect(Collectors.toList());
        } finally {
            physicalDeviceMemoryProperties.free();
        }

        return result;
    }
}
