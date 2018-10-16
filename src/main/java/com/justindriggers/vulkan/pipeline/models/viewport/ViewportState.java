package com.justindriggers.vulkan.pipeline.models.viewport;

import com.justindriggers.vulkan.models.Rect2D;

import java.util.List;

public class ViewportState {

    private final List<Viewport> viewports;
    private final List<Rect2D> scissors;

    public ViewportState(final List<Viewport> viewports,
                         final List<Rect2D> scissors) {
        this.viewports = viewports;
        this.scissors = scissors;
    }

    public List<Viewport> getViewports() {
        return viewports;
    }

    public List<Rect2D> getScissors() {
        return scissors;
    }
}
