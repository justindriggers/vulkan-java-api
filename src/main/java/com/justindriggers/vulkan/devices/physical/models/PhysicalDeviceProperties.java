package com.justindriggers.vulkan.devices.physical.models;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;

import java.util.Objects;
import java.util.UUID;

// TODO VkPhysicalDeviceLimits and VkPhysicalDeviceSparseProperties
public class PhysicalDeviceProperties {

    private final int apiVersion;
    private final int driverVersion;
    private final int vendorId;
    private final int deviceId;
    private final PhysicalDeviceType deviceType;
    private final String deviceName;
    private final UUID pipelineCacheUuid;

    public PhysicalDeviceProperties(final VkPhysicalDeviceProperties vkPhysicalDeviceProperties) {
        this(
                vkPhysicalDeviceProperties.apiVersion(),
                vkPhysicalDeviceProperties.driverVersion(),
                vkPhysicalDeviceProperties.vendorID(),
                vkPhysicalDeviceProperties.deviceID(),
                HasValue.getByValue(vkPhysicalDeviceProperties.deviceType(), PhysicalDeviceType.class),
                vkPhysicalDeviceProperties.deviceNameString(),
                new UUID(
                        vkPhysicalDeviceProperties.pipelineCacheUUID().getLong(),
                        vkPhysicalDeviceProperties.pipelineCacheUUID().getLong()
                )
        );
    }

    public PhysicalDeviceProperties(final int apiVersion,
                                    final int driverVersion,
                                    final int vendorId,
                                    final int deviceId,
                                    final PhysicalDeviceType deviceType,
                                    final String deviceName,
                                    final UUID pipelineCacheUuid) {
        this.apiVersion = apiVersion;
        this.driverVersion = driverVersion;
        this.vendorId = vendorId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.pipelineCacheUuid = pipelineCacheUuid;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public int getDriverVersion() {
        return driverVersion;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public PhysicalDeviceType getDeviceType() {
        return deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public UUID getPipelineCacheUuid() {
        return pipelineCacheUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhysicalDeviceProperties that = (PhysicalDeviceProperties) o;

        return apiVersion == that.apiVersion &&
                driverVersion == that.driverVersion &&
                vendorId == that.vendorId &&
                deviceId == that.deviceId &&
                deviceType == that.deviceType &&
                Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(pipelineCacheUuid, that.pipelineCacheUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                apiVersion,
                driverVersion,
                vendorId,
                deviceId,
                deviceType,
                deviceName,
                pipelineCacheUuid
        );
    }
}
