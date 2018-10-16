package com.justindriggers.vulkan.pipeline.models.depth;

public class StencilOperationState {

    private final StencilOperation failOperation;
    private final StencilOperation passOperation;
    private final StencilOperation depthFailOperation;
    private final CompareOperator compareOperator;
    private final int compareMask;
    private final int writeMask;
    private final int reference;

    public StencilOperationState(final StencilOperation failOperation,
                                 final StencilOperation passOperation,
                                 final StencilOperation depthFailOperation,
                                 final CompareOperator compareOperator,
                                 final int compareMask,
                                 final int writeMask,
                                 final int reference) {
        this.failOperation = failOperation;
        this.passOperation = passOperation;
        this.depthFailOperation = depthFailOperation;
        this.compareOperator = compareOperator;
        this.compareMask = compareMask;
        this.writeMask = writeMask;
        this.reference = reference;
    }

    public StencilOperation getFailOperation() {
        return failOperation;
    }

    public StencilOperation getPassOperation() {
        return passOperation;
    }

    public StencilOperation getDepthFailOperation() {
        return depthFailOperation;
    }

    public CompareOperator getCompareOperator() {
        return compareOperator;
    }

    public int getCompareMask() {
        return compareMask;
    }

    public int getWriteMask() {
        return writeMask;
    }

    public int getReference() {
        return reference;
    }
}
