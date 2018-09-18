package com.justindriggers.vulkan.buffer.models;

import com.justindriggers.vulkan.devices.physical.models.MemoryType;
import org.lwjgl.vulkan.VkMemoryRequirements;

import java.util.Objects;
import java.util.function.Predicate;

public class MemoryRequirements implements Predicate<MemoryType> {

    private final long size;
    private final long alignment;
    private final int memoryTypeBits;

    public MemoryRequirements(final VkMemoryRequirements vkMemoryRequirements) {
        size = vkMemoryRequirements.size();
        alignment = vkMemoryRequirements.alignment();
        memoryTypeBits = vkMemoryRequirements.memoryTypeBits();
    }

    @Override
    public boolean test(final MemoryType memoryType) {
        return (memoryTypeBits & (1 << memoryType.getIndex())) != 0;
    }

    public long getSize() {
        return size;
    }

    public long getAlignment() {
        return alignment;
    }

    public int getMemoryTypeBits() {
        return memoryTypeBits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MemoryRequirements that = (MemoryRequirements) o;

        return size == that.size
                && alignment == that.alignment
                && memoryTypeBits == that.memoryTypeBits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                size,
                alignment,
                memoryTypeBits
        );
    }
}
