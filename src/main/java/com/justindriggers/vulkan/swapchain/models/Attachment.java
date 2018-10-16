package com.justindriggers.vulkan.swapchain.models;

import com.justindriggers.vulkan.image.models.ImageLayout;
import com.justindriggers.vulkan.models.Format;
import com.justindriggers.vulkan.models.SampleCount;

import java.util.Set;

public abstract class Attachment {

    private final Format format;
    private final Set<SampleCount> sampleCounts;
    private final AttachmentLoadOperation loadOperation;
    private final AttachmentStoreOperation storeOperation;
    private final AttachmentLoadOperation stencilLoadOperation;
    private final AttachmentStoreOperation stencilStoreOperation;
    private final ImageLayout initialLayout;
    private final ImageLayout finalLayout;

    Attachment(final Format format,
               final Set<SampleCount> sampleCounts,
               final AttachmentLoadOperation loadOperation,
               final AttachmentStoreOperation storeOperation,
               final AttachmentLoadOperation stencilLoadOperation,
               final AttachmentStoreOperation stencilStoreOperation,
               final ImageLayout initialLayout,
               final ImageLayout finalLayout) {
        this.format = format;
        this.sampleCounts = sampleCounts;
        this.loadOperation = loadOperation;
        this.storeOperation = storeOperation;
        this.stencilLoadOperation = stencilLoadOperation;
        this.stencilStoreOperation = stencilStoreOperation;
        this.initialLayout = initialLayout;
        this.finalLayout = finalLayout;
    }

    public Format getFormat() {
        return format;
    }

    public Set<SampleCount> getSampleCounts() {
        return sampleCounts;
    }

    public AttachmentLoadOperation getLoadOperation() {
        return loadOperation;
    }

    public AttachmentStoreOperation getStoreOperation() {
        return storeOperation;
    }

    public AttachmentLoadOperation getStencilLoadOperation() {
        return stencilLoadOperation;
    }

    public AttachmentStoreOperation getStencilStoreOperation() {
        return stencilStoreOperation;
    }

    public ImageLayout getInitialLayout() {
        return initialLayout;
    }

    public ImageLayout getFinalLayout() {
        return finalLayout;
    }

    public abstract ImageLayout getAttachmentLayout();
}
