package com.justindriggers.vulkan.command.commands.models;

import com.justindriggers.vulkan.image.Image;
import com.justindriggers.vulkan.image.models.ImageLayout;
import com.justindriggers.vulkan.models.Access;
import com.justindriggers.vulkan.queue.QueueFamily;

import java.util.Set;

public class ImageMemoryBarrier {

    private final Set<Access> sourceAccess;
    private final Set<Access> destinationAccess;
    private final ImageLayout oldLayout;
    private final ImageLayout newLayout;
    private final QueueFamily sourceQueueFamily;
    private final QueueFamily destinationQueueFamily;
    private final Image image;
    private final ImageSubresourceRange subresourceRange;

    public ImageMemoryBarrier(final Set<Access> sourceAccess,
                              final Set<Access> destinationAccess,
                              final ImageLayout oldLayout,
                              final ImageLayout newLayout,
                              final QueueFamily sourceQueueFamily,
                              final QueueFamily destinationQueueFamily,
                              final Image image,
                              final ImageSubresourceRange subresourceRange) {
        this.sourceAccess = sourceAccess;
        this.destinationAccess = destinationAccess;
        this.oldLayout = oldLayout;
        this.newLayout = newLayout;
        this.sourceQueueFamily = sourceQueueFamily;
        this.destinationQueueFamily = destinationQueueFamily;
        this.image = image;
        this.subresourceRange = subresourceRange;
    }

    public Set<Access> getSourceAccess() {
        return sourceAccess;
    }

    public Set<Access> getDestinationAccess() {
        return destinationAccess;
    }

    public ImageLayout getOldLayout() {
        return oldLayout;
    }

    public ImageLayout getNewLayout() {
        return newLayout;
    }

    public QueueFamily getSourceQueueFamily() {
        return sourceQueueFamily;
    }

    public QueueFamily getDestinationQueueFamily() {
        return destinationQueueFamily;
    }

    public Image getImage() {
        return image;
    }

    public ImageSubresourceRange getSubresourceRange() {
        return subresourceRange;
    }
}
