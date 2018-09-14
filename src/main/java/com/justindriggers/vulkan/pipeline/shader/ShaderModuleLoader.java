package com.justindriggers.vulkan.pipeline.shader;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Optional;

public class ShaderModuleLoader {

    public ShaderModule loadFromFile(final LogicalDevice device, final String resourcePath) {
        final ShaderModule result;

        final File file = Optional.of(Thread.currentThread())
                .map(Thread::getContextClassLoader)
                .map(classLoader -> classLoader.getResource(resourcePath))
                .map(URL::getFile)
                .map(File::new)
                .orElseThrow(() -> new IllegalArgumentException("Unable to open file at " + resourcePath));

        try (final FileInputStream fileInputStream = new FileInputStream(file)) {
            final FileChannel channel = fileInputStream.getChannel();
            final ByteBuffer shaderCode = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

            result = new ShaderModule(device, shaderCode);
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to load shader module", e);
        }

        return result;
    }
}
