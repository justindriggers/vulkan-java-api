package com.justindriggers.vulkan.devices.physical.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VkMemoryHeap;

import java.util.Objects;
import java.util.Set;

public class MemoryHeap {

    private final long size;
    private final Set<MemoryHeapFlag> flags;

    public MemoryHeap(final VkMemoryHeap vkMemoryHeap) {
        size = vkMemoryHeap.size();
        flags = Maskable.fromBitMask(vkMemoryHeap.flags(), MemoryHeapFlag.class);
    }

    public long getSize() {
        return size;
    }

    public Set<MemoryHeapFlag> getFlags() {
        return flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MemoryHeap that = (MemoryHeap) o;

        return size == that.size
                && Objects.equals(flags, that.flags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                size,
                flags
        );
    }
}
