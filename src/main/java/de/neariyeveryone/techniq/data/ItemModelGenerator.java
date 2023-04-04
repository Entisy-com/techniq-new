package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.Techniq;
import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.techniq.item.Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BaseEntityBlock;
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
            if (item.get() instanceof BlockItem block) {
                if (block.getBlock() instanceof BaseEntityBlock)
                    machineBlock(item);
                else
                    simpleBlockItem(item);
            }
            else simpleItem(item);
        });
    }

    private void machineBlock(RegistryObject<Item> item) {
        var name = item.getId().getPath();
        orientableWithBottom(name,
                modLoc(String.format("block/machines/%s/side", name)),
                modLoc(String.format("block/machines/%s/front", name)),
                modLoc(String.format("block/machines/%s/bottom", name)),
                modLoc(String.format("block/machines/%s/top", name)));
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
