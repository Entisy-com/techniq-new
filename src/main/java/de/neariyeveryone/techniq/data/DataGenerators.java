package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.TechniqConstants;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechniqConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var eFH = event.getExistingFileHelper();

        gen.addProvider(true, new RecipeGenerator(gen));
        gen.addProvider(true, new BlockStateGenerator(gen, eFH));
        gen.addProvider(true, new ItemModelGenerator(gen, eFH));
        gen.addProvider(true, new LootTableGenerator(gen));
        gen.addProvider(true, new LangFileGenerator(gen));
    }
}
