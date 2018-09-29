package com.justindriggers.vulkan.command;

import com.justindriggers.vulkan.command.commands.Command;
import com.justindriggers.vulkan.command.models.CommandBufferUsage;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposableReferencePointer;
import org.lwjgl.system.Pointer;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferBeginInfo;

import static org.lwjgl.vulkan.VK10.VK_COMMAND_BUFFER_RESET_RELEASE_RESOURCES_BIT;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO;
import static org.lwjgl.vulkan.VK10.vkBeginCommandBuffer;
import static org.lwjgl.vulkan.VK10.vkEndCommandBuffer;
import static org.lwjgl.vulkan.VK10.vkResetCommandBuffer;

public class CommandBuffer extends DisposableReferencePointer<VkCommandBuffer> {

    CommandBuffer(final VkCommandBuffer vkCommandBuffer) {
        super(vkCommandBuffer, Pointer::address);
    }

    @Override
    protected void dispose(final VkCommandBuffer commandBuffer, final long address) {
        // Nothing to do, but disposal ensures that nothing can reference the underlying pointer or value
    }

    public void reset() {
        VulkanFunction.execute(() -> vkResetCommandBuffer(unwrap(), VK_COMMAND_BUFFER_RESET_RELEASE_RESOURCES_BIT));
    }

    public void begin(final CommandBufferUsage... usages) {
        final VkCommandBufferBeginInfo commandBufferBeginInfo = VkCommandBufferBeginInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO)
                .flags(Maskable.toBitMask(usages));

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
