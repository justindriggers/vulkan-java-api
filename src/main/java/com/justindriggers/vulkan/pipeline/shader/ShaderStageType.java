package com.justindriggers.vulkan.pipeline.shader;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VK10;

public enum ShaderStageType implements Maskable {

    VERTEX(VK10.VK_SHADER_STAGE_VERTEX_BIT),
    TESSELLATION_CONTROL(VK10.VK_SHADER_STAGE_TESSELLATION_CONTROL_BIT),
    TESSELLATION_EVALUATION(VK10.VK_SHADER_STAGE_TESSELLATION_EVALUATION_BIT),
    GEOMETRY(VK10.VK_SHADER_STAGE_GEOMETRY_BIT),
    FRAGMENT(VK10.VK_SHADER_STAGE_FRAGMENT_BIT),
    COMPUTE(VK10.VK_SHADER_STAGE_COMPUTE_BIT),
    ALL_GRAPHICS(VK10.VK_SHADER_STAGE_ALL_GRAPHICS),
    ALL(VK10.VK_SHADER_STAGE_ALL);

    private final int bit;

    ShaderStageType(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
