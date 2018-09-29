package com.justindriggers.vulkan.pipeline.models.assembly;

public class InputAssemblyState {

    private final PrimitiveTopology topology;
    private final boolean isPrimitiveRestartEnabled;

    public InputAssemblyState(final PrimitiveTopology topology,
                              final boolean isPrimitiveRestartEnabled) {
        this.topology = topology;
        this.isPrimitiveRestartEnabled = isPrimitiveRestartEnabled;
    }

    public PrimitiveTopology getTopology() {
        return topology;
    }

    public boolean isPrimitiveRestartEnabled() {
        return isPrimitiveRestartEnabled;
    }
}
