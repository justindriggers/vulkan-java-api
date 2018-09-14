package com.justindriggers.vulkan.models.pointers;

import java.util.function.Function;

public abstract class ReferencePointer<T> extends Pointer implements Container<T> {

    private final T reference;

    protected ReferencePointer(final T reference, Function<T, Long> pointerFunction) {
        super(pointerFunction.apply(reference));
        this.reference = reference;
    }

    @Override
    public T unwrap() {
        return reference;
    }
}
