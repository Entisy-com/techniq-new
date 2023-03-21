package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.techniq.item.Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(DataGenerator output, ExistingFileHelper existingFileHelper) {
        super(output, TechniqConstants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Items.ITEMS.getEntries().forEach(item -> {
            if (item.get() instanceof BlockItem block)
                simpleBlockItem(item);
            else simpleItem(item);
        });
    }

    private void simpleItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(TechniqConstants.MOD_ID,
                        String.format("item/%s", item.getId().getPath())));
    }

    private void simpleBlockItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(),
                new ResourceLocation(TechniqConstants.MOD_ID,
                        String.format("block/%s", item.getId().getPath())));
    }

    private void handheldItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(),
                new ResourceLocation("texture/handheld")).texture("layer0",
                new ResourceLocation(TechniqConstants.MOD_ID,
                        String.format("item/%s", item.getId().getPath())));
    }
}
