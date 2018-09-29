package com.justindriggers.vulkan.surface.models.capabilities;

import com.justindriggers.vulkan.models.Extent2D;
import com.justindriggers.vulkan.models.ImageUsage;
import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;

import java.util.Objects;
import java.util.Set;

public class SurfaceCapabilities {

    private final int minImageCount;
    private final int maxImageCount;
    private final Extent2D currentExtent;
    private final Extent2D minImageExtent;
    private final Extent2D maxImageExtent;
    private final int maxImageArrayLayers;
    private final Set<SurfaceTransform> supportedTransforms;
    private final SurfaceTransform currentTransform;
    private final Set<CompositeAlpha> supportedCompositeAlpha;
    private final Set<ImageUsage> supportedUsageFlags;

    public SurfaceCapabilities(final VkSurfaceCapabilitiesKHR vkSurfaceCapabilitiesKHR) {
        this(
                vkSurfaceCapabilitiesKHR.minImageCount(),
                vkSurfaceCapabilitiesKHR.maxImageCount(),
                new Extent2D(vkSurfaceCapabilitiesKHR.currentExtent()),
                new Extent2D(vkSurfaceCapabilitiesKHR.minImageExtent()),
                new Extent2D(vkSurfaceCapabilitiesKHR.maxImageExtent()),
                vkSurfaceCapabilitiesKHR.maxImageArrayLayers(),
                Maskable.fromBitMask(vkSurfaceCapabilitiesKHR.supportedTransforms(), SurfaceTransform.class),
                Maskable.fromBit(vkSurfaceCapabilitiesKHR.currentTransform(), SurfaceTransform.class),
                Maskable.fromBitMask(vkSurfaceCapabilitiesKHR.supportedCompositeAlpha(), CompositeAlpha.class),
                Maskable.fromBitMask(vkSurfaceCapabilitiesKHR.supportedUsageFlags(), ImageUsage.class)
        );
    }

    public SurfaceCapabilities(final int minImageCount,
                               final int maxImageCount,
                               final Extent2D currentExtent,
                               final Extent2D minImageExtent,
                               final Extent2D maxImageExtent,
                               final int maxImageArrayLayers,
                               final Set<SurfaceTransform> supportedTransforms,
                               final SurfaceTransform currentTransform,
                               final Set<CompositeAlpha> supportedCompositeAlpha,
                               final Set<ImageUsage> supportedUsageFlags) {
        this.minImageCount = minImageCount;
        this.maxImageCount = maxImageCount;
        this.currentExtent = currentExtent;
        this.minImageExtent = minImageExtent;
        this.maxImageExtent = maxImageExtent;
        this.maxImageArrayLayers = maxImageArrayLayers;
        this.supportedTransforms = supportedTransforms;
        this.currentTransform = currentTransform;
        this.supportedCompositeAlpha = supportedCompositeAlpha;
        this.supportedUsageFlags = supportedUsageFlags;
    }

    public int getMinImageCount() {
        return minImageCount;
    }

    public int getMaxImageCount() {
        return maxImageCount;
    }

    public Extent2D getCurrentExtent() {
        return currentExtent;
    }

    public Extent2D getMinImageExtent() {
        return minImageExtent;
    }

    public Extent2D getMaxImageExtent() {
        return maxImageExtent;
    }

    public int getMaxImageArrayLayers() {
        return maxImageArrayLayers;
    }

    public Set<SurfaceTransform> getSupportedTransforms() {
        return supportedTransforms;
    }

    public SurfaceTransform getCurrentTransform() {
        return currentTransform;
    }

    public Set<CompositeAlpha> getSupportedCompositeAlpha() {
        return supportedCompositeAlpha;
    }

    public Set<ImageUsage> getSupportedUsageFlags() {
        return supportedUsageFlags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SurfaceCapabilities)) {
            return false;
        }

        SurfaceCapabilities that = (SurfaceCapabilities) o;

        return minImageCount == that.minImageCount &&
                maxImageCount == that.maxImageCount &&
                maxImageArrayLayers == that.maxImageArrayLayers &&
                Objects.equals(currentExtent, that.currentExtent) &&
                Objects.equals(minImageExtent, that.minImageExtent) &&
                Objects.equals(maxImageExtent, that.maxImageExtent) &&
                Objects.equals(supportedTransforms, that.supportedTransforms) &&
                currentTransform == that.currentTransform &&
                Objects.equals(supportedCompositeAlpha, that.supportedCompositeAlpha) &&
                Objects.equals(supportedUsageFlags, that.supportedUsageFlags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                minImageCount,
                maxImageCount,
                currentExtent,
                minImageExtent,
                maxImageExtent,
                maxImageArrayLayers,
                supportedTransforms,
                currentTransform,
                supportedCompositeAlpha,
                supportedUsageFlags
        );
    }
}
