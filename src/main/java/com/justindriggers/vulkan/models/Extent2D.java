package com.justindriggers.vulkan.models;

import org.lwjgl.vulkan.VkExtent2D;

import java.util.Objects;

public class Extent2D {

    private final int width;
    private final int height;

    public Extent2D(final VkExtent2D vkExtent2D) {
        this(vkExtent2D.width(), vkExtent2D.height());
    }

    public Extent2D(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Extent2D extent2D = (Extent2D) o;

        return width == extent2D.width &&
                height == extent2D.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                width,
                height
        );
    }
}
