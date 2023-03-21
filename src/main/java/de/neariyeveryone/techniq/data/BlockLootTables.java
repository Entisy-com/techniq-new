package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.block.Blocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class BlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        Blocks.BLOCKS.getEntries().forEach(block -> dropSelf(block.get()));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return Blocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
