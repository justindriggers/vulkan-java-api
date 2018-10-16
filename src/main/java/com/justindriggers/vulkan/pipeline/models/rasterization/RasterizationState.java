package com.justindriggers.vulkan.pipeline.models.rasterization;

public class RasterizationState {

    private final boolean isDepthClampEnabled;
    private final boolean isRasterizerDiscardEnabled;
    private final PolygonMode polygonMode;
    private final CullMode cullMode;
    private final FrontFace frontFace;
    private final boolean isDepthBiasEnabled;
    private final float depthBiasConstantFactor;
    private final float depthBiasClamp;
    private final float depthBiasSlopeFactor;
    private final float lineWidth;

    public RasterizationState(final boolean isDepthClampEnabled,
                              final boolean isRasterizerDiscardEnabled,
                              final PolygonMode polygonMode,
                              final CullMode cullMode,
                              final FrontFace frontFace,
                              final boolean isDepthBiasEnabled,
                              final float depthBiasConstantFactor,
                              final float depthBiasClamp,
                              final float depthBiasSlopeFactor,
                              final float lineWidth) {
        this.isDepthClampEnabled = isDepthClampEnabled;
        this.isRasterizerDiscardEnabled = isRasterizerDiscardEnabled;
        this.polygonMode = polygonMode;
        this.cullMode = cullMode;
        this.frontFace = frontFace;
        this.isDepthBiasEnabled = isDepthBiasEnabled;
        this.depthBiasConstantFactor = depthBiasConstantFactor;
        this.depthBiasClamp = depthBiasClamp;
        this.depthBiasSlopeFactor = depthBiasSlopeFactor;
        this.lineWidth = lineWidth;
    }

    public boolean isDepthClampEnabled() {
        return isDepthClampEnabled;
    }

    public boolean isRasterizerDiscardEnabled() {
        return isRasterizerDiscardEnabled;
    }

    public PolygonMode getPolygonMode() {
        return polygonMode;
    }

    public CullMode getCullMode() {
        return cullMode;
    }

    public FrontFace getFrontFace() {
        return frontFace;
    }

    public boolean isDepthBiasEnabled() {
        return isDepthBiasEnabled;
    }

    public float getDepthBiasConstantFactor() {
        return depthBiasConstantFactor;
    }

    public float getDepthBiasClamp() {
        return depthBiasClamp;
    }

    public float getDepthBiasSlopeFactor() {
        return depthBiasSlopeFactor;
    }

    public float getLineWidth() {
        return lineWidth;
    }
}
