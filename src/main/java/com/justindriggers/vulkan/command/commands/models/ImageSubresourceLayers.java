package com.justindriggers.vulkan.command.commands.models;

import com.justindriggers.vulkan.image.models.ImageAspect;

import java.util.Set;

public class ImageSubresourceLayers {

    private final Set<ImageAspect> aspects;
    private final int mipLevel;
    private final int baseArrayLayer;
    private final int layerCount;

    public ImageSubresourceLayers(final Set<ImageAspect> aspects,
                                  final int mipLevel,
                                  final int baseArrayLayer,
                                  final int layerCount) {
        this.aspects = aspects;
        this.mipLevel = mipLevel;
        this.baseArrayLayer = baseArrayLayer;
        this.layerCount = layerCount;
    }

    public Set<ImageAspect> getAspects() {
        return aspects;
    }

    public int getMipLevel() {
        return mipLevel;
    }

    public int getBaseArrayLayer() {
        return baseArrayLayer;
    }

    public int getLayerCount() {
        return layerCount;
    }
}
