package com.justindriggers.vulkan.pipeline.models.depth;

public class DepthStencilState {

    private final boolean isDepthTestEnabled;
    private final boolean isDepthWriteEnabled;
    private final CompareOperator depthCompareOperator;
    private final boolean isDepthBoundsTestEnabled;
    private final boolean isStencilTestEnabled;
    private final StencilOperationState frontOperationState;
    private final StencilOperationState backOperationState;
    private final float minDepthBounds;
    private final float maxDepthBounds;

    public DepthStencilState(final boolean isDepthTestEnabled,
                             final boolean isDepthWriteEnabled,
                             final CompareOperator depthCompareOperator,
                             final boolean isDepthBoundsTestEnabled,
                             final boolean isStencilTestEnabled,
                             final StencilOperationState frontOperationState,
                             final StencilOperationState backOperationState,
                             final float minDepthBounds,
                             final float maxDepthBounds) {
        this.isDepthTestEnabled = isDepthTestEnabled;
        this.isDepthWriteEnabled = isDepthWriteEnabled;
        this.depthCompareOperator = depthCompareOperator;
        this.isDepthBoundsTestEnabled = isDepthBoundsTestEnabled;
        this.isStencilTestEnabled = isStencilTestEnabled;
        this.frontOperationState = frontOperationState;
        this.backOperationState = backOperationState;
        this.minDepthBounds = minDepthBounds;
        this.maxDepthBounds = maxDepthBounds;
    }

    public boolean isDepthTestEnabled() {
        return isDepthTestEnabled;
    }

    public boolean isDepthWriteEnabled() {
        return isDepthWriteEnabled;
    }

    public CompareOperator getDepthCompareOperator() {
        return depthCompareOperator;
    }

    public boolean isDepthBoundsTestEnabled() {
        return isDepthBoundsTestEnabled;
    }

    public boolean isStencilTestEnabled() {
        return isStencilTestEnabled;
    }

    public StencilOperationState getFrontOperationState() {
        return frontOperationState;
    }

    public StencilOperationState getBackOperationState() {
        return backOperationState;
    }

    public float getMinDepthBounds() {
        return minDepthBounds;
    }

    public float getMaxDepthBounds() {
        return maxDepthBounds;
    }
}
