package com.justindriggers.vulkan.pipeline.models.viewport;

public class Viewport {

    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final float minDepth;
    private final float maxDepth;

    public Viewport(final float x,
                    final float y,
                    final float width,
                    final float height,
                    final float minDepth,
                    final float maxDepth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getMinDepth() {
        return minDepth;
    }

    public float getMaxDepth() {
        return maxDepth;
    }
}
