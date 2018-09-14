package com.justindriggers.vulkan.instance;

import com.justindriggers.vulkan.instance.models.VulkanException;
import com.justindriggers.vulkan.instance.models.VulkanResult;
import com.justindriggers.vulkan.models.HasValue;

import java.util.Optional;

@FunctionalInterface
public interface VulkanFunction {

    int getResult();

    default void execute() {
        Optional.of(getResult())
                .map(code -> HasValue.getByValue(code, VulkanResult.class))
                .filter(result -> !VulkanResult.SUCCESS.equals(result))
                .map(VulkanException::new)
                .ifPresent(exception -> {
                    throw exception;
                });
    }

    static void execute(final VulkanFunction vulkanFunction) {
        vulkanFunction.execute();
    }
}
