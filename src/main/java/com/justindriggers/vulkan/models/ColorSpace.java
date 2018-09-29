package com.justindriggers.vulkan.models;

import org.lwjgl.vulkan.EXTSwapchainColorspace;
import org.lwjgl.vulkan.KHRSurface;

public enum ColorSpace implements HasValue<Integer> {

    SRGB_NONLINEAR(KHRSurface.VK_COLOR_SPACE_SRGB_NONLINEAR_KHR),
    DISPLAY_P3_NONLINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_DISPLAY_P3_NONLINEAR_EXT),
    EXTENDED_SRGB_LINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_EXTENDED_SRGB_LINEAR_EXT),
    DCI_P3_LINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_DCI_P3_LINEAR_EXT),
    DCI_P3_NONLINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_DCI_P3_NONLINEAR_EXT),
    BT709_LINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_BT709_LINEAR_EXT),
    BT709_NONLINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_BT709_NONLINEAR_EXT),
    BT2020_LINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_BT2020_LINEAR_EXT),
    HDR10_ST2084(EXTSwapchainColorspace.VK_COLOR_SPACE_HDR10_ST2084_EXT),
    DOLBYVISION(EXTSwapchainColorspace.VK_COLOR_SPACE_DOLBYVISION_EXT),
    HDR10_HLG(EXTSwapchainColorspace.VK_COLOR_SPACE_HDR10_HLG_EXT),
    ADOBERGB_LINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_ADOBERGB_LINEAR_EXT),
    ADOBERGB_NONLINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_ADOBERGB_NONLINEAR_EXT),
    PASS_THROUGH(EXTSwapchainColorspace.VK_COLOR_SPACE_PASS_THROUGH_EXT),
    EXTENDED_SRGB_NONLINEAR(EXTSwapchainColorspace.VK_COLOR_SPACE_EXTENDED_SRGB_NONLINEAR_EXT);

    private final int value;

    ColorSpace(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
