package com.justindriggers.vulkan.models;

public class Offset3D extends Offset2D {

    private final int z;

    public Offset3D(final int x, final int y, final int z) {
        super(x, y);
        this.z = z;
    }

    public int getZ() {
        return z;
    }
}
