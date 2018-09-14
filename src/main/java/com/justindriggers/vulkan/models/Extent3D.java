package com.justindriggers.vulkan.models;

import org.lwjgl.vulkan.VkExtent3D;

import java.util.Objects;

public class Extent3D extends Extent2D {

    private final int depth;

    public Extent3D(final VkExtent3D vkExtent3D) {
        this(vkExtent3D.width(), vkExtent3D.height(), vkExtent3D.depth());
    }

    public Extent3D(final int width, final int height, final int depth) {
        super(width, height);

        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Extent3D)) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        Extent3D extent3D = (Extent3D) o;

        return depth == extent3D.depth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                depth
        );
    }
}
