package com.justindriggers.vulkan.models.pointers;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DisposablePointer extends Pointer implements Disposable {

    private final AtomicBoolean isDisposed;

    protected DisposablePointer(final long handle) {
        super(handle);

        isDisposed = new AtomicBoolean(false);
    }

    @Override
    public long getAddress() {
        return Optional.of(super.getAddress())
                .filter(address -> !isDisposed.get())
                .orElseThrow(() -> new IllegalStateException("Underlying instance has already been destroyed"));
    }

    @Override
    public void close() {
        if (!isDisposed.getAndSet(true)) {
            dispose(super.getAddress());
        }
    }

    protected abstract void dispose(final long address);
}
