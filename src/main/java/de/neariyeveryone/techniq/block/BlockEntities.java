package de.neariyeveryone.techniq.block;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.advanced_furnace_generator.AdvancedFurnaceGeneratorBlockEntity;
import de.neariyeveryone.techniq.block.basic_furnace_generator.BasicFurnaceGeneratorBlockEntity;
//import de.neariyeveryone.techniq.block.compressor.CompressorBlockEntity;
import de.neariyeveryone.techniq.block.metal_press.MetalPressBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES, TechniqConstants.MOD_ID);

//    public static RegistryObject<BlockEntityType<TestBlockEntity>> TEST_BLOCK_ENTITY = BLOCK_ENTITIES.register(
//            "test_block", () -> BlockEntityType.Builder.of(TestBlockEntity::new, Blocks.TEST_BLOCK.get()).build(null));

    public static RegistryObject<BlockEntityType<MetalPressBlockEntity>> METAL_PRESS_ENTITY = BLOCK_ENTITIES.register(
            "metal_press", () -> BlockEntityType.Builder.of(MetalPressBlockEntity::new, Blocks.METAL_PRESS.get()).build(null));

    public static RegistryObject<BlockEntityType<BasicFurnaceGeneratorBlockEntity>> BASIC_FURNACE_GENERATOR_ENTITY =
            BLOCK_ENTITIES.register("basic_furnace_generator", () -> BlockEntityType.Builder.of(
                    BasicFurnaceGeneratorBlockEntity::new, Blocks.BASIC_FURNACE_GENERATOR.get()).build(null));

    public static RegistryObject<BlockEntityType<AdvancedFurnaceGeneratorBlockEntity>> ADVANCED_FURNACE_GENERATOR_ENTITY =
            BLOCK_ENTITIES.register("advanced_furnace_generator", () -> BlockEntityType.Builder.of(
                    AdvancedFurnaceGeneratorBlockEntity::new, Blocks.ADVANCED_FURNACE_GENERATOR.get()).build(null));

//    public static RegistryObject<BlockEntityType<CompressorBlockEntity>> COMPRESSOR_ENTITY = BLOCK_ENTITIES.register(
//            "compressor", () -> BlockEntityType.Builder.of(CompressorBlockEntity::new, Blocks.COMPRESSOR.get()).build(null));
}
