package com.justindriggers.vulkan.models;

import org.lwjgl.vulkan.VkExtensionProperties;

import java.util.Objects;

public class Extension {

    private final String name;
    private final int version;

    public Extension(final VkExtensionProperties vkExtensionProperties) {
        this(
                vkExtensionProperties.extensionNameString(),
                vkExtensionProperties.specVersion()
        );
    }

    public Extension(final String name,
                     final int version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Extension that = (Extension) o;

        return version == that.version &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                version
        );
    }
}
