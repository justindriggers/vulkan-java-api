package com.justindriggers.vulkan.command.commands.models;

import com.justindriggers.vulkan.models.Access;

import java.util.Set;

public class MemoryBarrier {

    private final Set<Access> sourceAccess;
    private final Set<Access> destinationAccess;

    public MemoryBarrier(final Set<Access> sourceAccess,
                         final Set<Access> destinationAccess) {
        this.sourceAccess = sourceAccess;
        this.destinationAccess = destinationAccess;
    }

    public Set<Access> getSourceAccess() {
        return sourceAccess;
    }

    public Set<Access> getDestinationAccess() {
        return destinationAccess;
    }
}
