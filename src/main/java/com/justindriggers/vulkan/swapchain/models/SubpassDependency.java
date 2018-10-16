package com.justindriggers.vulkan.swapchain.models;

import com.justindriggers.vulkan.models.Access;
import com.justindriggers.vulkan.pipeline.models.PipelineStage;

import java.util.Set;

public class SubpassDependency {

    private final int sourceSubpass;
    private final int destinationSubpass;
    private final Set<PipelineStage> sourceStages;
    private final Set<PipelineStage> destinationStages;
    private final Set<Access> sourceAccessFlags;
    private final Set<Access> destinationAccessFlags;

    public SubpassDependency(final int sourceSubpass,
                             final int destinationSubpass,
                             final Set<PipelineStage> sourceStages,
                             final Set<PipelineStage> destinationStages,
                             final Set<Access> sourceAccessFlags,
                             final Set<Access> destinationAccessFlags) {
        this.sourceSubpass = sourceSubpass;
        this.destinationSubpass = destinationSubpass;
        this.sourceStages = sourceStages;
        this.destinationStages = destinationStages;
        this.sourceAccessFlags = sourceAccessFlags;
        this.destinationAccessFlags = destinationAccessFlags;
    }

    public int getSourceSubpass() {
        return sourceSubpass;
    }

    public int getDestinationSubpass() {
        return destinationSubpass;
    }

    public Set<PipelineStage> getSourceStages() {
        return sourceStages;
    }

    public Set<PipelineStage> getDestinationStages() {
        return destinationStages;
    }

    public Set<Access> getSourceAccessFlags() {
        return sourceAccessFlags;
    }

    public Set<Access> getDestinationAccessFlags() {
        return destinationAccessFlags;
    }
}
