package com.justindriggers.vulkan.devices.physical.models;

import java.util.Set;

public class FormatProperties {

    private final Set<FormatFeature> linearTilingFeatures;
    private final Set<FormatFeature> optimalTilingFeatures;
    private final Set<FormatFeature> bufferFeatures;

    public FormatProperties(final Set<FormatFeature> linearTilingFeatures,
                            final Set<FormatFeature> optimalTilingFeatures,
                            final Set<FormatFeature> bufferFeatures) {
        this.linearTilingFeatures = linearTilingFeatures;
        this.optimalTilingFeatures = optimalTilingFeatures;
        this.bufferFeatures = bufferFeatures;
    }

    public Set<FormatFeature> getLinearTilingFeatures() {
        return linearTilingFeatures;
    }

    public Set<FormatFeature> getOptimalTilingFeatures() {
        return optimalTilingFeatures;
    }

    public Set<FormatFeature> getBufferFeatures() {
        return bufferFeatures;
    }
}
