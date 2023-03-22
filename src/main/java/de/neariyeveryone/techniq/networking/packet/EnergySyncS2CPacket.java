package de.neariyeveryone.techniq.networking.packet;

import de.neariyeveryone.techniq.block.entity.TestBlockEntity;
import de.neariyeveryone.techniq.screen.TestBlockMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EnergySyncS2CPacket {
    private final int energy;
    private final BlockPos pos;

    public EnergySyncS2CPacket(int energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }

    public EnergySyncS2CPacket(FriendlyByteBuf buffer) {
        this.energy = buffer.readInt();
        this.pos = buffer.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(energy);
        buffer.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            var mc = Minecraft.getInstance();
            if (mc.level.getBlockEntity(pos) instanceof TestBlockEntity e) {
                e.setEnergyLevel(energy);
                if (mc.player.containerMenu instanceof TestBlockMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    e.setEnergyLevel(energy);
                }
            }
        });
        return true;
    }
}
