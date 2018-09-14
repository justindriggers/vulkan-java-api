package com.justindriggers.vulkan.pipeline;

import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.pointers.DisposableReferencePointer;
import com.justindriggers.vulkan.pipeline.commands.Command;
import org.lwjgl.system.Pointer;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferBeginInfo;

import static org.lwjgl.vulkan.VK10.VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO;
import static org.lwjgl.vulkan.VK10.vkBeginCommandBuffer;
import static org.lwjgl.vulkan.VK10.vkEndCommandBuffer;

public class CommandBuffer extends DisposableReferencePointer<VkCommandBuffer> {

    CommandBuffer(final VkCommandBuffer vkCommandBuffer) {
        super(vkCommandBuffer, Pointer::address);
    }

    @Override
    protected void dispose(final VkCommandBuffer commandBuffer, final long address) {
        // Nothing to do, but disposal ensures that nothing can reference the underlying pointer or value
    }

    public void begin() {
        final VkCommandBufferBeginInfo commandBufferBeginInfo = VkCommandBufferBeginInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO)
                .flags(VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT);

        try {
            VulkanFunction.execute(() -> vkBeginCommandBuffer(unwrap(), commandBufferBeginInfo));
        } finally {
            commandBufferBeginInfo.free();
        }
    }

    public void end() {
        VulkanFunction.execute(() -> vkEndCommandBuffer(unwrap()));
    }

    public void submit(final Command command) {
        command.execute(this);
    }
}
