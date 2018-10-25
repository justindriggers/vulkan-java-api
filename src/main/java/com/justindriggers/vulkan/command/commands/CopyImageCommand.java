package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.command.CommandBuffer;
import com.justindriggers.vulkan.command.commands.models.ImageCopy;
import com.justindriggers.vulkan.image.Image;
import com.justindriggers.vulkan.image.models.ImageLayout;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VkExtent3D;
import org.lwjgl.vulkan.VkImageCopy;
import org.lwjgl.vulkan.VkImageSubresourceLayers;
import org.lwjgl.vulkan.VkOffset3D;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.vulkan.VK10.vkCmdCopyImage;

public class CopyImageCommand implements Command {

    private final Image sourceImage;
    private final ImageLayout sourceImageLayout;
    private final Image destinationImage;
    private final ImageLayout destinationImageLayout;
    private final List<ImageCopy> regions;

    public CopyImageCommand(final Image sourceImage,
                            final ImageLayout sourceImageLayout,
                            final Image destinationImage,
                            final ImageLayout destinationImageLayout,
                            final List<ImageCopy> regions) {
        this.sourceImage = sourceImage;
        this.sourceImageLayout = sourceImageLayout;
        this.destinationImage = destinationImage;
        this.destinationImageLayout = destinationImageLayout;
        this.regions = regions;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        final List<ImageCopy> regionsSafe = Optional.ofNullable(regions)
                .orElseGet(Collections::emptyList);

        final VkImageCopy.Buffer imageCopyBuffer = VkImageCopy.calloc(regionsSafe.size());

        regionsSafe.stream()
                .map(region -> {
                    final VkImageSubresourceLayers srcSubresource = Optional.ofNullable(region.getSourceSubresource())
                            .map(subresource -> VkImageSubresourceLayers.calloc()
                                    .aspectMask(Maskable.toBitMask(subresource.getAspects()))
                                    .mipLevel(subresource.getMipLevel())
                                    .baseArrayLayer(subresource.getBaseArrayLayer())
                                    .layerCount(subresource.getLayerCount()))
                            .orElse(null);

                    final VkOffset3D srcOffset = Optional.ofNullable(region.getSourceOffset())
                            .map(offset -> VkOffset3D.calloc()
                                    .x(offset.getX())
                                    .y(offset.getY())
                                    .z(offset.getZ()))
                            .orElse(null);

                    final VkImageSubresourceLayers dstSubresource = Optional.ofNullable(region.getDestinationSubresource())
                            .map(subresource -> VkImageSubresourceLayers.calloc()
                                    .aspectMask(Maskable.toBitMask(subresource.getAspects()))
                                    .mipLevel(subresource.getMipLevel())
                                    .baseArrayLayer(subresource.getBaseArrayLayer())
                                    .layerCount(subresource.getLayerCount()))
                            .orElse(null);

                    final VkOffset3D dstOffset = Optional.ofNullable(region.getDestinationOffset())
                            .map(offset -> VkOffset3D.calloc()
                                    .x(offset.getX())
                                    .y(offset.getY())
                                    .z(offset.getZ()))
                            .orElse(null);

                    final VkExtent3D extent = Optional.ofNullable(region.getExtent())
                            .map(extent3D -> VkExtent3D.calloc()
                                    .width(extent3D.getWidth())
                                    .height(extent3D.getHeight())
                                    .depth(extent3D.getDepth()))
                            .orElse(null);

                    return VkImageCopy.calloc()
                            .srcSubresource(srcSubresource)
                            .srcOffset(srcOffset)
                            .dstSubresource(dstSubresource)
                            .dstOffset(dstOffset)
                            .extent(extent);
                }).forEachOrdered(imageCopyBuffer::put);

        imageCopyBuffer.flip();

        try {
            vkCmdCopyImage(commandBuffer.unwrap(), sourceImage.getAddress(), HasValue.getValue(sourceImageLayout),
                    destinationImage.getAddress(), HasValue.getValue(destinationImageLayout), imageCopyBuffer);
        } finally {
            imageCopyBuffer.free();
        }
    }
}
