package de.neariyeveryone.techniq.block;

import de.neariyeveryone.techniq.block.entity.BlockEntities;
import de.neariyeveryone.techniq.block.entity.TestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class TestBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final Set<ToolAction> TOOL_ACTIONS =  ToolActions.DEFAULT_PICKAXE_ACTIONS;

    

    public TestBlock() {
        super(Properties.of(Material.STONE).noOcclusion().strength(5f, 6f)
                .requiresCorrectToolForDrops());
    }



//    private static final VoxelShape NORTH = Shapes.join(
//            Block.box(5, 5, 0, 9, 10, 6), Block.box(0, 0, 0, 16, 5, 16), BooleanOp.OR);
//    private static final VoxelShape EAST = Shapes.join(
//            Block.box(10, 5, 5, 16, 10, 9), Block.box(0, 0, 0, 16, 5, 16), BooleanOp.OR);
//    private static final VoxelShape SOUTH = Shapes.join(
//            Block.box(7, 5, 10, 11, 10, 16), Block.box(0, 0, 0, 16, 5, 16), BooleanOp.OR);
//    private static final VoxelShape WEST = Shapes.join(
//            Block.box(0, 5, 7, 6, 10, 11), Block.box(0, 0, 0, 16, 5, 16), BooleanOp.OR);



    @Override
    public @NotNull @Deprecated VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter,
                               @NotNull BlockPos pos, @NotNull CollisionContext context) {
//        return switch (state.getValue(FACING)) {
//            case EAST -> EAST;
//            case SOUTH -> SOUTH;
//            case WEST -> WEST;
//            default -> NORTH;
//        };
        return Block.box(0, 0, 0, 16, 7, 16);
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

    @Deprecated
    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Deprecated
    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TestBlockEntity)
                ((TestBlockEntity) blockEntity).drops();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Deprecated
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                 @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult rayCastHit) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TestBlockEntity)
                NetworkHooks.openGui(((ServerPlayer) player), (TestBlockEntity) blockEntity, pos);
            else
                throw new IllegalStateException("Container Provider Missing");
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TestBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntities.TEST_BLOCK_ENTITY.get(), TestBlockEntity::tick);
    }
}
