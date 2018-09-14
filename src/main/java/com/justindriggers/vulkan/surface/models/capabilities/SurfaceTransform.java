package com.justindriggers.vulkan.surface.models.capabilities;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.KHRSurface;

public enum SurfaceTransform implements Maskable {

    IDENTITY(KHRSurface.VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR),
    ROTATE_90(KHRSurface.VK_SURFACE_TRANSFORM_ROTATE_90_BIT_KHR),
    ROTATE_180(KHRSurface.VK_SURFACE_TRANSFORM_ROTATE_180_BIT_KHR),
    ROTATE_270(KHRSurface.VK_SURFACE_TRANSFORM_ROTATE_270_BIT_KHR),
    HORIZONTAL_MIRROR(KHRSurface.VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_BIT_KHR),
    HORIZONTAL_MIRROR_ROTATE_90(KHRSurface.VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_ROTATE_90_BIT_KHR),
    HORIZONTAL_MIRROR_ROTATE_180(KHRSurface.VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_ROTATE_180_BIT_KHR),
    HORIZONTAL_MIRROR_ROTATE_270(KHRSurface.VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_ROTATE_270_BIT_KHR),
    INHERIT(KHRSurface.VK_SURFACE_TRANSFORM_INHERIT_BIT_KHR);

    private final int bit;

    SurfaceTransform(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
