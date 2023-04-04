//package de.neariyeveryone.techniq.block.compressor;
//
//import de.neariyeveryone.techniq.block.BlockEntities;
//import de.neariyeveryone.techniq.block.metal_press.MetalPressBlock;
////import de.neariyeveryone.techniq.networking.Messages;
////import de.neariyeveryone.techniq.networking.packet.EnergySyncS2CPacket;
//import de.neariyeveryone.techniq.recipe.RecipeTypes;
//import de.neariyeveryone.techniq.recipe.CompressorRecipe;
//import de.neariyeveryone.utilities.NAtomicInteger;
//import de.neariyeveryone.utilities.NBlockEntity;
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
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ForgeCapabilities;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.energy.IEnergyStorage;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.items.ItemStackHandler;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class CompressorBlockEntity extends NBlockEntity implements MenuProvider {
//    private final NAtomicInteger progress = new NAtomicInteger(0);
//    private final NAtomicInteger maxProgress = new NAtomicInteger(200);
//
//    private LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();
//    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
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
//                case 0 -> match(stack.getItem(), 0);
//                case 1 -> match(stack.getItem(), 1);
//                case 2 -> false;
//                default -> super.isItemValid(slot, stack);
//            };
//        }
//    };
//    private final NEnergyStorage ENERGY_STORAGE = new NEnergyStorage(60_000, 250, 2000) {
//        @Override
//        public void onEnergyChange() {
//            setChanged();
////            Messages.sendToClients(new EnergySyncS2CPacket(energy, getBlockPos()));
//        }
//    };
//
//    private boolean match(Item toCheck, int slot) {
//        AtomicBoolean toReturn = new AtomicBoolean(false);
//        getRecipes().forEach(recipe -> {
//            if (recipe.getIngredients().get(slot).getItems()[0].getItem() == toCheck)
//                toReturn.set(true);
//        });
//        return toReturn.get();
//    }
//
//    private List<CompressorRecipe> getRecipes() {
//        assert level != null;
//        return level.getRecipeManager().getAllRecipesFor(RecipeTypes.COMPRESSOR_RECIPE_TYPE.get());
//    }
//
//    public CompressorBlockEntity(BlockPos pos, BlockState state) {
//        super(BlockEntities.COMPRESSOR_ENTITY.get(), pos, state);
//        data = new ContainerData() {
//            @Override
//            public int get(int index) {
//                return switch (index) {
//                    case 0 -> progress.get();
//                    case 1 -> maxProgress.get();
//                    default -> 0;
//                };
//            }
//
//            @Override
//            public void set(int index, int value) {
//                switch (index) {
//                    case 0 -> progress.set(value);
//                    case 1 -> maxProgress.set(value);
//                }
//            }
//
//            @Override
//            public int getCount() {
//                return 2;
//            }
//        };
//    }
//
//    @NotNull
//    @Override
//    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
//        if(cap == ForgeCapabilities.ENERGY) {
//            return lazyEnergyHandler.cast();
//        }
//        if(cap == ForgeCapabilities.ITEM_HANDLER){
//            return lazyItemHandler.cast();
//        }
//        return super.getCapability(cap, side);
//    }
//
//    @Override
//    public @NotNull Component getDisplayName() {
//        return Component.literal("Compressor");
//    }
//
//    @Nullable
//    @Override
//    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory inv, @NotNull Player player) {
////        Messages.sendToClients(new EnergySyncS2CPacket(ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
//        return new CompressorMenu(windowId, inv, this, data);
//    }
//
//    @Override
//    public void onLoad() {
//        super.onLoad();
//        lazyItemHandler = LazyOptional.of(() -> itemHandler);
//        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
//    }
//
//    @Override
//    public void invalidateCaps() {
//        super.invalidateCaps();
//        lazyItemHandler.invalidate();
//        lazyEnergyHandler.invalidate();
//    }
//
//    @Override
//    protected void saveAdditional(@NotNull CompoundTag nbt) {
//        nbt.put("inventory", itemHandler.serializeNBT());
//        nbt.putInt("compressor.progress", progress.get());
//        nbt.putInt("compressor.energy", ENERGY_STORAGE.getEnergyStored());
//        super.saveAdditional(nbt);
//    }
//
//    @Override
//    public void load(@NotNull CompoundTag nbt) {
//        super.load(nbt);
//        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
//        progress.set(nbt.getInt("compressor.progress"));
//        ENERGY_STORAGE.setEnergy(nbt.getInt("compressor.energy"));
//    }
//
//    public void drops() {
//        var inv = new SimpleContainer(itemHandler.getSlots());
//        for (int i = 0; i < itemHandler.getSlots(); i++)
//            inv.setItem(i, itemHandler.getStackInSlot(i));
//        Containers.dropContents(Objects.requireNonNull(level), worldPosition, inv);
//    }
//
//    private static boolean hasEnoughEnergy(CompressorBlockEntity e) {
//        if (getRecipe(e) != null)
//            return e.ENERGY_STORAGE.getEnergyStored() >= getRecipe(e).getRequiredEnergy();
//        return false;
//    }
//
//    private static CompressorRecipe getRecipe(CompressorBlockEntity e) {
//        var inv = new SimpleContainer(e.itemHandler.getSlots());
//        for (int i = 0; i < e.itemHandler.getSlots(); i++)
//            inv.setItem(i, e.itemHandler.getStackInSlot(i));
//
//        var recipe = Objects.requireNonNull(e.level).getRecipeManager().getRecipeFor(RecipeTypes.COMPRESSOR_RECIPE_TYPE.get(),
//                inv, e.level);
//        return recipe.orElse(null);
//    }
//
//    private static void craft(CompressorBlockEntity e) {
//        var inv = new SimpleContainer(e.itemHandler.getSlots());
//        for (int i = 0; i < e.itemHandler.getSlots(); i++)
//            inv.setItem(i, e.itemHandler.getStackInSlot(i));
//
//        var recipe = Objects.requireNonNull(e.level).getRecipeManager().getRecipeFor(RecipeTypes.COMPRESSOR_RECIPE_TYPE.get(),
//                inv, e.level);
//
//        e.ENERGY_STORAGE.extractEnergy(recipe.get().getRequiredEnergy(), false);
//        e.itemHandler.extractItem(0, recipe.get().getInput1Count(), false);
//        e.itemHandler.extractItem(1, recipe.get().getInput2Count(), false);
//        e.itemHandler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem(),
//                e.itemHandler.getStackInSlot(2).getCount() + 1));
//    }
//
//    private static boolean hasRecipe(CompressorBlockEntity e) {
//        var level = e.level;
//        var inv = new SimpleContainer(e.itemHandler.getSlots());
//        for (int i = 0; i < e.itemHandler.getSlots(); i++)
//            inv.setItem(i, e.itemHandler.getStackInSlot(i));
//
//        assert level != null;
//        var recipe = level.getRecipeManager().getRecipeFor(RecipeTypes.COMPRESSOR_RECIPE_TYPE.get(),
//                inv, level);
//
//        return recipe.isPresent() && hasEnoughEnergy(e) && canMoveItem(e, inv, recipe.get().getResultItem());
//    }
//
//    private static boolean canMoveItem(CompressorBlockEntity e, SimpleContainer inv, ItemStack item) {
//        return inv.getItem(2).getMaxStackSize() > inv.getItem(2).getCount() + getRecipe(e).getOutputCount() &&
//                (inv.getItem(2).getItem() == item.getItem() || inv.getItem(2).isEmpty());
//    }
//
//    public IEnergyStorage getStorage() {
//        return ENERGY_STORAGE;
//    }
//
//    public void setEnergyLevel(int energy) {
//        ENERGY_STORAGE.setEnergy(energy);
//    }
//
//    @Override
//    public void setFluid(FluidStack fluid) {
//
//    }
//
//    public static void tick(Level level, BlockPos pos, BlockState state, CompressorBlockEntity e) {
//        if (level.isClientSide())
//            return;
//
//        setChanged(level, pos, state);
//
//        if (hasRecipe(e)) {
//            level.setBlock(pos, state.setValue(MetalPressBlock.WORKING, true), 0);
//            e.maxProgress.set(getRecipe(e).getProcessTime());
//
//            if (e.progress.compare(e.maxProgress)) {
//                e.progress.set(0);
//                craft(e);
//                setChanged(level, pos, state);
//            }
//
//            e.progress.addAndGet(1);
//            setChanged(level, pos, state);
//        } else {
//            level.setBlock(pos, state.setValue(MetalPressBlock.WORKING, false), 0);
//            e.progress.set(0);
//            setChanged(level, pos, state);
//        }
//    }
//}
