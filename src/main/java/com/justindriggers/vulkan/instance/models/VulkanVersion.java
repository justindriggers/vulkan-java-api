package com.justindriggers.vulkan.instance.models;

public class VulkanVersion {

    private final int major;
    private final int minor;
    private final int patch;

    public VulkanVersion(final int major,
                         final int minor,
                         final int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }
}
