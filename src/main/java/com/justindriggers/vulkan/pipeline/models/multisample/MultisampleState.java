package com.justindriggers.vulkan.pipeline.models.multisample;

import com.justindriggers.vulkan.models.SampleCount;

public class MultisampleState {

    private final SampleCount rasterizationSamples;
    private final boolean isSampleShadingEnabled;
    private final float minSampleShading;
    private final boolean isAlphaToCoverageEnabled;
    private final boolean isAlphaToOneEnabled;

    public MultisampleState(final SampleCount rasterizationSamples,
                            final boolean isSampleShadingEnabled,
                            final float minSampleShading,
                            final boolean isAlphaToCoverageEnabled,
                            final boolean isAlphaToOneEnabled) {
        this.rasterizationSamples = rasterizationSamples;
        this.isSampleShadingEnabled = isSampleShadingEnabled;
        this.minSampleShading = minSampleShading;
        this.isAlphaToCoverageEnabled = isAlphaToCoverageEnabled;
        this.isAlphaToOneEnabled = isAlphaToOneEnabled;
    }

    public SampleCount getRasterizationSamples() {
        return rasterizationSamples;
    }

    public boolean isSampleShadingEnabled() {
        return isSampleShadingEnabled;
    }

    public float getMinSampleShading() {
        return minSampleShading;
    }

    public boolean isAlphaToCoverageEnabled() {
        return isAlphaToCoverageEnabled;
    }

    public boolean isAlphaToOneEnabled() {
        return isAlphaToOneEnabled;
    }
}
