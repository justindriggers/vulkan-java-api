package com.justindriggers.vulkan.instance.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.EXTDebugUtils;

import java.util.logging.Level;

public enum MessageSeverity implements Maskable {

    VERBOSE(EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_SEVERITY_VERBOSE_BIT_EXT, Level.FINE),
    INFO(EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_SEVERITY_INFO_BIT_EXT, Level.INFO),
    WARNING(EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT, Level.WARNING),
    ERROR(EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT, Level.SEVERE);

    private final int bit;
    private final Level level;

    MessageSeverity(final int bit,
                          final Level level) {
        this.bit = bit;
        this.level = level;
    }

    @Override
    public int getBitValue() {
        return bit;
    }

    public Level getLevel() {
        return level;
    }
}
