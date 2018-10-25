package com.justindriggers.vulkan.command.commands;

import com.justindriggers.vulkan.command.CommandBuffer;
import com.justindriggers.vulkan.command.commands.models.ImageSubresourceRange;
import com.justindriggers.vulkan.image.Image;
import com.justindriggers.vulkan.image.models.ImageLayout;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.clear.ClearColor;
import org.lwjgl.vulkan.VkClearValue;
import org.lwjgl.vulkan.VkImageSubresourceRange;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.vulkan.VK10.vkCmdClearColorImage;

public class ClearColorImageCommand implements Command {

    private final Image image;
    private final ImageLayout imageLayout;
    private final ClearColor clearColor;
    private final List<ImageSubresourceRange> regions;

    public ClearColorImageCommand(final Image image,
                                  final ImageLayout imageLayout,
                                  final ClearColor clearColor,
                                  final List<ImageSubresourceRange> regions) {
        this.image = image;
        this.imageLayout = imageLayout;
        this.clearColor = clearColor;
        this.regions = regions;
    }

    @Override
    public void execute(final CommandBuffer commandBuffer) {
        final VkClearValue clearValue = clearColor.toStruct();

        final List<ImageSubresourceRange> regionsSafe = Optional.ofNullable(regions)
                .orElseGet(Collections::emptyList);

        final VkImageSubresourceRange.Buffer regionBuffer = VkImageSubresourceRange.calloc(regionsSafe.size());

        regionsSafe.stream()
                .map(region -> VkImageSubresourceRange.calloc()
                        .aspectMask(Maskable.toBitMask(region.getAspects()))
                        .baseMipLevel(region.getBaseMipLevel())
                        .levelCount(region.getLevelCount())
                        .baseArrayLayer(region.getBaseArrayLayer())
                        .layerCount(region.getLayerCount()))
                .forEachOrdered(regionBuffer::put);

        regionBuffer.flip();

        try {
            vkCmdClearColorImage(commandBuffer.unwrap(), image.getAddress(), HasValue.getValue(imageLayout),
                    clearValue.color(), regionBuffer);
        } finally {
            clearValue.free();
            regionBuffer.free();
        }
    }
}
