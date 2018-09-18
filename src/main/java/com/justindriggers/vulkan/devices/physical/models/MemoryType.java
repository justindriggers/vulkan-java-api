package com.justindriggers.vulkan.devices.physical.models;

import java.util.Objects;
import java.util.Set;

public class MemoryType {

    private final int index;
    private final Set<MemoryProperty> properties;
    private final MemoryHeap heap;

    public MemoryType(final int index,
                      final Set<MemoryProperty> properties,
                      final MemoryHeap heap) {
        this.index = index;
        this.properties = properties;
        this.heap = heap;
    }

    public int getIndex() {
        return index;
    }

    public Set<MemoryProperty> getProperties() {
        return properties;
    }

    public MemoryHeap getHeap() {
        return heap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MemoryType that = (MemoryType) o;

        return index == that.index
                && Objects.equals(properties, that.properties)
                && Objects.equals(heap, that.heap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                index,
                properties,
                heap
        );
    }
}
