package com.justindriggers.vulkan.pipeline;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Extent2D;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.pipeline.shader.ShaderModule;
import org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo;
import org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState;
import org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineViewportStateCreateInfo;
import org.lwjgl.vulkan.VkRect2D;
import org.lwjgl.vulkan.VkViewport;

import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.system.MemoryUtil.memUTF8;
import static org.lwjgl.vulkan.VK10.VK_BLEND_FACTOR_ONE;
import static org.lwjgl.vulkan.VK10.VK_BLEND_FACTOR_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.vulkan.VK10.VK_BLEND_FACTOR_SRC_ALPHA;
import static org.lwjgl.vulkan.VK10.VK_BLEND_FACTOR_ZERO;
import static org.lwjgl.vulkan.VK10.VK_BLEND_OP_ADD;
import static org.lwjgl.vulkan.VK10.VK_COLOR_COMPONENT_A_BIT;
import static org.lwjgl.vulkan.VK10.VK_COLOR_COMPONENT_B_BIT;
import static org.lwjgl.vulkan.VK10.VK_COLOR_COMPONENT_G_BIT;
import static org.lwjgl.vulkan.VK10.VK_COLOR_COMPONENT_R_BIT;
import static org.lwjgl.vulkan.VK10.VK_CULL_MODE_BACK_BIT;
import static org.lwjgl.vulkan.VK10.VK_FRONT_FACE_CLOCKWISE;
import static org.lwjgl.vulkan.VK10.VK_NULL_HANDLE;
import static org.lwjgl.vulkan.VK10.VK_POLYGON_MODE_FILL;
import static org.lwjgl.vulkan.VK10.VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST;
import static org.lwjgl.vulkan.VK10.VK_SAMPLE_COUNT_1_BIT;
import static org.lwjgl.vulkan.VK10.VK_SHADER_STAGE_FRAGMENT_BIT;
import static org.lwjgl.vulkan.VK10.VK_SHADER_STAGE_VERTEX_BIT;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateGraphicsPipelines;
import static org.lwjgl.vulkan.VK10.vkDestroyPipeline;

public class GraphicsPipeline extends DisposablePointer {

    private final LogicalDevice device;

    public GraphicsPipeline(final LogicalDevice device,
                            final ShaderModule vertexShader,
                            final ShaderModule fragmentShader,
                            final Extent2D imageExtent,
                            final RenderPass renderPass,
                            final PipelineLayout pipelineLayout) {
        super(createGraphicsPipeline(device, vertexShader, fragmentShader, imageExtent, renderPass, pipelineLayout));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyPipeline(device.unwrap(), address, null);
    }

    private static long createGraphicsPipeline(final LogicalDevice device,
                                               final ShaderModule vertexShader,
                                               final ShaderModule fragmentShader,
                                               final Extent2D imageExtent,
                                               final RenderPass renderPass,
                                               final PipelineLayout pipelineLayout) {
        final long result;

        final LongBuffer graphicsPipeline = memAllocLong(1);

        final VkPipelineShaderStageCreateInfo vertexShaderStageCreateInfo = VkPipelineShaderStageCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO)
                .stage(VK_SHADER_STAGE_VERTEX_BIT)
                .module(vertexShader.getAddress())
                .pName(memUTF8("main"));

        final VkPipelineShaderStageCreateInfo fragmentShaderStageCreateInfo = VkPipelineShaderStageCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO)
                .stage(VK_SHADER_STAGE_FRAGMENT_BIT)
                .module(fragmentShader.getAddress())
                .pName(memUTF8("main"));

        final VkPipelineShaderStageCreateInfo.Buffer shaderStageCreateInfos = VkPipelineShaderStageCreateInfo.calloc(2)
                .put(vertexShaderStageCreateInfo)
                .put(fragmentShaderStageCreateInfo)
                .flip();

        final VkPipelineVertexInputStateCreateInfo vertexInputStateCreateInfo = VkPipelineVertexInputStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO);

        final VkPipelineInputAssemblyStateCreateInfo inputAssemblyStateCreateInfo = VkPipelineInputAssemblyStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO)
                .topology(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST)
                .primitiveRestartEnable(false);

        final VkViewport.Buffer viewport = VkViewport.calloc(1)
                .x(0)
                .y(0)
                .width(imageExtent.getWidth())
                .height(imageExtent.getHeight())
                .minDepth(0.0f)
                .maxDepth(1.0f);

        final VkRect2D.Buffer scissors = VkRect2D.calloc(1);

        scissors.extent()
                .width(imageExtent.getWidth())
                .height(imageExtent.getHeight());

        scissors.offset()
                .x(0)
                .y(0);

        final VkPipelineViewportStateCreateInfo viewportStateCreateInfo = VkPipelineViewportStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO)
                .pViewports(viewport)
                .pScissors(scissors);

        final VkPipelineRasterizationStateCreateInfo rasterizationStateCreateInfo = VkPipelineRasterizationStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO)
                .depthClampEnable(false)
                .rasterizerDiscardEnable(false)
                .polygonMode(VK_POLYGON_MODE_FILL)
                .lineWidth(1.0f)
                .cullMode(VK_CULL_MODE_BACK_BIT)
                .frontFace(VK_FRONT_FACE_CLOCKWISE)
                .depthBiasEnable(false);

        final VkPipelineMultisampleStateCreateInfo multisampleStateCreateInfo = VkPipelineMultisampleStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO)
                .sampleShadingEnable(false)
                .rasterizationSamples(VK_SAMPLE_COUNT_1_BIT);

        final VkPipelineColorBlendAttachmentState.Buffer colorBlendAttachmentState = VkPipelineColorBlendAttachmentState.calloc(1)
                .colorWriteMask(VK_COLOR_COMPONENT_R_BIT | VK_COLOR_COMPONENT_G_BIT | VK_COLOR_COMPONENT_B_BIT | VK_COLOR_COMPONENT_A_BIT)
                .blendEnable(true)
                .srcColorBlendFactor(VK_BLEND_FACTOR_SRC_ALPHA)
                .dstColorBlendFactor(VK_BLEND_FACTOR_ONE_MINUS_SRC_ALPHA)
                .colorBlendOp(VK_BLEND_OP_ADD)
                .srcAlphaBlendFactor(VK_BLEND_FACTOR_ONE)
                .dstAlphaBlendFactor(VK_BLEND_FACTOR_ZERO)
                .alphaBlendOp(VK_BLEND_OP_ADD);

        final VkPipelineColorBlendStateCreateInfo colorBlendStateCreateInfo = VkPipelineColorBlendStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO)
                .logicOpEnable(false)
                .pAttachments(colorBlendAttachmentState);

        final VkGraphicsPipelineCreateInfo.Buffer graphicsPipelineCreateInfo = VkGraphicsPipelineCreateInfo.calloc(1)
                .sType(VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO)
                .pStages(shaderStageCreateInfos)
                .pVertexInputState(vertexInputStateCreateInfo)
                .pInputAssemblyState(inputAssemblyStateCreateInfo)
                .pViewportState(viewportStateCreateInfo)
                .pRasterizationState(rasterizationStateCreateInfo)
                .pMultisampleState(multisampleStateCreateInfo)
                .pDepthStencilState(null)
                .pColorBlendState(colorBlendStateCreateInfo)
                .pDynamicState(null)
                .layout(pipelineLayout.getAddress())
                .renderPass(renderPass.getAddress())
                .subpass(0);

        try {
            VulkanFunction.execute(() -> vkCreateGraphicsPipelines(device.unwrap(), VK_NULL_HANDLE,
                    graphicsPipelineCreateInfo, null, graphicsPipeline));

            result = graphicsPipeline.get(0);
        } finally {
            memFree(graphicsPipeline);

            shaderStageCreateInfos.free();
            vertexInputStateCreateInfo.free();
            inputAssemblyStateCreateInfo.free();
            viewport.free();
            scissors.free();
            viewportStateCreateInfo.free();
            rasterizationStateCreateInfo.free();
            multisampleStateCreateInfo.free();
            colorBlendAttachmentState.free();
            colorBlendStateCreateInfo.free();
            graphicsPipelineCreateInfo.free();
        }

        return result;
    }
}
