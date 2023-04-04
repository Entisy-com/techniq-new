package de.neariyeveryone.techniq.networking.packet;

import de.neariyeveryone.utilities.NBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidSyncS2CPacket extends LoginPacket {
    private final FluidStack fluidStack;
    private final BlockPos pos;

    public FluidSyncS2CPacket(FluidStack fluidStack, BlockPos pos) {
        this.fluidStack = fluidStack;
        this.pos = pos;
    }

    public FluidSyncS2CPacket(FriendlyByteBuf buffer) {
        this.fluidStack = buffer.readFluidStack();
        this.pos = buffer.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeFluidStack(fluidStack);
        buffer.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        context.enqueueWork(() -> {
            var mc = Minecraft.getInstance();
            assert mc.level != null;
            if (mc.level.getBlockEntity(pos) instanceof NBlockEntity e) {
                e.setFluid(fluidStack);
            }
        });
        return true;
    }
}
