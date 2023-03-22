package de.neariyeveryone.techniq.networking;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.networking.packet.EnergySyncS2CPacket;
import de.neariyeveryone.techniq.networking.packet.FluidSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(TechniqConstants.MOD_ID, "messages"),
                () -> "1.0", s -> true, s -> true);

        INSTANCE = net;

        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncS2CPacket::new).encoder(EnergySyncS2CPacket::toBytes)
                .consumer(EnergySyncS2CPacket::handle).add();

        net.messageBuilder(FluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FluidSyncS2CPacket::new).encoder(FluidSyncS2CPacket::toBytes)
                .consumer(FluidSyncS2CPacket::handle).add();
    }

    public static <MSG> void endToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
