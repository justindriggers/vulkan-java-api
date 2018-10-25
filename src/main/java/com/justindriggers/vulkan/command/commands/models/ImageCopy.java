package com.justindriggers.vulkan.command.commands.models;

import com.justindriggers.vulkan.models.Extent3D;
import com.justindriggers.vulkan.models.Offset3D;

public class ImageCopy {

    private final ImageSubresourceLayers sourceSubresource;
    private final Offset3D sourceOffset;
    private final ImageSubresourceLayers destinationSubresource;
    private final Offset3D destinationOffset;
    private final Extent3D extent;

    public ImageCopy(final ImageSubresourceLayers sourceSubresource,
                     final Offset3D sourceOffset,
                     final ImageSubresourceLayers destinationSubresource,
                     final Offset3D destinationOffset,
                     final Extent3D extent) {
        this.sourceSubresource = sourceSubresource;
        this.sourceOffset = sourceOffset;
        this.destinationSubresource = destinationSubresource;
        this.destinationOffset = destinationOffset;
        this.extent = extent;
    }

    public ImageSubresourceLayers getSourceSubresource() {
        return sourceSubresource;
    }

    public Offset3D getSourceOffset() {
        return sourceOffset;
    }

    public ImageSubresourceLayers getDestinationSubresource() {
        return destinationSubresource;
    }

    public Offset3D getDestinationOffset() {
        return destinationOffset;
    }

    public Extent3D getExtent() {
        return extent;
    }
}
