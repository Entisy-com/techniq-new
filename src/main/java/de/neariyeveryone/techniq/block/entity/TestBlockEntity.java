package de.neariyeveryone.techniq.block.entity;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.techniq.block.TestBlock;
import de.neariyeveryone.techniq.item.Items;
import de.neariyeveryone.techniq.recipe.TestBlockRecipe;
import de.neariyeveryone.techniq.screen.TestBlockMenu;
import de.neariyeveryone.utilities.NAtomicInteger;
import de.neariyeveryone.utilities.NEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TestBlockEntity extends BlockEntity implements MenuProvider {
    private final NAtomicInteger progress = new NAtomicInteger(0);
    private final NAtomicInteger maxProgress = new NAtomicInteger(200);

    protected final ContainerData data;

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() == net.minecraft.world.item.Items.WATER_BUCKET;
                case 1 -> stack.getItem() == Items.RAW_AMBER.get();
                case 2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final NEnergyStorage ENERGY_STORAGE = new NEnergyStorage(60_000, 256) {
        @Override
        public void onEnergyChange() {
            setChanged();
        }
    };
    private static final int REQUIRED_ENERGY = 32;

    private LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap = Map.of(
            Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
            Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1,
                    (i, s) -> itemHandler.isItemValid(1, s))),
            Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
            Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1,
                    (i, s) -> itemHandler.isItemValid(1, s))),
            Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 0 || i == 1,
                    (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s)))
    );

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.TEST_BLOCK_ENTITY.get(), pos, state);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TestBlockEntity.this.progress.get();
                    case 1 -> TestBlockEntity.this.maxProgress.get();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TestBlockEntity.this.progress.set(value);
                    case 1 -> TestBlockEntity.this.maxProgress.set(value);
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }


    @Override
    public @NotNull Component getDisplayName() {
        return new TranslatableComponent("Test Block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory inv, @NotNull Player player) {
        return new TestBlockMenu(windowId, inv, this, data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY)
            return lazyEnergyHandler.cast();

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null)
                return lazyItemHandler.cast();
            if (directionWrappedHandlerMap.containsKey(side)) {
                Direction direction = getBlockState().getValue(TestBlock.FACING);

                if (side == Direction.UP || side == Direction.DOWN)
                    return directionWrappedHandlerMap.get(side).cast();

                return switch (direction) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("test_block.progress", progress.get());
        nbt.putInt("test_block.energy", ENERGY_STORAGE.getEnergyStored());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress.set(nbt.getInt("test_block.progress"));
        ENERGY_STORAGE.setEnergy(nbt.getInt("test_block.energy"));
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++)
            inv.setItem(i, itemHandler.getStackInSlot(i));
        Containers.dropContents(Objects.requireNonNull(level), worldPosition, inv);
    }

    private static boolean flag1(TestBlockEntity e) {
        return e.itemHandler.getStackInSlot(0).getItem() == Blocks.AMBER_ORE.get().asItem();
    }

    private static boolean hasEnoughEnergy(TestBlockEntity e) {
        return e.ENERGY_STORAGE.getEnergyStored() >= REQUIRED_ENERGY * e.maxProgress.get();
    }

    private static void extractEnergy(TestBlockEntity e) {
        e.ENERGY_STORAGE.extractEnergy(REQUIRED_ENERGY, false);
    }
    public static void tick(Level level, BlockPos pos, BlockState state, TestBlockEntity e) {
        if (level.isClientSide()) return;

        if (flag1(e)) {
            e.ENERGY_STORAGE.receiveEnergy(64, false);
        }

        if (hasRecipe(e) && hasEnoughEnergy(e)) {
            e.progress.addAndGet(1);
            extractEnergy(e);
            setChanged(level, pos, state);
            if (e.progress.compare(e.maxProgress)) {
                craft(e);
                e.progress.set(0);
                setChanged(level, pos, state);
            }
        } else {
            e.progress.set(0);
            setChanged(level, pos, state);
        }
    }

    private static void craft(TestBlockEntity e) {
        if (!hasRecipe(e)) return;

        Level level = e.level;
        SimpleContainer inv = new SimpleContainer(e.itemHandler.getSlots());
        for (int i = 0; i < e.itemHandler.getSlots(); i++)
            inv.setItem(i, e.itemHandler.getStackInSlot(i));

        Optional<TestBlockRecipe> recipe = Objects.requireNonNull(level).getRecipeManager().getRecipeFor(TestBlockRecipe.Type.INSTANCE,
                inv, level);

        e.itemHandler.extractItem(1, 1, false);
        e.itemHandler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem(),
                e.itemHandler.getStackInSlot(2).getCount() + 1));
    }

    private static boolean hasRecipe(TestBlockEntity e) {
        Level level = e.level;
        SimpleContainer inv = new SimpleContainer(e.itemHandler.getSlots());
        for (int i = 0; i < e.itemHandler.getSlots(); i++)
            inv.setItem(i, e.itemHandler.getStackInSlot(i));

        Optional<TestBlockRecipe> recipe = Objects.requireNonNull(level).getRecipeManager().getRecipeFor(TestBlockRecipe.Type.INSTANCE,
                inv, level);

        return recipe.isPresent() && canMoveItem(inv, recipe.get().getResultItem());
    }

    private static boolean canMoveItem(SimpleContainer inv, ItemStack item) {
        return inv.getItem(2).getMaxStackSize() > inv.getItem(2).getCount() && (
                inv.getItem(2).getItem() == item.getItem() || inv.getItem(2).isEmpty());
    }
}
