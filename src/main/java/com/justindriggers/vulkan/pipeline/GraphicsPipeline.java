package com.justindriggers.vulkan.pipeline;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.Extent2D;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.pipeline.models.assembly.InputAssemblyState;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputAttribute;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputBinding;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputState;
import com.justindriggers.vulkan.pipeline.shader.ShaderStage;
import com.justindriggers.vulkan.swapchain.RenderPass;
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
import org.lwjgl.vulkan.VkVertexInputAttributeDescription;
import org.lwjgl.vulkan.VkVertexInputBindingDescription;
import org.lwjgl.vulkan.VkViewport;

import java.nio.LongBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import static org.lwjgl.vulkan.VK10.VK_FRONT_FACE_COUNTER_CLOCKWISE;
import static org.lwjgl.vulkan.VK10.VK_NULL_HANDLE;
import static org.lwjgl.vulkan.VK10.VK_POLYGON_MODE_FILL;
import static org.lwjgl.vulkan.VK10.VK_SAMPLE_COUNT_1_BIT;
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
                            final List<ShaderStage> stages,
                            final VertexInputState vertexInputState,
                            final InputAssemblyState inputAssemblyState,
                            final Extent2D imageExtent,
                            final RenderPass renderPass,
                            final PipelineLayout pipelineLayout) {
        super(createGraphicsPipeline(device, stages, vertexInputState, inputAssemblyState, imageExtent, renderPass,
                pipelineLayout));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyPipeline(device.unwrap(), address, null);
    }

    private static long createGraphicsPipeline(final LogicalDevice device,
                                               final List<ShaderStage> stages,
                                               final VertexInputState vertexInputState,
                                               final InputAssemblyState inputAssemblyState,
                                               final Extent2D imageExtent,
                                               final RenderPass renderPass,
                                               final PipelineLayout pipelineLayout) {
        final long result;

        final LongBuffer graphicsPipeline = memAllocLong(1);

        final VkPipelineShaderStageCreateInfo.Buffer shaderStageCreateInfos = VkPipelineShaderStageCreateInfo.calloc(stages.size());

        stages.forEach(stage -> {
            final VkPipelineShaderStageCreateInfo stageCreateInfo = VkPipelineShaderStageCreateInfo.calloc()
                    .sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO)
                    .stage(Maskable.toBit(stage.getType()))
                    .module(stage.getModule().getAddress())
                    .pName(memUTF8(stage.getEntryPoint()));

            shaderStageCreateInfos.put(stageCreateInfo);
        });

        shaderStageCreateInfos.flip();

        // Bindings
        final List<VertexInputBinding> vertexInputBindings = Optional.ofNullable(vertexInputState.getVertexInputBindings())
                .orElseGet(Collections::emptyList);

        final VkVertexInputBindingDescription.Buffer vertexInputBindingDescriptions = VkVertexInputBindingDescription.calloc(vertexInputBindings.size());

        vertexInputBindings.forEach(vertexInputBinding -> {
            final VkVertexInputBindingDescription vertexInputBindingDescription = VkVertexInputBindingDescription.calloc()
                    .binding(vertexInputBinding.getBinding())
                    .stride(vertexInputBinding.getStride())
                    .inputRate(HasValue.getValue(vertexInputBinding.getRate()));

            vertexInputBindingDescriptions.put(vertexInputBindingDescription);
        });

        vertexInputBindingDescriptions.flip();

        // Attributes
        final List<VertexInputAttribute> vertexInputAttributes = Optional.ofNullable(vertexInputState.getVertexInputAttributes())
                .orElseGet(Collections::emptyList);

        final VkVertexInputAttributeDescription.Buffer vertexInputAttributeDescriptions = VkVertexInputAttributeDescription.calloc(vertexInputAttributes.size());

        vertexInputAttributes.forEach(vertexInputAttribute -> {
            final VkVertexInputAttributeDescription vertexInputAttributeDescription = VkVertexInputAttributeDescription.calloc()
                    .binding(vertexInputAttribute.getBinding())
                    .location(vertexInputAttribute.getLocation())
                    .format(HasValue.getValue(vertexInputAttribute.getFormat()))
                    .offset(vertexInputAttribute.getOffset());

            vertexInputAttributeDescriptions.put(vertexInputAttributeDescription);
        });

        vertexInputAttributeDescriptions.flip();

        final VkPipelineVertexInputStateCreateInfo vertexInputStateCreateInfo = VkPipelineVertexInputStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO)
                .pVertexBindingDescriptions(vertexInputBindingDescriptions)
                .pVertexAttributeDescriptions(vertexInputAttributeDescriptions);

        final VkPipelineInputAssemblyStateCreateInfo inputAssemblyStateCreateInfo = VkPipelineInputAssemblyStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO)
                .topology(HasValue.getValue(inputAssemblyState.getTopology()))
                .primitiveRestartEnable(inputAssemblyState.isPrimitiveRestartEnabled());

        final VkViewport.Buffer viewport = VkViewport.calloc(1)
                .x(0)
                .y(imageExtent.getHeight())
                .width(imageExtent.getWidth())
                .height(-imageExtent.getHeight())
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
                .frontFace(VK_FRONT_FACE_COUNTER_CLOCKWISE)
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
            vertexInputBindingDescriptions.free();
            vertexInputAttributeDescriptions.free();
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
