package com.justindriggers.vulkan.pipeline;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.Rect2D;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.pipeline.models.PipelineCreateFlag;
import com.justindriggers.vulkan.pipeline.models.assembly.InputAssemblyState;
import com.justindriggers.vulkan.pipeline.models.colorblend.ColorBlendAttachmentState;
import com.justindriggers.vulkan.pipeline.models.colorblend.ColorBlendState;
import com.justindriggers.vulkan.pipeline.models.depth.DepthStencilState;
import com.justindriggers.vulkan.pipeline.models.depth.StencilOperationState;
import com.justindriggers.vulkan.pipeline.models.multisample.MultisampleState;
import com.justindriggers.vulkan.pipeline.models.rasterization.RasterizationState;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputAttribute;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputBinding;
import com.justindriggers.vulkan.pipeline.models.vertex.VertexInputState;
import com.justindriggers.vulkan.pipeline.models.viewport.Viewport;
import com.justindriggers.vulkan.pipeline.models.viewport.ViewportState;
import com.justindriggers.vulkan.pipeline.shader.ShaderStage;
import com.justindriggers.vulkan.swapchain.RenderPass;
import org.lwjgl.system.Struct;
import org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo;
import org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState;
import org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineDepthStencilStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineViewportStateCreateInfo;
import org.lwjgl.vulkan.VkRect2D;
import org.lwjgl.vulkan.VkStencilOpState;
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
import static org.lwjgl.vulkan.VK10.VK_NULL_HANDLE;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_DEPTH_STENCIL_STATE_CREATE_INFO;
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
                            final ViewportState viewportState,
                            final RasterizationState rasterizationState,
                            final MultisampleState multisampleState,
                            final DepthStencilState depthStencilState,
                            final ColorBlendState colorBlendState,
                            final RenderPass renderPass,
                            final PipelineLayout pipelineLayout,
                            final int subpassIndex,
                            final PipelineCreateFlag... flags) {
        super(createGraphicsPipeline(device, stages, vertexInputState, inputAssemblyState, viewportState,
                rasterizationState, multisampleState, depthStencilState, colorBlendState, renderPass, pipelineLayout,
                subpassIndex, flags));
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
                                               final ViewportState viewportState,
                                               final RasterizationState rasterizationState,
                                               final MultisampleState multisampleState,
                                               final DepthStencilState depthStencilState,
                                               final ColorBlendState colorBlendState,
                                               final RenderPass renderPass,
                                               final PipelineLayout pipelineLayout,
                                               final int subpassIndex,
                                               final PipelineCreateFlag... flags) {
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

        final List<Viewport> viewportsSafe = Optional.ofNullable(viewportState.getViewports())
                .orElseGet(Collections::emptyList);

        final VkViewport.Buffer viewports = VkViewport.calloc(viewportsSafe.size());

        viewportsSafe.stream()
                .map(viewport -> VkViewport.calloc()
                        .x(viewport.getX())
                        .y(viewport.getY())
                        .width(viewport.getWidth())
                        .height(viewport.getHeight())
                        .minDepth(viewport.getMinDepth())
                        .maxDepth(viewport.getMaxDepth()))
                .forEachOrdered(viewports::put);

        viewports.flip();

        final List<Rect2D> scissorsSafe = Optional.ofNullable(viewportState.getScissors())
                .orElseGet(Collections::emptyList);

        final VkRect2D.Buffer scissors = VkRect2D.calloc(scissorsSafe.size());

        scissorsSafe.stream()
                .map(scissor -> {
                    final VkRect2D rect2D = VkRect2D.calloc();

                    rect2D.extent()
                            .width(scissor.getExtent().getWidth())
                            .height(scissor.getExtent().getHeight());

                    rect2D.offset()
                            .x(scissor.getOffset().getX())
                            .y(scissor.getOffset().getY());

                    return rect2D;
                }).forEachOrdered(scissors::put);

        scissors.flip();

        final VkPipelineViewportStateCreateInfo viewportStateCreateInfo = VkPipelineViewportStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO)
                .pViewports(viewports)
                .pScissors(scissors);

        final VkPipelineRasterizationStateCreateInfo rasterizationStateCreateInfo = VkPipelineRasterizationStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO)
                .depthClampEnable(rasterizationState.isDepthClampEnabled())
                .rasterizerDiscardEnable(rasterizationState.isRasterizerDiscardEnabled())
                .polygonMode(HasValue.getValue(rasterizationState.getPolygonMode()))
                .cullMode(Maskable.toBitMask(rasterizationState.getCullMode()))
                .frontFace(HasValue.getValue(rasterizationState.getFrontFace()))
                .depthBiasEnable(rasterizationState.isDepthBiasEnabled())
                .depthBiasConstantFactor(rasterizationState.getDepthBiasConstantFactor())
                .depthBiasClamp(rasterizationState.getDepthBiasClamp())
                .depthBiasSlopeFactor(rasterizationState.getDepthBiasSlopeFactor())
                .lineWidth(rasterizationState.getLineWidth());

        final VkPipelineMultisampleStateCreateInfo multisampleStateCreateInfo = VkPipelineMultisampleStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO)
                .rasterizationSamples(Maskable.toBitMask(multisampleState.getRasterizationSamples()))
                .sampleShadingEnable(multisampleState.isSampleShadingEnabled())
                .minSampleShading(multisampleState.getMinSampleShading())
                .alphaToCoverageEnable(multisampleState.isAlphaToCoverageEnabled())
                .alphaToOneEnable(multisampleState.isAlphaToOneEnabled());

        final VkPipelineDepthStencilStateCreateInfo depthStencilStateCreateInfo = Optional.ofNullable(depthStencilState)
                .map(state -> VkPipelineDepthStencilStateCreateInfo.calloc()
                        .sType(VK_STRUCTURE_TYPE_PIPELINE_DEPTH_STENCIL_STATE_CREATE_INFO)
                        .depthTestEnable(state.isDepthTestEnabled())
                        .depthWriteEnable(state.isDepthWriteEnabled())
                        .depthCompareOp(HasValue.getValue(state.getDepthCompareOperator()))
                        .depthBoundsTestEnable(state.isDepthBoundsTestEnabled())
                        .stencilTestEnable(state.isStencilTestEnabled())
                        .front(getVkStencilOpState(state.getFrontOperationState()))
                        .back(getVkStencilOpState(state.getBackOperationState()))
                        .minDepthBounds(state.getMinDepthBounds())
                        .maxDepthBounds(state.getMaxDepthBounds()))
                .orElse(null);

        final List<ColorBlendAttachmentState> colorBlendAttachmentStatesSafe = Optional.ofNullable(colorBlendState.getAttachments())
                .orElseGet(Collections::emptyList);

        final VkPipelineColorBlendAttachmentState.Buffer colorBlendAttachmentStates = VkPipelineColorBlendAttachmentState.calloc(colorBlendAttachmentStatesSafe.size());

        colorBlendAttachmentStatesSafe.stream()
                .map(state -> VkPipelineColorBlendAttachmentState.calloc()
                        .blendEnable(state.isBlendEnabled())
                        .srcColorBlendFactor(HasValue.getValue(state.getSourceColorBlendFactor()))
                        .dstColorBlendFactor(HasValue.getValue(state.getDestinationColorBlendFactor()))
                        .colorBlendOp(HasValue.getValue(state.getColorBlendOperation()))
                        .srcAlphaBlendFactor(HasValue.getValue(state.getSourceAlphaBlendFactor()))
                        .dstAlphaBlendFactor(HasValue.getValue(state.getDestinationAlphaBlendFactor()))
                        .alphaBlendOp(HasValue.getValue(state.getAlphaBlendOperation()))
                        .colorWriteMask(Maskable.toBitMask(state.getColorWrites())))
                .forEachOrdered(colorBlendAttachmentStates::put);

        colorBlendAttachmentStates.flip();

        final float[] blendConstants = Optional.ofNullable(colorBlendState.getBlendConstants())
                .orElseGet(() -> new float[4]);

        final int logicOp = colorBlendState.isLogicOpEnabled() ? HasValue.getValue(colorBlendState.getLogicalOperation()) : 0;

        final VkPipelineColorBlendStateCreateInfo colorBlendStateCreateInfo = VkPipelineColorBlendStateCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO)
                .logicOpEnable(colorBlendState.isLogicOpEnabled())
                .logicOp(logicOp)
                .pAttachments(colorBlendAttachmentStates)
                .blendConstants(0, blendConstants[0])
                .blendConstants(1, blendConstants[1])
                .blendConstants(2, blendConstants[2])
                .blendConstants(3, blendConstants[3]);

        final VkGraphicsPipelineCreateInfo.Buffer graphicsPipelineCreateInfo = VkGraphicsPipelineCreateInfo.calloc(1)
                .sType(VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO)
                .flags(Maskable.toBitMask(flags))
                .pStages(shaderStageCreateInfos)
                .pVertexInputState(vertexInputStateCreateInfo)
                .pInputAssemblyState(inputAssemblyStateCreateInfo)
                .pTessellationState(null) // TODO
                .pViewportState(viewportStateCreateInfo)
                .pRasterizationState(rasterizationStateCreateInfo)
                .pMultisampleState(multisampleStateCreateInfo)
                .pDepthStencilState(depthStencilStateCreateInfo)
                .pColorBlendState(colorBlendStateCreateInfo)
                .pDynamicState(null)
                .layout(pipelineLayout.getAddress())
                .renderPass(renderPass.getAddress())
                .subpass(subpassIndex);

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
            viewportStateCreateInfo.free();
            rasterizationStateCreateInfo.free();
            multisampleStateCreateInfo.free();

            Optional.ofNullable(depthStencilStateCreateInfo).ifPresent(Struct::free);

            colorBlendStateCreateInfo.free();
            graphicsPipelineCreateInfo.free();
        }

        return result;
    }

    private static VkStencilOpState getVkStencilOpState(final StencilOperationState stencilOperationState) {
        return Optional.ofNullable(stencilOperationState)
                .map(state -> VkStencilOpState.calloc()
                        .failOp(HasValue.getValue(state.getFailOperation()))
                        .passOp(HasValue.getValue(state.getPassOperation()))
                        .depthFailOp(HasValue.getValue(state.getDepthFailOperation()))
                        .compareOp(HasValue.getValue(state.getCompareOperator()))
                        .compareMask(state.getCompareMask())
                        .writeMask(state.getWriteMask())
                        .reference(state.getReference()))
                .orElseGet(VkStencilOpState::calloc);
    }
}
