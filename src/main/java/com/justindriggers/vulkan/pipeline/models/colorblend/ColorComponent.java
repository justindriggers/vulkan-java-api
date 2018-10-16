package com.justindriggers.vulkan.pipeline.models.colorblend;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VK10;

public enum ColorComponent implements Maskable {

    R(VK10.VK_COLOR_COMPONENT_R_BIT),
    G(VK10.VK_COLOR_COMPONENT_G_BIT),
    B(VK10.VK_COLOR_COMPONENT_B_BIT),
    A(VK10.VK_COLOR_COMPONENT_A_BIT);

    private final int bit;

    ColorComponent(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
