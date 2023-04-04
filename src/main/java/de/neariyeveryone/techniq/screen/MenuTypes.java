package de.neariyeveryone.techniq.screen;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.advanced_furnace_generator.AdvancedFurnaceGeneratorMenu;
import de.neariyeveryone.techniq.block.basic_furnace_generator.BasicFurnaceGeneratorMenu;
//import de.neariyeveryone.techniq.block.compressor.CompressorMenu;
import de.neariyeveryone.techniq.block.metal_press.MetalPressMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            TechniqConstants.MOD_ID);

//    public static final RegistryObject<MenuType<TestBlockMenu>> TEST_MENU = register(TestBlockMenu::new, "test_block_menu");
    public static final RegistryObject<MenuType<MetalPressMenu>> METAL_PRESS_MENU = register(MetalPressMenu::new, "metal_press");
    public static final RegistryObject<MenuType<BasicFurnaceGeneratorMenu>> BASIC_FURNACE_GENERATOR_MENU = register(
            BasicFurnaceGeneratorMenu::new, "basic_furnace_generator");
    public static final RegistryObject<MenuType<AdvancedFurnaceGeneratorMenu>> ADVANCED_FURNACE_GENERATOR_MENU = register(
            AdvancedFurnaceGeneratorMenu::new, "advanced_furnace_generator");
//    public static final RegistryObject<MenuType<CompressorMenu>> COMPRESSOR_MENU = register(
//            CompressorMenu::new, "compressor");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(
            IContainerFactory<T> factory, String name) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }
}
