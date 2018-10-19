package com.justindriggers.vulkan.models.pointers;

import java.io.Closeable;

public interface Disposable extends Closeable {

    @Override
    void close();
}
