package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.command.CommandBuffer;
import com.justindriggers.vulkan.command.commands.models.BufferMemoryBarrier;
import com.justindriggers.vulkan.command.commands.models.ImageMemoryBarrier;
import com.justindriggers.vulkan.command.commands.models.MemoryBarrier;
import com.justindriggers.vulkan.models.Dependency;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.pipeline.models.PipelineStage;
import com.justindriggers.vulkan.queue.QueueFamily;
import org.lwjgl.vulkan.VkBufferMemoryBarrier;
import org.lwjgl.vulkan.VkImageMemoryBarrier;
import org.lwjgl.vulkan.VkImageSubresourceRange;
import org.lwjgl.vulkan.VkMemoryBarrier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.lwjgl.vulkan.VK10.VK_QUEUE_FAMILY_IGNORED;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_BUFFER_MEMORY_BARRIER;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_MEMORY_BARRIER;
import static org.lwjgl.vulkan.VK10.vkCmdPipelineBarrier;

public class PipelineBarrierCommand implements Command {

    private final Set<PipelineStage> sourceStages;
    private final Set<PipelineStage> destinationStages;
    private final Set<Dependency> dependencies;
    private final List<MemoryBarrier> memoryBarriers;
    private final List<BufferMemoryBarrier> bufferMemoryBarriers;
    private final List<ImageMemoryBarrier> imageMemoryBarriers;

    public PipelineBarrierCommand(final Set<PipelineStage> sourceStages,
                                  final Set<PipelineStage> destinationStages,
                                  final Set<Dependency> dependencies,
                                  final List<MemoryBarrier> memoryBarriers,
                                  final List<BufferMemoryBarrier> bufferMemoryBarriers,
                                  final List<ImageMemoryBarrier> imageMemoryBarriers) {
        this.sourceStages = sourceStages;
        this.destinationStages = destinationStages;
        this.dependencies = dependencies;
        this.memoryBarriers = memoryBarriers;
        this.bufferMemoryBarriers = bufferMemoryBarriers;
        this.imageMemoryBarriers = imageMemoryBarriers;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        final List<MemoryBarrier> memoryBarriersSafe = Optional.ofNullable(memoryBarriers)
                .orElseGet(Collections::emptyList);

        final VkMemoryBarrier.Buffer memoryBarrierBuffer = VkMemoryBarrier.calloc(memoryBarriersSafe.size());

        memoryBarriersSafe.stream()
                .map(memoryBarrier -> VkMemoryBarrier.calloc()
                        .sType(VK_STRUCTURE_TYPE_MEMORY_BARRIER)
                        .srcAccessMask(Maskable.toBitMask(memoryBarrier.getSourceAccess()))
                        .dstAccessMask(Maskable.toBitMask(memoryBarrier.getDestinationAccess())))
                .forEachOrdered(memoryBarrierBuffer::put);

        memoryBarrierBuffer.flip();

        final List<BufferMemoryBarrier> bufferMemoryBarriersSafe = Optional.ofNullable(bufferMemoryBarriers)
                .orElseGet(Collections::emptyList);

        final VkBufferMemoryBarrier.Buffer bufferMemoryBarrierBuffer = VkBufferMemoryBarrier.calloc(bufferMemoryBarriersSafe.size());

        bufferMemoryBarriersSafe.stream()
                .map(bufferMemoryBarrier -> VkBufferMemoryBarrier.calloc()
                        .sType(VK_STRUCTURE_TYPE_BUFFER_MEMORY_BARRIER)
                        .srcAccessMask(Maskable.toBitMask(bufferMemoryBarrier.getSourceAccess()))
                        .dstAccessMask(Maskable.toBitMask(bufferMemoryBarrier.getDestinationAccess()))
                        .srcQueueFamilyIndex(Optional.ofNullable(bufferMemoryBarrier.getSourceQueueFamily())
                                .map(QueueFamily::getIndex)
                                .orElse(VK_QUEUE_FAMILY_IGNORED))
                        .dstQueueFamilyIndex(Optional.ofNullable(bufferMemoryBarrier.getDestinationQueueFamily())
                                .map(QueueFamily::getIndex)
                                .orElse(VK_QUEUE_FAMILY_IGNORED))
                        .buffer(bufferMemoryBarrier.getBuffer().getAddress())
                        .offset(bufferMemoryBarrier.getOffset())
                        .size(bufferMemoryBarrier.getSize()))
                .forEachOrdered(bufferMemoryBarrierBuffer::put);

        bufferMemoryBarrierBuffer.flip();

        final List<ImageMemoryBarrier> imageMemoryBarriersSafe = Optional.ofNullable(imageMemoryBarriers)
                .orElseGet(Collections::emptyList);

        final VkImageMemoryBarrier.Buffer imageMemoryBarrierBuffer = VkImageMemoryBarrier.calloc(imageMemoryBarriersSafe.size());

        imageMemoryBarriersSafe.stream()
                .map(imageMemoryBarrier -> {
                    final VkImageSubresourceRange vkImageSubresourceRange = Optional.ofNullable(imageMemoryBarrier.getSubresourceRange())
                            .map(subresourceRange -> VkImageSubresourceRange.calloc()
                                    .aspectMask(Maskable.toBitMask(subresourceRange.getAspects()))
                                    .baseMipLevel(subresourceRange.getBaseMipLevel())
                                    .levelCount(subresourceRange.getLevelCount())
                                    .baseArrayLayer(subresourceRange.getBaseArrayLayer())
                                    .layerCount(subresourceRange.getLayerCount()))
                            .orElse(null);

                    return VkImageMemoryBarrier.calloc()
                                    .sType(VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER)
                                    .srcAccessMask(Maskable.toBitMask(imageMemoryBarrier.getSourceAccess()))
                                    .dstAccessMask(Maskable.toBitMask(imageMemoryBarrier.getDestinationAccess()))
                                    .oldLayout(HasValue.getValue(imageMemoryBarrier.getOldLayout()))
                                    .newLayout(HasValue.getValue(imageMemoryBarrier.getNewLayout()))
                                    .srcQueueFamilyIndex(Optional.ofNullable(imageMemoryBarrier.getSourceQueueFamily())
                                            .map(QueueFamily::getIndex)
                                            .orElse(VK_QUEUE_FAMILY_IGNORED))
                                    .dstQueueFamilyIndex(Optional.ofNullable(imageMemoryBarrier.getDestinationQueueFamily())
                                            .map(QueueFamily::getIndex)
                                            .orElse(VK_QUEUE_FAMILY_IGNORED))
                                    .image(imageMemoryBarrier.getImage().getAddress())
                                    .subresourceRange(vkImageSubresourceRange);
                }).forEachOrdered(imageMemoryBarrierBuffer::put);

        imageMemoryBarrierBuffer.flip();

        try {
            vkCmdPipelineBarrier(commandBuffer.unwrap(), Maskable.toBitMask(sourceStages),
                    Maskable.toBitMask(destinationStages), Maskable.toBitMask(dependencies), memoryBarrierBuffer,
                    bufferMemoryBarrierBuffer, imageMemoryBarrierBuffer);
        } finally {
            memoryBarrierBuffer.free();
            bufferMemoryBarrierBuffer.free();
            imageMemoryBarrierBuffer.free();
        }
    }
}
