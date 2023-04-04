package de.neariyeveryone.techniq.block.basic_furnace_generator;

import com.google.common.collect.Maps;
import de.neariyeveryone.techniq.block.BlockEntities;
//import de.neariyeveryone.techniq.networking.Messages;
//import de.neariyeveryone.techniq.networking.packet.EnergySyncS2CPacket;
import de.neariyeveryone.utilities.NAtomicInteger;
import de.neariyeveryone.utilities.NBlockEntity;
import de.neariyeveryone.utilities.NEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class BasicFurnaceGeneratorBlockEntity extends NBlockEntity implements MenuProvider {
    private final NAtomicInteger progress = new NAtomicInteger(0);
    private final NAtomicInteger maxProgress = new NAtomicInteger(200);

    private LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return getFuel().containsKey(stack.getItem());
        }
    };
    private final NEnergyStorage ENERGY_STORAGE = new NEnergyStorage(60_000, 100_000, 2000) {
        @Override
        public void onEnergyChange() {
            setChanged();
//            Messages.sendToClients(new EnergySyncS2CPacket(energy, getBlockPos()));
        }
    };

    public BasicFurnaceGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.BASIC_FURNACE_GENERATOR_ENTITY.get(), pos, state);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress.get();
                    case 1 -> maxProgress.get();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress.set(value);
                    case 1 -> maxProgress.set(value);
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Basic Furnace Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory inv, @NotNull Player player) {
//        Messages.sendToClients(new EnergySyncS2CPacket(ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
        return new BasicFurnaceGeneratorMenu(windowId, inv, this, data);
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
        nbt.putInt("basic_furnace_generator.progress", progress.get());
        nbt.putInt("basic_furnace_generator.energy", ENERGY_STORAGE.getEnergyStored());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress.set(nbt.getInt("basic_furnace_generator.progress"));
        ENERGY_STORAGE.setEnergy(nbt.getInt("basic_furnace_generator.energy"));
    }

    public void drops() {
        var inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++)
            inv.setItem(i, itemHandler.getStackInSlot(i));
        Containers.dropContents(Objects.requireNonNull(level), worldPosition, inv);
    }

    private static boolean hasFuel(BasicFurnaceGeneratorBlockEntity e) {
        return getFuel().containsKey(e.itemHandler.getStackInSlot(0).getItem());
    }

    private static void craft(BasicFurnaceGeneratorBlockEntity e) {
        e.ENERGY_STORAGE.receiveEnergy(getFuel().get(e.itemHandler.getStackInSlot(0).getItem()) * 2, false);
        if (e.itemHandler.getStackInSlot(0).getItem() == Items.LAVA_BUCKET)
            e.itemHandler.setStackInSlot(0, new ItemStack(Items.BUCKET, 1));
        else
            e.itemHandler.extractItem(0, 1, false);
    }

    private static void add(Map<Item, Integer> map, TagKey<Item> tag, int burnTime) {
        for(Holder<Item> holder : Registry.ITEM.getTagOrEmpty(tag))
            map.put(holder.value(), burnTime);
    }

    private static void add(Map<Item, Integer> map, ItemLike item, int burnTime) {
        map.put(item.asItem(), burnTime);
    }

    public static Map<Item, Integer> getFuel() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();
        add(map, Items.LAVA_BUCKET, 20000);
        add(map, Blocks.COAL_BLOCK, 16000);
        add(map, Items.BLAZE_ROD, 2400);
        add(map, Items.COAL, 1600);
        add(map, Items.CHARCOAL, 1600);
        add(map, ItemTags.LOGS, 300);
        add(map, ItemTags.PLANKS, 300);
        add(map, ItemTags.WOODEN_STAIRS, 300);
        add(map, ItemTags.WOODEN_SLABS, 150);
        add(map, ItemTags.WOODEN_TRAPDOORS, 300);
        add(map, ItemTags.WOODEN_PRESSURE_PLATES, 300);
        add(map, Blocks.OAK_FENCE, 300);
        add(map, Blocks.BIRCH_FENCE, 300);
        add(map, Blocks.SPRUCE_FENCE, 300);
        add(map, Blocks.JUNGLE_FENCE, 300);
        add(map, Blocks.DARK_OAK_FENCE, 300);
        add(map, Blocks.ACACIA_FENCE, 300);
        add(map, Blocks.OAK_FENCE_GATE, 300);
        add(map, Blocks.BIRCH_FENCE_GATE, 300);
        add(map, Blocks.SPRUCE_FENCE_GATE, 300);
        add(map, Blocks.JUNGLE_FENCE_GATE, 300);
        add(map, Blocks.DARK_OAK_FENCE_GATE, 300);
        add(map, Blocks.ACACIA_FENCE_GATE, 300);
        add(map, Blocks.NOTE_BLOCK, 300);
        add(map, Blocks.BOOKSHELF, 300);
        add(map, Blocks.LECTERN, 300);
        add(map, Blocks.JUKEBOX, 300);
        add(map, Blocks.CHEST, 300);
        add(map, Blocks.TRAPPED_CHEST, 300);
        add(map, Blocks.CRAFTING_TABLE, 300);
        add(map, Blocks.DAYLIGHT_DETECTOR, 300);
        add(map, ItemTags.BANNERS, 300);
        add(map, Items.BOW, 300);
        add(map, Items.FISHING_ROD, 300);
        add(map, Blocks.LADDER, 300);
        add(map, ItemTags.SIGNS, 200);
        add(map, Items.WOODEN_SHOVEL, 200);
        add(map, Items.WOODEN_SWORD, 200);
        add(map, Items.WOODEN_HOE, 200);
        add(map, Items.WOODEN_AXE, 200);
        add(map, Items.WOODEN_PICKAXE, 200);
        add(map, ItemTags.WOODEN_DOORS, 200);
        add(map, ItemTags.BOATS, 1200);
        add(map, ItemTags.WOOL, 100);
        add(map, ItemTags.WOODEN_BUTTONS, 100);
        add(map, Items.STICK, 100);
        add(map, ItemTags.SAPLINGS, 100);
        add(map, Items.BOWL, 100);
        add(map, Blocks.DRIED_KELP_BLOCK, 4001);
        add(map, Items.CROSSBOW, 300);
        add(map, Blocks.BAMBOO, 50);
        add(map, Blocks.DEAD_BUSH, 100);
        add(map, Blocks.SCAFFOLDING, 400);
        add(map, Blocks.LOOM, 300);
        add(map, Blocks.BARREL, 300);
        add(map, Blocks.CARTOGRAPHY_TABLE, 300);
        add(map, Blocks.FLETCHING_TABLE, 300);
        add(map, Blocks.SMITHING_TABLE, 300);
        add(map, Blocks.COMPOSTER, 300);
        add(map, Blocks.AZALEA, 100);
        add(map, Blocks.FLOWERING_AZALEA, 100);
        return map;
    }

    public IEnergyStorage getStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }

    @Override
    public void setFluid(FluidStack fluid) {

    }

    public static void tick(Level level, BlockPos pos, BlockState state, BasicFurnaceGeneratorBlockEntity e) {
        assert e.level != null;
        if (e.level.isClientSide())
            return;

        if (hasFuel(e)) {
            e.maxProgress.set(getFuel().get(e.itemHandler.getStackInSlot(0).getItem()) / 6);

            if (e.progress.compare(e.maxProgress)) {
                e.progress.set(0);
                craft(e);
                setChanged(level, pos, state);
            }

            e.progress.addAndGet(1);
            setChanged(level, pos, state);
        } else {
            e.progress.set(0);
            setChanged(level, pos, state);
        }
    }
}
