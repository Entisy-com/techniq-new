package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.TechniqConstants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = TechniqConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var eFH = event.getExistingFileHelper();

        gen.addProvider(new RecipeGenerator(gen));
        gen.addProvider(new BlockStateGenerator(gen, eFH));
        gen.addProvider(new ItemModelGenerator(gen, eFH));
        gen.addProvider(new LootTableGenerator(gen));
        gen.addProvider(new LangFileGenerator(gen));
    }
}
