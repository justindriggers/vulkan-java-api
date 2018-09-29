package com.justindriggers.vulkan.pipeline.shader;

public class ShaderStage implements AutoCloseable {

    private final ShaderStageType type;
    private final ShaderModule module;
    private final String entryPoint;

    public ShaderStage(final ShaderStageType type,
                       final ShaderModule module,
                       final String entryPoint) {
        this.type = type;
        this.module = module;
        this.entryPoint = entryPoint;
    }

    public ShaderStageType getType() {
        return type;
    }

    public ShaderModule getModule() {
        return module;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    @Override
    public void close() {
        module.close();
    }
}
