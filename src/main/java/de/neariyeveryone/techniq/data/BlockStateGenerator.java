package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(DataGenerator output, ExistingFileHelper exFileHelper) {
        super(output, TechniqConstants.MOD_ID, exFileHelper);
    }

    private void blockWithItem(RegistryObject<? extends Block> block) {
        simpleBlockItem(block.get(), cubeAll(block.get()));
    }

    @Override
    protected void registerStatesAndModels() {
        Blocks.simpleBlocks.forEach(this::blockWithItem);
    }
}
