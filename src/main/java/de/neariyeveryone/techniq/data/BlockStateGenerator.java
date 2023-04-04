package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.utilities.NBaseEntityBlock;
import jdk.jfr.Description;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.framework.qual.Unused;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(DataGenerator output, ExistingFileHelper exFileHelper) {
        super(output, TechniqConstants.MOD_ID, exFileHelper);
    }

    private void blockWithItem(RegistryObject<? extends Block> block) {
        simpleBlockItem(block.get(), cubeAll(block.get()));
    }

    private void machineBlockWithItem(RegistryObject<? extends Block> block) {
        simpleBlockItem(block.get(), orientable(block.get()));
    }

    private ModelFile orientable(Block block) {
        var name = block.getName().getString().replace(String.format("block.%s.", TechniqConstants.MOD_ID), "");
        return models().orientableWithBottom(name,
                modLoc(String.format("block/machines/%s/side", name)),
                modLoc(String.format("block/machines/%s/front", name)),
                modLoc(String.format("block/machines/%s/bottom", name)),
                modLoc(String.format("block/machines/%s/top", name)));
    }

    @Override
    protected void registerStatesAndModels() {
        Blocks.BLOCKS.getEntries().forEach(b -> {
            if (b.get() instanceof BaseEntityBlock)
                if (!(b.get() instanceof NBaseEntityBlock))
                    machineBlockWithItem(b);
            else if (!(b.get() instanceof NBaseEntityBlock))
                blockWithItem(b);
        });
    }
}
