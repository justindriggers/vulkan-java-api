package com.justindriggers.vulkan.queue;

import com.justindriggers.vulkan.command.CommandBuffer;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.Pointer;
import com.justindriggers.vulkan.models.pointers.ReferencePointer;
import com.justindriggers.vulkan.pipeline.models.PipelineStage;
import com.justindriggers.vulkan.swapchain.Swapchain;
import com.justindriggers.vulkan.synchronize.Fence;
import com.justindriggers.vulkan.synchronize.Semaphore;
import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.lwjgl.vulkan.VkQueue;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.KHRSwapchain.VK_STRUCTURE_TYPE_PRESENT_INFO_KHR;
import static org.lwjgl.vulkan.KHRSwapchain.vkQueuePresentKHR;
import static org.lwjgl.vulkan.VK10.VK_NULL_HANDLE;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_SUBMIT_INFO;
import static org.lwjgl.vulkan.VK10.vkQueueSubmit;
import static org.lwjgl.vulkan.VK10.vkQueueWaitIdle;

public class Queue extends ReferencePointer<VkQueue> {

    public Queue(final VkQueue vkQueue) {
        super(vkQueue, org.lwjgl.system.Pointer::address);
    }

    public void submit(final List<Semaphore> waitSemaphores,
                       final List<PipelineStage> waitDstStages,
                       final Set<CommandBuffer> commandBuffers,
                       final Set<Semaphore> signalSemaphores,
                       final Fence fence) {
        final int waitSemaphoresCount = Optional.ofNullable(waitSemaphores)
                .map(List::size)
                .orElse(0);

        final LongBuffer waitSemaphoreHandles;

        if (waitSemaphoresCount > 0) {
            waitSemaphoreHandles = memAllocLong(waitSemaphores.size());
            waitSemaphores.stream()
                    .map(Pointer::getAddress)
                    .forEach(waitSemaphoreHandles::put);
            waitSemaphoreHandles.flip();
        } else {
            waitSemaphoreHandles = null;
        }

        final IntBuffer dstStageMask;

        if (waitDstStages != null) {
            dstStageMask = memAllocInt(waitDstStages.size());
            waitDstStages.stream()
                    .map(Maskable::toBit)
                    .forEach(dstStageMask::put);
            dstStageMask.flip();
        } else {
            dstStageMask = null;
        }

        final PointerBuffer commandBufferPointers = memAllocPointer(commandBuffers.size());
        commandBuffers.stream()
                .map(Pointer::getAddress)
                .forEach(commandBufferPointers::put);
        commandBufferPointers.flip();

        final LongBuffer signalSemaphoreHandles;

        if (signalSemaphores != null) {
            signalSemaphoreHandles = memAllocLong(signalSemaphores.size());
            signalSemaphores.stream()
                    .map(Pointer::getAddress)
                    .forEach(signalSemaphoreHandles::put);
            signalSemaphoreHandles.flip();
        } else {
            signalSemaphoreHandles = null;
        }

        final long fenceHandle = Optional.ofNullable(fence)
                .map(Pointer::getAddress)
                .orElse(VK_NULL_HANDLE);

        final VkSubmitInfo submitInfo = VkSubmitInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_SUBMIT_INFO)
                .waitSemaphoreCount(waitSemaphoresCount)
                .pWaitSemaphores(waitSemaphoreHandles)
                .pWaitDstStageMask(dstStageMask)
                .pCommandBuffers(commandBufferPointers)
                .pSignalSemaphores(signalSemaphoreHandles);

        try {
            VulkanFunction.execute(() -> vkQueueSubmit(unwrap(), submitInfo, fenceHandle));
        } finally {
            memFree(waitSemaphoreHandles);
            memFree(dstStageMask);
            memFree(commandBufferPointers);
            memFree(signalSemaphoreHandles);

            submitInfo.free();
        }
    }

    public void present(final List<Swapchain> swapchains,
                        final List<Integer> imageIndices,
                        final Set<Semaphore> waitSemaphores) {
        final LongBuffer waitSemaphoreHandles = memAllocLong(waitSemaphores.size());

        waitSemaphores.stream()
                .map(Pointer::getAddress)
                .forEach(waitSemaphoreHandles::put);

        waitSemaphoreHandles.flip();

        final LongBuffer swapchainHandles = memAllocLong(swapchains.size());

        swapchains.stream()
                .map(Pointer::getAddress)
                .forEach(swapchainHandles::put);

        swapchainHandles.flip();

        final IntBuffer imageIndexBuffer = memAllocInt(imageIndices.size());
        imageIndices.forEach(imageIndexBuffer::put);
        imageIndexBuffer.flip();

        final VkPresentInfoKHR presentInfo = VkPresentInfoKHR.calloc()
                .sType(VK_STRUCTURE_TYPE_PRESENT_INFO_KHR)
                .pWaitSemaphores(waitSemaphoreHandles)
                .swapchainCount(swapchains.size())
                .pSwapchains(swapchainHandles)
                .pImageIndices(imageIndexBuffer);

        try {
            VulkanFunction.execute(() -> vkQueuePresentKHR(unwrap(), presentInfo));
        } finally {
            memFree(waitSemaphoreHandles);
            memFree(swapchainHandles);
            memFree(imageIndexBuffer);

            presentInfo.free();
        }
    }

    public void waitIdle() {
        VulkanFunction.execute(() -> vkQueueWaitIdle(unwrap()));
    }
}
