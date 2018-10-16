package com.justindriggers.vulkan.models;

public class Offset2D {

    private final int x;
    private final int y;

    public Offset2D(final int x,
                    final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
