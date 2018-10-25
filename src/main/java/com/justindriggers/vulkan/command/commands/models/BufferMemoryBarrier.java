package com.justindriggers.vulkan.command.commands.models;

import com.justindriggers.vulkan.buffer.Buffer;
import com.justindriggers.vulkan.models.Access;
import com.justindriggers.vulkan.queue.QueueFamily;

import java.util.Set;

public class BufferMemoryBarrier {

    private final Set<Access> sourceAccess;
    private final Set<Access> destinationAccess;
    private final QueueFamily sourceQueueFamily;
    private final QueueFamily destinationQueueFamily;
    private final Buffer buffer;
    private final long offset;
    private final long size;

    public BufferMemoryBarrier(final Set<Access> sourceAccess,
                               final Set<Access> destinationAccess,
                               final QueueFamily sourceQueueFamily,
                               final QueueFamily destinationQueueFamily,
                               final Buffer buffer,
                               final long offset,
                               final long size) {
        this.sourceAccess = sourceAccess;
        this.destinationAccess = destinationAccess;
        this.sourceQueueFamily = sourceQueueFamily;
        this.destinationQueueFamily = destinationQueueFamily;
        this.buffer = buffer;
        this.offset = offset;
        this.size = size;
    }

    public Set<Access> getSourceAccess() {
        return sourceAccess;
    }

    public Set<Access> getDestinationAccess() {
        return destinationAccess;
    }

    public QueueFamily getSourceQueueFamily() {
        return sourceQueueFamily;
    }

    public QueueFamily getDestinationQueueFamily() {
        return destinationQueueFamily;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }
}
