package com.justindriggers.vulkan.models;

public class Rect2D {

    private final Offset2D offset;
    private final Extent2D extent;

    public Rect2D(final Offset2D offset,
                  final Extent2D extent) {
        this.offset = offset;
        this.extent = extent;
    }

    public Rect2D(final int x,
                  final int y,
                  final int width,
                  final int height) {
        this.offset = new Offset2D(x, y);
        this.extent = new Extent2D(width, height);
    }

    public Offset2D getOffset() {
        return offset;
    }

    public Extent2D getExtent() {
        return extent;
    }
}
