package com.justindriggers.vulkan.models.pointers;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public abstract class DisposableReferencePointer<T> extends ReferencePointer<T> implements Disposable {

    private final AtomicBoolean isDisposed;

    protected DisposableReferencePointer(final T reference, final Function<T, Long> pointerFunction) {
        super(reference, pointerFunction);

        isDisposed = new AtomicBoolean(false);
    }

    @Override
    public long getAddress() {
        return Optional.of(super.getAddress())
                .filter(address -> !isDisposed.get())
                .orElseThrow(() -> new IllegalStateException("Underlying instance has already been destroyed"));
    }

    @Override
    public T unwrap() {
        return Optional.of(super.unwrap())
                .filter(ref -> !isDisposed.get())
                .orElseThrow(() -> new IllegalStateException("Underlying instance has already been disposed"));
    }

    @Override
    public void close() {
        if (!isDisposed.getAndSet(true)) {
            dispose(super.unwrap(), super.getAddress());
        }
    }

    protected abstract void dispose(final T reference, final long address);
}
