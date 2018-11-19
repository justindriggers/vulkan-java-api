package com.justindriggers.vulkan.pipeline.shader;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Optional;

public class ShaderModuleLoader {

    public ShaderModule loadFromFile(final LogicalDevice device, final String resourcePath) {
        final ShaderModule result;

        final URL resourceUrl = Optional.of(getClass())
                .map(Class::getClassLoader)
                .map(classLoader -> classLoader.getResource(resourcePath))
                .orElseThrow(() -> new IllegalArgumentException("Unable to open file at " + resourcePath));

        try (final InputStream inputStream = resourceUrl.openStream();
             final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            boolean hasMore = true;

            do {
                final int b = inputStream.read();

                if (b == -1) {
                    hasMore = false;
                } else {
                    byteArrayOutputStream.write(b);
                }
            } while (hasMore);

            final byte[] code = byteArrayOutputStream.toByteArray();

            final ByteBuffer shaderCode = ByteBuffer.allocateDirect(code.length);
            shaderCode.put(code);
            shaderCode.flip();

            result = new ShaderModule(device, shaderCode);
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to load shader module", e);
        }

        return result;
    }
}
