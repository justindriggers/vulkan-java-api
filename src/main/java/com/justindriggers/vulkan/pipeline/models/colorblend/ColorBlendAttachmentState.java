package com.justindriggers.vulkan.pipeline.models.colorblend;

import java.util.Set;

public class ColorBlendAttachmentState {

    private final boolean isBlendEnabled;
    private final BlendFactor sourceColorBlendFactor;
    private final BlendFactor destinationColorBlendFactor;
    private final BlendOperation colorBlendOperation;
    private final BlendFactor sourceAlphaBlendFactor;
    private final BlendFactor destinationAlphaBlendFactor;
    private final BlendOperation alphaBlendOperation;
    private final Set<ColorComponent> colorWrites;

    public ColorBlendAttachmentState(final boolean isBlendEnabled,
                                     final BlendFactor sourceColorBlendFactor,
                                     final BlendFactor destinationColorBlendFactor,
                                     final BlendOperation colorBlendOperation,
                                     final BlendFactor sourceAlphaBlendFactor,
                                     final BlendFactor destinationAlphaBlendFactor,
                                     final BlendOperation alphaBlendOperation,
                                     final Set<ColorComponent> colorWrites) {
        this.isBlendEnabled = isBlendEnabled;
        this.sourceColorBlendFactor = sourceColorBlendFactor;
        this.destinationColorBlendFactor = destinationColorBlendFactor;
        this.colorBlendOperation = colorBlendOperation;
        this.sourceAlphaBlendFactor = sourceAlphaBlendFactor;
        this.destinationAlphaBlendFactor = destinationAlphaBlendFactor;
        this.alphaBlendOperation = alphaBlendOperation;
        this.colorWrites = colorWrites;
    }

    public boolean isBlendEnabled() {
        return isBlendEnabled;
    }

    public BlendFactor getSourceColorBlendFactor() {
        return sourceColorBlendFactor;
    }

    public BlendFactor getDestinationColorBlendFactor() {
        return destinationColorBlendFactor;
    }

    public BlendOperation getColorBlendOperation() {
        return colorBlendOperation;
    }

    public BlendFactor getSourceAlphaBlendFactor() {
        return sourceAlphaBlendFactor;
    }

    public BlendFactor getDestinationAlphaBlendFactor() {
        return destinationAlphaBlendFactor;
    }

    public BlendOperation getAlphaBlendOperation() {
        return alphaBlendOperation;
    }

    public Set<ColorComponent> getColorWrites() {
        return colorWrites;
    }
}
