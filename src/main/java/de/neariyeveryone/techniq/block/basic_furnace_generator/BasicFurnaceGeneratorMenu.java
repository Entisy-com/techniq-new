package de.neariyeveryone.techniq.block.basic_furnace_generator;

import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.techniq.screen.MenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BasicFurnaceGeneratorMenu extends AbstractContainerMenu {
    public BasicFurnaceGeneratorBlockEntity blockEntity;
    private Level level;
    private ContainerData data;

    public BasicFurnaceGeneratorMenu(int windowId, Inventory inv, ContainerLevelAccess access) {
        super(MenuTypes.BASIC_FURNACE_GENERATOR_MENU.get(), windowId);
    }

    public BasicFurnaceGeneratorMenu(int windowId, Inventory inv, FriendlyByteBuf buffer){
        this(windowId, inv, inv.player.level.getBlockEntity(buffer.readBlockPos()), new SimpleContainerData(2));
    }
    public BasicFurnaceGeneratorMenu(int windowId, Inventory inv, BlockEntity entity, ContainerData data) {
        this(windowId, inv, ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos()));
        blockEntity = (BasicFurnaceGeneratorBlockEntity) entity;
        level = inv.player.level;
        this.data = data;

        addInvSlots(inv);
        addHotbarSlots(inv);

        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 74, 36));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        var progress = data.get(0);
        var maxProgress = data.get(1);
        var progressArrowSize = 25;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 1;

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        var sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        var sourceStack = sourceSlot.getItem();
        var copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false) ||
                    !sourceSlot.mayPlace(sourceStack)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level,
                blockEntity.getBlockPos()), player, Blocks.BASIC_FURNACE_GENERATOR.get());
    }

    private void addInvSlots(Inventory inv) {
        for (var i = 0; i < 3; i++)
            for (var j = 0; j < 9; j++)
                addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
    }

    private void addHotbarSlots(Inventory inv) {
        for (var i = 0; i < 9; i++)
            addSlot(new Slot(inv, i, 8 + i * 18, 142));
    }

    public BasicFurnaceGeneratorBlockEntity getBlockEntity() {
        return blockEntity;
    }

}
