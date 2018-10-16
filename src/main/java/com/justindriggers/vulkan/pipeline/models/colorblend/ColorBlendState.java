package com.justindriggers.vulkan.pipeline.models.colorblend;

import java.util.List;

public class ColorBlendState {

    private final boolean isLogicOpEnabled;
    private final LogicalOperation logicalOperation;
    private final List<ColorBlendAttachmentState> attachments;
    private final float[] blendConstants;

    public ColorBlendState(final boolean isLogicOpEnabled,
                           final LogicalOperation logicalOperation,
                           final List<ColorBlendAttachmentState> attachments,
                           final float[] blendConstants) {
        this.isLogicOpEnabled = isLogicOpEnabled;
        this.logicalOperation = logicalOperation;
        this.attachments = attachments;
        this.blendConstants = blendConstants;
    }

    public boolean isLogicOpEnabled() {
        return isLogicOpEnabled;
    }

    public LogicalOperation getLogicalOperation() {
        return logicalOperation;
    }

    public List<ColorBlendAttachmentState> getAttachments() {
        return attachments;
    }

    public float[] getBlendConstants() {
        return blendConstants;
    }
}
