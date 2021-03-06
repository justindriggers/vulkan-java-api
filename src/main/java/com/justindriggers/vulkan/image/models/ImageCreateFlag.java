package com.justindriggers.vulkan.image.models;

import com.justindriggers.vulkan.models.Maskable;
import org.lwjgl.vulkan.EXTSampleLocations;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VK11;

public enum ImageCreateFlag implements Maskable {

    SPARSE_BINDING(VK10.VK_IMAGE_CREATE_SPARSE_BINDING_BIT),
    SPARSE_RESIDENCY(VK10.VK_IMAGE_CREATE_SPARSE_RESIDENCY_BIT),
    SPARSE_ALIASED(VK10.VK_IMAGE_CREATE_SPARSE_ALIASED_BIT),
    MUTABLE_FORMAT(VK10.VK_IMAGE_CREATE_MUTABLE_FORMAT_BIT),
    CUBE_COMPATIBLE(VK10.VK_IMAGE_CREATE_CUBE_COMPATIBLE_BIT),
    TWO_DIMENSIONAL_ARRAY_COMPATIBLE(VK11.VK_IMAGE_CREATE_2D_ARRAY_COMPATIBLE_BIT),
    PROTECTED(VK11.VK_IMAGE_CREATE_PROTECTED_BIT),
    SPLIT_INSTANCE_BIND_REGIONS(VK11.VK_IMAGE_CREATE_SPLIT_INSTANCE_BIND_REGIONS_BIT),
    BLOCK_TEXEL_VIEW_COMPATIBLE(VK11.VK_IMAGE_CREATE_BLOCK_TEXEL_VIEW_COMPATIBLE_BIT),
    EXTENDED_USAGE(VK11.VK_IMAGE_CREATE_EXTENDED_USAGE_BIT),
    DISJOINT(VK11.VK_IMAGE_CREATE_DISJOINT_BIT),
    ALIAS(VK11.VK_IMAGE_CREATE_ALIAS_BIT),
    SAMPLE_LOCATIONS_COMPATIBLE_DEPTH(EXTSampleLocations.VK_IMAGE_CREATE_SAMPLE_LOCATIONS_COMPATIBLE_DEPTH_BIT_EXT);
    
    private final int bit;
    
    ImageCreateFlag(final int bit) {
        this.bit = bit;
    }

    @Override
    public int getBitValue() {
        return bit;
    }
}
