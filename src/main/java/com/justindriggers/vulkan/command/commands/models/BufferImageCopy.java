package com.justindriggers.vulkan.command.commands.models;

import com.justindriggers.vulkan.models.Extent3D;
import com.justindriggers.vulkan.models.Offset3D;

public class BufferImageCopy {

    private final long bufferOffset;
    private final int bufferRowLength;
    private final int bufferImageHeight;
    private final ImageSubresourceLayers imageSubresource;
    private final Offset3D imageOffset;
    private final Extent3D imageExtent;

    public BufferImageCopy(final long bufferOffset,
                           final int bufferRowLength,
                           final int bufferImageHeight,
                           final ImageSubresourceLayers imageSubresource,
                           final Offset3D imageOffset,
                           final Extent3D imageExtent) {
        this.bufferOffset = bufferOffset;
        this.bufferRowLength = bufferRowLength;
        this.bufferImageHeight = bufferImageHeight;
        this.imageSubresource = imageSubresource;
        this.imageOffset = imageOffset;
        this.imageExtent = imageExtent;
    }

    public long getBufferOffset() {
        return bufferOffset;
    }

    public int getBufferRowLength() {
        return bufferRowLength;
    }

    public int getBufferImageHeight() {
        return bufferImageHeight;
    }

    public ImageSubresourceLayers getImageSubresource() {
        return imageSubresource;
    }

    public Offset3D getImageOffset() {
        return imageOffset;
    }

    public Extent3D getImageExtent() {
        return imageExtent;
    }
}
