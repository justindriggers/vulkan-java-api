package com.justindriggers.vulkan.models.pointers;

public abstract class Pointer {

    private final long handle;

    protected Pointer(final long handle) {
        this.handle = handle;
    }

    public long getAddress() {
        return handle;
    }
}
