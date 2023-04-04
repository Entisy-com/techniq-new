package de.neariyeveryone.techniq;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class TechniqConstants {
    public TechniqConstants() throws IllegalAccessException {
        throw new IllegalAccessException("TechniqConstants is a utility class!");
    }

    public static final String MOD_ID = "techniq";
    public static final BlockBehaviour.Properties MACHINE = BlockBehaviour.Properties.of(Material.METAL).strength(2.5f, 18).requiresCorrectToolForDrops();

    public static final ResourceLocation METAL_PRESS = modId("metal_press");
    public static final ResourceLocation COMPRESSOR = modId("compressor");

    public static ResourceLocation getId(String path) {
        if (path.contains(":")) {
            throw new IllegalArgumentException("path contains namespace");
        }
        return new ResourceLocation(MOD_ID, path);
    }

    private static ResourceLocation modId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
