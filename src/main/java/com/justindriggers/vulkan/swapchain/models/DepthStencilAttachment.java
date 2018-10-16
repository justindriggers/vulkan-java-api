package com.justindriggers.vulkan.swapchain.models;

import com.justindriggers.vulkan.image.models.ImageLayout;
import com.justindriggers.vulkan.models.Format;
import com.justindriggers.vulkan.models.SampleCount;

import java.util.Set;

public class DepthStencilAttachment extends Attachment {

    public DepthStencilAttachment(final Format format,
                                  final Set<SampleCount> sampleCounts,
                                  final AttachmentLoadOperation loadOperation,
                                  final AttachmentStoreOperation storeOperation,
                                  final AttachmentLoadOperation stencilLoadOperation,
                                  final AttachmentStoreOperation stencilStoreOperation,
                                  final ImageLayout initialLayout,
                                  final ImageLayout finalLayout) {
        super(format, sampleCounts, loadOperation, storeOperation, stencilLoadOperation, stencilStoreOperation,
                initialLayout, finalLayout);
    }

    @Override
    public ImageLayout getAttachmentLayout() {
        return ImageLayout.DEPTH_STENCIL_ATTACHMENT_OPTIMAL;
    }
}
