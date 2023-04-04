//package de.neariyeveryone.techniq.block.test_block;
//
//import de.neariyeveryone.techniq.block.BlockEntities;
//import de.neariyeveryone.utilities.NBlockEntity;
//import de.neariyeveryone.utilities.WrappedHandler;
//import de.neariyeveryone.techniq.item.Items;
//import de.neariyeveryone.techniq.networking.Messages;
//import de.neariyeveryone.techniq.networking.packet.EnergySyncS2CPacket;
//import de.neariyeveryone.techniq.networking.packet.FluidSyncS2CPacket;
//import de.neariyeveryone.techniq.recipe.TestBlockRecipe;
//import de.neariyeveryone.utilities.NAtomicInteger;
//import de.neariyeveryone.utilities.NEnergyStorage;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.Containers;
//import net.minecraft.world.MenuProvider;
//import net.minecraft.world.SimpleContainer;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.ContainerData;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ForgeCapabilities;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.energy.IEnergyStorage;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import net.minecraftforge.fluids.capability.templates.FluidTank;
//import net.minecraftforge.items.ItemStackHandler;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Map;
//import java.util.Objects;
//import java.util.Optional;
//
//public class TestBlockEntity extends NBlockEntity implements MenuProvider {
//    private final NAtomicInteger progress = new NAtomicInteger(0);
//    private final NAtomicInteger maxProgress = new NAtomicInteger(200);
//
//    protected final ContainerData data;
//
//    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
//        @Override
//        protected void onContentsChanged(int slot) {
//            setChanged();
//        }
//
//        @Override
//        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//            return switch (slot) {
//                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent();
//                case 1 -> stack.getItem() == Items.RAW_AMBER.get();
//                case 2 -> false;
//                default -> super.isItemValid(slot, stack);
//            };
//        }
//    };
//
//    private final NEnergyStorage ENERGY_STORAGE = new NEnergyStorage(60_000, 2000) {
//        @Override
//        public void onEnergyChange() {
//            setChanged();
//            Messages.sendToClients(new EnergySyncS2CPacket(energy, getBlockPos()));
//        }
//    };
//    private static final int REQUIRED_ENERGY = 32;
//
//    private final FluidTank FLUID_TANK = new FluidTank(64_000) {
//        @Override
//        public boolean isFluidValid(FluidStack stack) {
//            // recipes later
//            return stack.getFluid() == Fluids.WATER;
//        }
//
//        @Override
//        protected void onContentsChanged() {
//            setChanged();
//            if (level.isClientSide()) return;
//            Messages.sendToClients(new FluidSyncS2CPacket(fluid, worldPosition));
//        }
//    };
//
//    public void setFluid(FluidStack stack) {
//        FLUID_TANK.setFluid(stack);
//    }
//
//    public FluidStack getFluidStack() {
//        return FLUID_TANK.getFluid();
//    }
//
//    private LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();
//    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap = Map.of(
//            Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
//            Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1,
//                    (i, s) -> itemHandler.isItemValid(1, s))),
//            Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
//            Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1,
//                    (i, s) -> itemHandler.isItemValid(1, s))),
//            Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 0 || i == 1,
//                    (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s)))
//    );
//
//    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
//    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
//
//    public TestBlockEntity(BlockPos pos, BlockState state) {
//        super(BlockEntities.TEST_BLOCK_ENTITY.get(), pos, state);
//        data = new ContainerData() {
//            @Override
//            public int get(int index) {
//                return switch (index) {
//                    case 0 -> TestBlockEntity.this.progress.get();
//                    case 1 -> TestBlockEntity.this.maxProgress.get();
//                    default -> 0;
//                };
//            }
//
//            @Override
//            public void set(int index, int value) {
//                switch (index) {
//                    case 0 -> TestBlockEntity.this.progress.set(value);
//                    case 1 -> TestBlockEntity.this.maxProgress.set(value);
//                };
//            }
//
//            @Override
//            public int getCount() {
//                return 2;
//            }
//        };
//    }
//
//
//    @Override
//    public @NotNull Component getDisplayName() {
//        return Component.literal("Test Block");
//    }
//
//    @Nullable
//    @Override
//    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory inv, @NotNull Player player) {
//        Messages.sendToClients(new EnergySyncS2CPacket(ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
//        Messages.sendToClients(new FluidSyncS2CPacket(getFluidStack(), worldPosition));
//        return new TestBlockMenu(windowId, inv, this, data);
//    }
//
//    @Override
//    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
//        if (cap == ForgeCapabilities.ENERGY)
//            return lazyEnergyHandler.cast();
//
//        if (cap == ForgeCapabilities.ITEM_HANDLER) {
//            if (side == null)
//                return lazyItemHandler.cast();
//            if (directionWrappedHandlerMap.containsKey(side)) {
//                Direction direction = getBlockState().getValue(TestBlock.FACING);
//
//                if (side == Direction.UP || side == Direction.DOWN)
//                    return directionWrappedHandlerMap.get(side).cast();
//
//                return switch (direction) {
//                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
//                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
//                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
//                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
//                };
//            }
//        }
//
//        if (cap == ForgeCapabilities.FLUID_HANDLER) {
//            return lazyFluidHandler.cast();
//        }
//
//        return super.getCapability(cap, side);
//    }
//
//    @Override
//    public void onLoad() {
//        super.onLoad();
//        lazyItemHandler = LazyOptional.of(() -> itemHandler);
//        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
//        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
//    }
//
//    @Override
//    public void invalidateCaps() {
//        super.invalidateCaps();
//        lazyItemHandler.invalidate();
//        lazyEnergyHandler.invalidate();
//        lazyFluidHandler.invalidate();
//    }
//
//    @Override
//    protected void saveAdditional(@NotNull CompoundTag nbt) {
//        nbt.put("inventory", itemHandler.serializeNBT());
//        nbt.putInt("test_block.progress", progress.get());
//        nbt.putInt("test_block.energy", ENERGY_STORAGE.getEnergyStored());
//        nbt = FLUID_TANK.writeToNBT(nbt);
//        super.saveAdditional(nbt);
//    }
//
//    @Override
//    public void load(@NotNull CompoundTag nbt) {
//        super.load(nbt);
//        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
//        progress.set(nbt.getInt("test_block.progress"));
//        ENERGY_STORAGE.setEnergy(nbt.getInt("test_block.energy"));
//        FLUID_TANK.readFromNBT(nbt);
//    }
//
//    public void drops() {
//        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
//        for (int i = 0; i < itemHandler.getSlots(); i++)
//            inv.setItem(i, itemHandler.getStackInSlot(i));
//        Containers.dropContents(Objects.requireNonNull(level), worldPosition, inv);
//    }
//
//    private static boolean hasEnoughEnergy(TestBlockEntity e) {
//        return e.ENERGY_STORAGE.getEnergyStored() >= e.getRecipe(e).getRequiredEnergy();
//    }
//
//    public static void tick(Level level, BlockPos pos, BlockState state, TestBlockEntity e) {
//        if (level.isClientSide()) return;
//        setChanged(level, pos, state); // maybe remove later
//
//        if (hasRecipe(e) && hasEnoughEnergy(e) && hasEnoughFluid(e)) {
//            e.progress.addAndGet(1);
//            setChanged(level, pos, state);
//            if (e.progress.compare(e.maxProgress)) {
//                craft(e);
//                e.progress.set(0);
//                setChanged(level, pos, state);
//            }
//        } else {
//            e.progress.set(0);
//            setChanged(level, pos, state);
//        }
//
//        if (hasFluidItemInSourceSlot(e)) {
//            transferFluidToTank(e);
//        }
//    }
//
//    private static boolean hasEnoughFluid(TestBlockEntity e) {
//        return e.FLUID_TANK.getFluidAmount() >= e.getRecipe(e).getFluid().getAmount();
//    }
//
//    private static void transferFluidToTank(TestBlockEntity e) {
//        e.itemHandler.getStackInSlot(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
//            int drainAmount = Math.min(e.FLUID_TANK.getSpace(), 1000);
//            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
//            if (e.FLUID_TANK.isFluidValid(stack)) {
//                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
//                fillTank(e, stack, handler.getContainer());
//            }
//        });
//    }
//
//    private static void fillTank(TestBlockEntity e, FluidStack stack, ItemStack container) {
//        e.FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);
//        e.itemHandler.extractItem(0, 1, false);
//        e.itemHandler.insertItem(0, container, false);
//    }
//
//    private static boolean hasFluidItemInSourceSlot(TestBlockEntity e) {
//        return e.itemHandler.getStackInSlot(0).getCount() > 0;
//    }
//
//    private static void craft(TestBlockEntity e) {
//        if (!hasRecipe(e)) return;
//
//        Level level = e.level;
//        SimpleContainer inv = new SimpleContainer(e.itemHandler.getSlots());
//        for (int i = 0; i < e.itemHandler.getSlots(); i++)
//            inv.setItem(i, e.itemHandler.getStackInSlot(i));
//
//        Optional<TestBlockRecipe> recipe = Objects.requireNonNull(level).getRecipeManager().getRecipeFor(TestBlockRecipe.Type.INSTANCE,
//                inv, level);
//
//        e.FLUID_TANK.drain(recipe.get().getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
//        e.ENERGY_STORAGE.extractEnergy(recipe.get().getRequiredEnergy(), false);
//        e.itemHandler.extractItem(1, 1, false);
//        e.itemHandler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem(),
//                e.itemHandler.getStackInSlot(2).getCount() + 1));
//    }
//
//    private static boolean hasRecipe(TestBlockEntity e) {
//        Level level = e.level;
//        SimpleContainer inv = new SimpleContainer(e.itemHandler.getSlots());
//        for (int i = 0; i < e.itemHandler.getSlots(); i++)
//            inv.setItem(i, e.itemHandler.getStackInSlot(i));
//
//        System.out.println(inv.getItem(1));
//
//        Optional<TestBlockRecipe> recipe = Objects.requireNonNull(level).getRecipeManager().getRecipeFor(TestBlockRecipe.Type.INSTANCE,
//                inv, level);
//
//        return recipe.isPresent() && canMoveItem(inv, recipe.get().getResultItem());
//    }
//
//    private TestBlockRecipe getRecipe(TestBlockEntity e) {
//        Level level = e.level;
//        SimpleContainer inv = new SimpleContainer(e.itemHandler.getSlots());
//        for (int i = 0; i < e.itemHandler.getSlots(); i++)
//            inv.setItem(i, e.itemHandler.getStackInSlot(i));
//
//        Optional<TestBlockRecipe> recipe = Objects.requireNonNull(level).getRecipeManager().getRecipeFor(TestBlockRecipe.Type.INSTANCE,
//                inv, level);
//        return recipe.orElse(null);
//    }
//
//    private static boolean canMoveItem(SimpleContainer inv, ItemStack item) {
//        return inv.getItem(2).getMaxStackSize() > inv.getItem(2).getCount() && (
//                inv.getItem(2).getItem() == item.getItem() || inv.getItem(2).isEmpty());
//    }
//
//    public IEnergyStorage getStorage() {
//        return ENERGY_STORAGE;
//    }
//
//    public void setEnergyLevel(int energy) {
//        ENERGY_STORAGE.setEnergy(energy);
//    }
//}
