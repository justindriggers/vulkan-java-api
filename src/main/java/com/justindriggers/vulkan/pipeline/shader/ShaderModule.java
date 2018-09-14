package com.justindriggers.vulkan.pipeline.shader;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import org.lwjgl.vulkan.VkShaderModuleCreateInfo;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateShaderModule;
import static org.lwjgl.vulkan.VK10.vkDestroyShaderModule;

public class ShaderModule extends DisposablePointer {

    private final LogicalDevice device;

    public ShaderModule(final LogicalDevice device,
                        final ByteBuffer shaderCode) {
        super(createShaderModule(device, shaderCode));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyShaderModule(device.unwrap(), address, null);
    }

    private static long createShaderModule(final LogicalDevice device,
                                           final ByteBuffer shaderCode) {
        final long result;

        final LongBuffer shaderModule = memAllocLong(1);

        final VkShaderModuleCreateInfo shaderModuleCreateInfo = VkShaderModuleCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO)
                .pCode(shaderCode);

        try {
            VulkanFunction.execute(() -> vkCreateShaderModule(device.unwrap(), shaderModuleCreateInfo, null,
                    shaderModule));

            result = shaderModule.get(0);
        } finally {
            memFree(shaderModule);

            shaderModuleCreateInfo.free();
        }

        return result;
    }
}
