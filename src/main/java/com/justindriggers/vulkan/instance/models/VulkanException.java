package com.justindriggers.vulkan.instance.models;

public class VulkanException extends RuntimeException {

    private final VulkanResult result;

    public VulkanException(final VulkanResult result) {
        super(String.format("%s (%d)", result.toString(), result.getValue()));

        this.result = result;
    }

    public VulkanResult getResult() {
        return result;
    }
}
