package com.justindriggers.vulkan.pipeline.models.colorblend;

import com.justindriggers.vulkan.models.HasValue;
import org.lwjgl.vulkan.VK10;

public enum LogicalOperation implements HasValue<Integer> {

    CLEAR(VK10.VK_LOGIC_OP_CLEAR),
    AND(VK10.VK_LOGIC_OP_AND),
    AND_REVERSE(VK10.VK_LOGIC_OP_AND_REVERSE),
    COPY(VK10.VK_LOGIC_OP_COPY),
    AND_INVERTED(VK10.VK_LOGIC_OP_AND_INVERTED),
    NO_OP(VK10.VK_LOGIC_OP_NO_OP),
    XOR(VK10.VK_LOGIC_OP_XOR),
    OR(VK10.VK_LOGIC_OP_OR),
    NOR(VK10.VK_LOGIC_OP_NOR),
    EQUIVALENT(VK10.VK_LOGIC_OP_EQUIVALENT),
    INVERT(VK10.VK_LOGIC_OP_INVERT),
    OR_REVERSE(VK10.VK_LOGIC_OP_OR_REVERSE),
    COPY_INVERTED(VK10.VK_LOGIC_OP_COPY_INVERTED),
    OR_INVERTED(VK10.VK_LOGIC_OP_OR_INVERTED),
    NAND(VK10.VK_LOGIC_OP_NAND),
    SET(VK10.VK_LOGIC_OP_SET);

    private final int value;

    LogicalOperation(final int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
