package com.justindriggers.vulkan.command.commands.models;

import com.justindriggers.vulkan.image.models.ImageAspect;

import java.util.Set;

public class ImageSubresourceRange {

    private final Set<ImageAspect> aspects;
    private final int baseMipLevel;
    private final int levelCount;
    private final int baseArrayLayer;
    private final int layerCount;

    public ImageSubresourceRange(final Set<ImageAspect> aspects,
                                 final int baseMipLevel,
                                 final int levelCount,
                                 final int baseArrayLayer,
                                 final int layerCount) {
        this.aspects = aspects;
        this.baseMipLevel = baseMipLevel;
        this.levelCount = levelCount;
        this.baseArrayLayer = baseArrayLayer;
        this.layerCount = layerCount;
    }

    public Set<ImageAspect> getAspects() {
        return aspects;
    }

    public int getBaseMipLevel() {
        return baseMipLevel;
    }

    public int getLevelCount() {
        return levelCount;
    }

    public int getBaseArrayLayer() {
        return baseArrayLayer;
    }

    public int getLayerCount() {
        return layerCount;
    }
}
