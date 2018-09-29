package com.justindriggers.vulkan.surface.models;

import com.justindriggers.vulkan.models.ColorFormat;
import com.justindriggers.vulkan.models.ColorSpace;
import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;

import java.util.Objects;

public class SurfaceFormat {

    private final ColorFormat format;
    private final ColorSpace colorSpace;

    public SurfaceFormat(final VkSurfaceFormatKHR vkSurfaceFormatKHR) {
        this(
                HasValue.getByValue(vkSurfaceFormatKHR.format(), ColorFormat.class),
                HasValue.getByValue(vkSurfaceFormatKHR.colorSpace(), ColorSpace.class)
        );
    }

    public SurfaceFormat(final ColorFormat format,
                         final ColorSpace colorSpace) {
        this.format = format;
        this.colorSpace = colorSpace;
    }

    public ColorFormat getFormat() {
        return format;
    }

    public ColorSpace getColorSpace() {
        return colorSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SurfaceFormat)) {
            return false;
        }

        SurfaceFormat that = (SurfaceFormat) o;

        return format == that.format &&
                colorSpace == that.colorSpace;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                format,
                colorSpace
        );
    }
}
