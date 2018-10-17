package com.justindriggers.vulkan.command;

import com.justindriggers.vulkan.command.models.CommandBufferLevel;
import com.justindriggers.vulkan.command.models.CommandPoolCreateFlag;
import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.queue.QueueFamily;
import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;

import java.nio.LongBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_COMMAND_BUFFER_LEVEL_PRIMARY;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkAllocateCommandBuffers;
import static org.lwjgl.vulkan.VK10.vkCreateCommandPool;
import static org.lwjgl.vulkan.VK10.vkDestroyCommandPool;
import static org.lwjgl.vulkan.VK10.vkFreeCommandBuffers;

public class CommandPool extends DisposablePointer {

    private final LogicalDevice device;

    public CommandPool(final LogicalDevice device,
                       final QueueFamily queueFamily,
                       final Set<CommandPoolCreateFlag> flags) {
        super(createCommandPool(device, queueFamily, flags));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyCommandPool(device.unwrap(), address, null);
    }

    public List<CommandBuffer> createCommandBuffers(final CommandBufferLevel commandBufferLevel, final int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be at least 1 when creating command buffers");
        }

        final List<CommandBuffer> result;

        final PointerBuffer commandBuffers = memAllocPointer(count);

        final VkCommandBufferAllocateInfo commandBufferAllocateInfo = VkCommandBufferAllocateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO)
                .commandPool(getAddress())
                .level(HasValue.getValue(commandBufferLevel))
                .commandBufferCount(count);

        try {
            VulkanFunction.execute(() -> vkAllocateCommandBuffers(device.unwrap(), commandBufferAllocateInfo,
                    commandBuffers));

            result = IntStream.range(0, count)
                    .mapToLong(commandBuffers::get)
                    .mapToObj(commandBuffer -> new VkCommandBuffer(commandBuffer, device.unwrap()))
                    .map(CommandBuffer::new)
                    .collect(Collectors.toList());
        } finally {
            commandBufferAllocateInfo.free();

            memFree(commandBuffers);
        }

        return result;
    }

    public void destroyCommandBuffers(final Collection<CommandBuffer> commandBuffers) {
        Objects.requireNonNull(commandBuffers);

        if (!commandBuffers.isEmpty()) {
            final PointerBuffer commandBufferPointers = memAllocPointer(commandBuffers.size());
            commandBuffers.forEach(commandBuffer -> commandBufferPointers.put(commandBuffer.unwrap().address()));
            commandBufferPointers.flip();

            try {
                vkFreeCommandBuffers(device.unwrap(), getAddress(), commandBufferPointers);
                commandBuffers.forEach(CommandBuffer::close);
            } finally {
                memFree(commandBufferPointers);
            }
        }
    }

    private static long createCommandPool(final LogicalDevice device,
                                          final QueueFamily queueFamily,
                                          final Set<CommandPoolCreateFlag> flags) {
        final long result;

        final LongBuffer commandPool = memAllocLong(1);

        final VkCommandPoolCreateInfo commandPoolCreateInfo = VkCommandPoolCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO)
                .queueFamilyIndex(queueFamily.getIndex())
                .flags(Maskable.toBitMask(flags));

        try {
            VulkanFunction.execute(() -> vkCreateCommandPool(device.unwrap(), commandPoolCreateInfo, null,
                    commandPool));

            result = commandPool.get(0);
        } finally {
            memFree(commandPool);

            commandPoolCreateInfo.free();
        }

        return result;
    }
}
