package de.neariyeveryone.techniq.block.advanced_furnace_generator;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class AdvancedFurnaceGeneratorBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AdvancedFurnaceGeneratorBlock() {
        super(TechniqConstants.MACHINE);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter getter, @NotNull List<Component> lore, @NotNull TooltipFlag flag) {
        lore.add(Component.literal("WIP"));
        super.appendHoverText(stack, getter, lore, flag);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AdvancedFurnaceGeneratorBlockEntity(pos, state);
    }

    @Override
    public @NotNull @Deprecated VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter,
                                                    @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            var blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AdvancedFurnaceGeneratorBlockEntity)
                ((AdvancedFurnaceGeneratorBlockEntity) blockEntity).drops();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                          @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult rayCastHit) {
        if (!level.isClientSide()) {
            var blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AdvancedFurnaceGeneratorBlockEntity)
                NetworkHooks.openScreen(((ServerPlayer) player), (AdvancedFurnaceGeneratorBlockEntity) blockEntity, pos);
            else
                throw new IllegalStateException("Container Provider Missing");
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntities.ADVANCED_FURNACE_GENERATOR_ENTITY.get(), AdvancedFurnaceGeneratorBlockEntity::tick);
    }
}
