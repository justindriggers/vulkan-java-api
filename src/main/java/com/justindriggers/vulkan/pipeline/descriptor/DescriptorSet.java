package com.justindriggers.vulkan.pipeline.descriptor;

import com.justindriggers.vulkan.buffer.Buffer;
import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.pointers.Pointer;
import com.justindriggers.vulkan.pipeline.descriptor.models.DescriptorType;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET;
import static org.lwjgl.vulkan.VK10.VK_WHOLE_SIZE;
import static org.lwjgl.vulkan.VK10.vkUpdateDescriptorSets;

public class DescriptorSet extends Pointer {

    private final LogicalDevice device;
    private final DescriptorType descriptorType;

    DescriptorSet(final LogicalDevice device,
                  final long handle,
                  final DescriptorType descriptorType) {
        super(handle);
        this.device = device;
        this.descriptorType = descriptorType;
    }

    public void update(final Buffer buffer) {
        final VkDescriptorBufferInfo.Buffer descriptorBufferInfo = VkDescriptorBufferInfo.calloc(1)
                .buffer(buffer.getAddress())
                .offset(0L)
                .range(VK_WHOLE_SIZE);

        final VkWriteDescriptorSet.Buffer writeDescriptorSet = VkWriteDescriptorSet.calloc(1)
                .sType(VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET)
                .dstSet(getAddress())
                .dstBinding(0)
                .dstArrayElement(0)
                .descriptorType(HasValue.getValue(descriptorType))
                .pBufferInfo(descriptorBufferInfo);

        try {
            vkUpdateDescriptorSets(device.unwrap(), writeDescriptorSet, null);
        } finally {
            descriptorBufferInfo.free();
            writeDescriptorSet.free();
        }
    }
}
