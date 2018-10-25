package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.buffer.Buffer;
import com.justindriggers.vulkan.command.CommandBuffer;
import com.justindriggers.vulkan.command.commands.models.BufferImageCopy;
import com.justindriggers.vulkan.image.Image;
import com.justindriggers.vulkan.image.models.ImageLayout;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VkBufferImageCopy;
import org.lwjgl.vulkan.VkExtent3D;
import org.lwjgl.vulkan.VkImageSubresourceLayers;
import org.lwjgl.vulkan.VkOffset3D;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.vulkan.VK10.vkCmdCopyImageToBuffer;

public class CopyImageToBufferCommand implements Command {

    private final Image sourceImage;
    private final ImageLayout sourceImageLayout;
    private final Buffer destinationBuffer;
    private final List<BufferImageCopy> regions;

    public CopyImageToBufferCommand(final Image sourceImage,
                                    final ImageLayout sourceImageLayout,
                                    final Buffer destinationBuffer,
                                    final List<BufferImageCopy> regions) {
        this.sourceImage = sourceImage;
        this.sourceImageLayout = sourceImageLayout;
        this.destinationBuffer = destinationBuffer;
        this.regions = regions;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        final List<BufferImageCopy> regionsSafe = Optional.ofNullable(regions)
                .orElseGet(Collections::emptyList);

        final VkBufferImageCopy.Buffer regionsBuffer = VkBufferImageCopy.calloc(regionsSafe.size());

        regionsSafe.stream()
                .map(region -> {
                    final VkImageSubresourceLayers imageSubresource = Optional.ofNullable(region.getImageSubresource())
                            .map(subresource -> VkImageSubresourceLayers.calloc()
                                    .aspectMask(Maskable.toBitMask(subresource.getAspects()))
                                    .mipLevel(subresource.getMipLevel())
                                    .baseArrayLayer(subresource.getBaseArrayLayer())
                                    .layerCount(subresource.getLayerCount()))
                            .orElse(null);

                    final VkOffset3D imageOffset = Optional.ofNullable(region.getImageOffset())
                            .map(offset -> VkOffset3D.calloc()
                                    .x(offset.getX())
                                    .y(offset.getY())
                                    .z(offset.getZ()))
                            .orElse(null);

                    final VkExtent3D imageExtent = Optional.ofNullable(region.getImageExtent())
                            .map(extent3D -> VkExtent3D.calloc()
                                    .width(extent3D.getWidth())
                                    .height(extent3D.getHeight())
                                    .depth(extent3D.getDepth()))
                            .orElse(null);

                    return VkBufferImageCopy.calloc()
                            .bufferOffset(region.getBufferOffset())
                            .bufferRowLength(region.getBufferRowLength())
                            .bufferImageHeight(region.getBufferImageHeight())
                            .imageSubresource(imageSubresource)
                            .imageOffset(imageOffset)
                            .imageExtent(imageExtent);
                }).forEachOrdered(regionsBuffer::put);

        regionsBuffer.flip();

        try {
            vkCmdCopyImageToBuffer(commandBuffer.unwrap(), sourceImage.getAddress(),
                    HasValue.getValue(sourceImageLayout), destinationBuffer.getAddress(), regionsBuffer);
        } finally {
            regionsBuffer.free();
        }
    }
}
