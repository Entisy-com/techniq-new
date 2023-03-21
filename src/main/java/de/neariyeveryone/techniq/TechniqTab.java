package de.neariyeveryone.techniq;//package de.neariyeveryone.techniq;
//
//import de.neariyeveryone.techniq.item.Items;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.event.CreativeModeTabEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = TechniqConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class TechniqTab {
//    public static CreativeModeTab TAB;
//
//    @SubscribeEvent
//    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
//        TAB = event.registerCreativeModeTab(new ResourceLocation(TechniqConstants.MOD_ID, "techniq_tab"),
//                builder -> builder.icon(() -> new ItemStack(Items.TEST_ITEM.get())).title(
//                        Component.literal("Techniq")).build());
//    }
//}
