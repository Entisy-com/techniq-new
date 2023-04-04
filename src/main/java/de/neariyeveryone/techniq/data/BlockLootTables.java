package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.block.Blocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

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
