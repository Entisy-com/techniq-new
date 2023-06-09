package de.neariyeveryone.techniq.block;

import de.neariyeveryone.techniq.Techniq;
import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.advanced_furnace_generator.AdvancedFurnaceGeneratorBlock;
import de.neariyeveryone.techniq.block.basic_furnace_generator.BasicFurnaceGeneratorBlock;
//import de.neariyeveryone.techniq.block.compressor.CompressorBlock;
import de.neariyeveryone.techniq.block.metal_press.MetalPressBlock;
import de.neariyeveryone.techniq.item.Items;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Blocks {
    public static final List<RegistryObject<? extends Block>> simpleBlocks = new ArrayList<>();
    public static final List<RegistryObject<? extends DropExperienceBlock>> oreBlocks = new ArrayList<>();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TechniqConstants.MOD_ID);


//    public static final RegistryObject<TestBlock> TEST_BLOCK = registerBlock("test_block", TestBlock::new);
    public static final RegistryObject<MetalPressBlock> METAL_PRESS = registerBlock("metal_press", MetalPressBlock::new);
    public static final RegistryObject<BasicFurnaceGeneratorBlock> BASIC_FURNACE_GENERATOR =
            registerBlock("basic_furnace_generator", BasicFurnaceGeneratorBlock::new);
    public static final RegistryObject<AdvancedFurnaceGeneratorBlock> ADVANCED_FURNACE_GENERATOR =
            registerBlock("advanced_furnace_generator", AdvancedFurnaceGeneratorBlock::new);
//    public static final RegistryObject<CompressorBlock> COMPRESSOR =
//            registerBlock("compressor", CompressorBlock::new);

//    public static final RegistryObject<DropExperienceBlock> AMBER_ORE = registerSimpleBlock("amber_ore", () -> new DropExperienceBlock(
//            BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.IRON_ORE), UniformInt.of(3, 7)));
//    public static final RegistryObject<Block> AMBER_BLOCK = registerSimpleBlock("amber_block", () -> new Block(
//            BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.IRON_BLOCK)));


    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return Items.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(Techniq.TAB)));
    }

    private static <T extends Block> RegistryObject<T> registerSimpleBlock(String name, Supplier<T> block) {
        var toReturn = registerBlock(name, block);
        simpleBlocks.add(toReturn);
        return toReturn;
    }
    private static <T extends DropExperienceBlock> RegistryObject<T> registerOreBlock(String name, Supplier<T> block) {
        var toReturn = registerBlock(name, block);
        oreBlocks.add(toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        name = name.replace(" ", "_").toLowerCase();
        var toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
}
