package de.neariyeveryone.techniq;

import de.neariyeveryone.techniq.block.BlockEntities;
import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.techniq.block.advanced_furnace_generator.AdvancedFurnaceGeneratorScreen;
import de.neariyeveryone.techniq.block.basic_furnace_generator.BasicFurnaceGeneratorScreen;
//import de.neariyeveryone.techniq.block.compressor.CompressorScreen;
import de.neariyeveryone.techniq.block.metal_press.MetalPressScreen;
import de.neariyeveryone.techniq.recipe.RecipeTypes;
import de.neariyeveryone.techniq.item.Items;
import de.neariyeveryone.techniq.recipe.Recipes;
import de.neariyeveryone.techniq.screen.MenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(TechniqConstants.MOD_ID)
public class Techniq {
    public Techniq() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        Items.ITEMS.register(bus);
        Blocks.BLOCKS.register(bus);
        BlockEntities.BLOCK_ENTITIES.register(bus);
        MenuTypes.MENU_TYPES.register(bus);
        RecipeTypes.RECIPE_TYPES.register(bus);
        Recipes.RECIPE_SERIALIZERS.register(bus);

        bus.addListener(this::clientSetup);

//        Messages.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void clientSetup(FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            MenuScreens.register(MenuTypes.METAL_PRESS_MENU.get(), MetalPressScreen::new);
//            MenuScreens.register(MenuTypes.COMPRESSOR_MENU.get(), CompressorScreen::new);
            MenuScreens.register(MenuTypes.ADVANCED_FURNACE_GENERATOR_MENU.get(), AdvancedFurnaceGeneratorScreen::new);
            MenuScreens.register(MenuTypes.BASIC_FURNACE_GENERATOR_MENU.get(), BasicFurnaceGeneratorScreen::new);
        });
    }

    public static final CreativeModeTab TAB = new CreativeModeTab(TechniqConstants.MOD_ID + ".tab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(Blocks.METAL_PRESS.get());
        }
    };

//    @Mod.EventBusSubscriber(modid = TechniqConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class ClientModEvents {
//        @SubscribeEvent
//        public static void onClientSetup(FMLClientSetupEvent e) {
//            e.enqueueWork(() -> {
//                MenuScreens.register(MenuTypes.METAL_PRESS_MENU.get(), MetalPressScreen::new);
//                MenuScreens.register(MenuTypes.COMPRESSOR_MENU.get(), CompressorScreen::new);
//                MenuScreens.register(MenuTypes.ADVANCED_FURNACE_GENERATOR_MENU.get(), AdvancedFurnaceGeneratorScreen::new);
//                MenuScreens.register(MenuTypes.BASIC_FURNACE_GENERATOR_MENU.get(), BasicFurnaceGeneratorScreen::new);
//            });
//        }
//    }
}