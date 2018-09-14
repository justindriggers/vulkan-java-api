package com.justindriggers.vulkan.instance;

import com.justindriggers.vulkan.instance.models.MessageSeverity;
import com.justindriggers.vulkan.instance.models.MessageType;
import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.VkDebugUtilsMessengerCallbackDataEXT;
import org.lwjgl.vulkan.VkDebugUtilsMessengerCallbackEXTI;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.vulkan.VK10.VK_FALSE;

public class DebugLogger implements VkDebugUtilsMessengerCallbackEXTI {

    private static final Logger LOGGER = Logger.getLogger(DebugLogger.class.getName());

    /**
     * Creates an instance of {@link VkDebugUtilsMessengerCallbackDataEXT} by referencing the callback data pointer.
     *
     * Since the struct was allocated outside of the scope of this method, and simply referenced by address, it should
     * not be destroyed here.
     */
    @SuppressWarnings("squid:S2095")
    @Override
    public int invoke(final int messageSeverity, final int messageType, final long callbackDataPointer,
                      final long userDataPointer) {
        final Level level = Maskable.fromBit(messageSeverity, MessageSeverity.class).getLevel();
        final MessageType type = Maskable.fromBit(messageType, MessageType.class);

        final VkDebugUtilsMessengerCallbackDataEXT callbackData = VkDebugUtilsMessengerCallbackDataEXT.create(
                callbackDataPointer);

        LOGGER.log(level, () -> String.format("[%s] %s", type, callbackData.pMessageString()));

        return VK_FALSE;
    }
}
