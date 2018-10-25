package com.justindriggers.vulkan.instance.models;

public class ApplicationInfo {

    private final String applicationName;
    private final int applicationVersion;
    private final String engineName;
    private final int engineVersion;
    private final VulkanVersion apiVersion;

    public ApplicationInfo(final String applicationName,
                           final int applicationVersion,
                           final String engineName,
                           final int engineVersion,
                           final VulkanVersion apiVersion) {
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.engineName = engineName;
        this.engineVersion = engineVersion;
        this.apiVersion = apiVersion;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public String getEngineName() {
        return engineName;
    }

    public int getEngineVersion() {
        return engineVersion;
    }

    public VulkanVersion getApiVersion() {
        return apiVersion;
    }
}
