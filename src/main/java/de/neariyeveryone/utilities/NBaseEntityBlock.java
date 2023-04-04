package de.neariyeveryone.utilities;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class NBaseEntityBlock extends BaseEntityBlock {
    public static final BooleanProperty WORKING = BooleanProperty.create("working");

    protected NBaseEntityBlock(Properties p_49224_) {
        super(p_49224_);
    }
}
