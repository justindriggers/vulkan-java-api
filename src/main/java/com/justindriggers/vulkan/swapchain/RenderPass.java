package com.justindriggers.vulkan.swapchain;

import com.justindriggers.vulkan.devices.logical.LogicalDevice;
import com.justindriggers.vulkan.instance.VulkanFunction;
import com.justindriggers.vulkan.models.HasValue;
import com.justindriggers.vulkan.models.Maskable;
import com.justindriggers.vulkan.models.pointers.DisposablePointer;
import com.justindriggers.vulkan.swapchain.models.ColorAttachment;
import com.justindriggers.vulkan.swapchain.models.Subpass;
import com.justindriggers.vulkan.swapchain.models.SubpassDependency;
import org.lwjgl.vulkan.VkAttachmentDescription;
import org.lwjgl.vulkan.VkAttachmentReference;
import org.lwjgl.vulkan.VkRenderPassCreateInfo;
import org.lwjgl.vulkan.VkSubpassDependency;
import org.lwjgl.vulkan.VkSubpassDescription;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateRenderPass;
import static org.lwjgl.vulkan.VK10.vkDestroyRenderPass;

public class RenderPass extends DisposablePointer {

    private final LogicalDevice device;

    public RenderPass(final LogicalDevice device,
                      final List<Subpass> subpasses,
                      final List<SubpassDependency> subpassDependencies) {
        super(createRenderPass(device, subpasses, subpassDependencies));
        this.device = device;
    }

    @Override
    protected void dispose(final long address) {
        vkDestroyRenderPass(device.unwrap(), address, null);
    }

    private static long createRenderPass(final LogicalDevice device,
                                         final List<Subpass> subpasses,
                                         final List<SubpassDependency> subpassDependencies) {
        final long result;

        final LongBuffer renderPass = memAllocLong(1);

        final List<Subpass> subpassesSafe = Optional.ofNullable(subpasses)
                .orElseGet(Collections::emptyList);

        final List<VkAttachmentDescription> attachmentDescriptions = new ArrayList<>();

        final VkSubpassDescription.Buffer subpassDescriptionsBuffer = VkSubpassDescription.calloc(subpassesSafe.size());

        subpassesSafe.forEach(subpass -> {
            final List<ColorAttachment> colorAttachments = Optional.ofNullable(subpass.getColorAttachments())
                    .orElseGet(Collections::emptyList);

            final List<VkAttachmentReference> colorAttachmentReferences = new ArrayList<>();

            for (final ColorAttachment colorAttachment : colorAttachments) {
                final VkAttachmentDescription attachmentDescription = VkAttachmentDescription.calloc()
                        .format(HasValue.getValue(colorAttachment.getFormat()))
                        .samples(Maskable.toBitMask(colorAttachment.getSampleCounts()))
                        .loadOp(HasValue.getValue(colorAttachment.getLoadOperation()))
                        .storeOp(HasValue.getValue(colorAttachment.getStoreOperation()))
                        .stencilLoadOp(HasValue.getValue(colorAttachment.getStencilLoadOperation()))
                        .stencilStoreOp(HasValue.getValue(colorAttachment.getStencilStoreOperation()))
                        .initialLayout(HasValue.getValue(colorAttachment.getInitialLayout()))
                        .finalLayout(HasValue.getValue(colorAttachment.getFinalLayout()));

                attachmentDescriptions.add(attachmentDescription);

                final VkAttachmentReference attachmentReference = VkAttachmentReference.calloc()
                        .attachment(attachmentDescriptions.size() - 1)
                        .layout(HasValue.getValue(colorAttachment.getAttachmentLayout()));

                colorAttachmentReferences.add(attachmentReference);
            }

            final VkAttachmentReference depthStencilAttachmentReference = Optional.ofNullable(subpass.getDepthStencilAttachment())
                    .map(depthStencilAttachment -> {
                        final VkAttachmentDescription attachmentDescription = VkAttachmentDescription.calloc()
                                .format(HasValue.getValue(depthStencilAttachment.getFormat()))
                                .samples(Maskable.toBitMask(depthStencilAttachment.getSampleCounts()))
                                .loadOp(HasValue.getValue(depthStencilAttachment.getLoadOperation()))
                                .storeOp(HasValue.getValue(depthStencilAttachment.getStoreOperation()))
                                .stencilLoadOp(HasValue.getValue(depthStencilAttachment.getStencilLoadOperation()))
                                .stencilStoreOp(HasValue.getValue(depthStencilAttachment.getStencilStoreOperation()))
                                .initialLayout(HasValue.getValue(depthStencilAttachment.getInitialLayout()))
                                .finalLayout(HasValue.getValue(depthStencilAttachment.getFinalLayout()));

                        attachmentDescriptions.add(attachmentDescription);

                        return VkAttachmentReference.calloc()
                                .attachment(attachmentDescriptions.size() - 1)
                                .layout(HasValue.getValue(depthStencilAttachment.getAttachmentLayout()));
                    }).orElse(null);

            final VkAttachmentReference.Buffer colorAttachmentReferenceBuffer = VkAttachmentReference.calloc(colorAttachmentReferences.size());
            colorAttachmentReferences.forEach(colorAttachmentReferenceBuffer::put);
            colorAttachmentReferenceBuffer.flip();

            final VkSubpassDescription subpassDescription = VkSubpassDescription.calloc()
                    .pipelineBindPoint(HasValue.getValue(subpass.getPipelineBindPoint()))
                    .colorAttachmentCount(colorAttachments.size())
                    .pColorAttachments(colorAttachmentReferenceBuffer)
                    .pDepthStencilAttachment(depthStencilAttachmentReference);

            subpassDescriptionsBuffer.put(subpassDescription);
        });

        subpassDescriptionsBuffer.flip();

        final VkAttachmentDescription.Buffer attachmentDescriptionsBuffer = VkAttachmentDescription.calloc(attachmentDescriptions.size());
        attachmentDescriptions.forEach(attachmentDescriptionsBuffer::put);
        attachmentDescriptionsBuffer.flip();

        final List<SubpassDependency> subpassDependenciesSafe = Optional.ofNullable(subpassDependencies)
                .orElseGet(Collections::emptyList);

        final VkSubpassDependency.Buffer subpassDependenciesBuffer = VkSubpassDependency.calloc(subpassDependenciesSafe.size());

        subpassDependenciesSafe.stream()
                .map(subpassDependency -> VkSubpassDependency.calloc()
                        .srcSubpass(subpassDependency.getSourceSubpass())
                        .dstSubpass(subpassDependency.getDestinationSubpass())
                        .srcStageMask(Maskable.toBitMask(subpassDependency.getSourceStages()))
                        .dstStageMask(Maskable.toBitMask(subpassDependency.getDestinationStages()))
                        .srcAccessMask(Maskable.toBitMask(subpassDependency.getSourceAccessFlags()))
                        .dstAccessMask(Maskable.toBitMask(subpassDependency.getDestinationAccessFlags())))
                .forEachOrdered(subpassDependenciesBuffer::put);

        subpassDependenciesBuffer.flip();

        final VkRenderPassCreateInfo renderPassCreateInfo = VkRenderPassCreateInfo.calloc()
                .sType(VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO)
                .pAttachments(attachmentDescriptionsBuffer)
                .pSubpasses(subpassDescriptionsBuffer)
                .pDependencies(subpassDependenciesBuffer);

        try {
            VulkanFunction.execute(() -> vkCreateRenderPass(device.unwrap(), renderPassCreateInfo, null, renderPass));

            result = renderPass.get(0);
        } finally {
            memFree(renderPass);

            subpassDescriptionsBuffer.free();
            subpassDependenciesBuffer.free();
            renderPassCreateInfo.free();
        }

        return result;
    }
}
