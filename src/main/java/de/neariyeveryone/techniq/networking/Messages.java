package de.neariyeveryone.techniq.networking;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.networking.packet.EnergySyncS2CPacket;
import de.neariyeveryone.techniq.networking.packet.FluidSyncS2CPacket;
import de.neariyeveryone.techniq.networking.packet.LoginPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.regex.Pattern;

@SuppressWarnings("removal")
public class Messages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    public static final String VERSION = "teq-net-14";
    private static final Pattern NET_VERSION_PATTERN = Pattern.compile("teq-net-\\d+$");
    private static final Pattern MOD_VERSION_PATTERN = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");

    private static int id() {
        return packetId++;
    }

    public static void register() {
//        var net = NetworkRegistry.ChannelBuilder.named(TechniqConstants.getId("network"))
//                .clientAcceptedVersions(s -> Objects.equals(s, VERSION))
//                .serverAcceptedVersions(s -> Objects.equals(s, VERSION))
//                .networkProtocolVersion(() -> VERSION)
//                .simpleChannel();
//
//        INSTANCE = net;
//
//        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
//                .decoder(EnergySyncS2CPacket::new)
//                .encoder(EnergySyncS2CPacket::toBytes)
////                .consumer(HandshakeHandler.biConsumerFor((hh, msg, ctx) -> {
////                    INSTANCE.reply(new LoginPacket.Reply(), ctx.get());
////                }))
//                .consumerNetworkThread(EnergySyncS2CPacket::handle)
//                .markAsLoginPacket()
//                .add();
//
//        net.messageBuilder(FluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
//                .decoder(FluidSyncS2CPacket::new)
//                .encoder(FluidSyncS2CPacket::toBytes)
////                .consumer(HandshakeHandler.biConsumerFor((hh, msg, ctx) -> {
////                    INSTANCE.reply(new LoginPacket.Reply(), ctx.get());
////                }))
//                .consumerNetworkThread(FluidSyncS2CPacket::handle)
//                .markAsLoginPacket()
//                .add();
    }

//    public static <MSG> void sendToServer(MSG message) {
//        INSTANCE.sendToServer(message);
//    }
//
//    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
//        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
//    }
//
//    public static <MSG> void sendToClients(MSG message) {
//        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
//    }
}
