package de.neariyeveryone.techniq;

import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.techniq.block.entity.BlockEntities;
import de.neariyeveryone.techniq.item.Items;
import de.neariyeveryone.techniq.networking.Messages;
import de.neariyeveryone.techniq.recipe.Recipes;
import de.neariyeveryone.techniq.screen.MenuTypes;
import de.neariyeveryone.techniq.screen.TestBlockScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        Recipes.RECIPE_SERIALIZERS.register(bus);

        Messages.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final CreativeModeTab TAB= new CreativeModeTab("tab") {

        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(Items.RAW_AMBER.get());
        }
    };

    @Mod.EventBusSubscriber(modid = TechniqConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent e) {
            MenuScreens.register(MenuTypes.TEST_MENU.get(), TestBlockScreen::new);
        }
    }
}