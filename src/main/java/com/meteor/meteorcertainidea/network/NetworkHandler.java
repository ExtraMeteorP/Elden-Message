package com.meteor.meteorcertainidea.network;

import com.meteor.meteorcertainidea.lib.LibMisc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NetworkHandler {

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(LibMisc.MOD_ID, "main"),
            () -> "0",
            "0"::equals,
            "0"::equals);

    public static void init() {
        int i = 0;
        CHANNEL.registerMessage(i++, PacketLike.class, PacketLike::encode, PacketLike::decode,
                makeServerBoundHandler(PacketLike::handle));
        CHANNEL.registerMessage(i++, PacketLeaveMessage.class, PacketLeaveMessage::encode, PacketLeaveMessage::decode,
                makeServerBoundHandler(PacketLeaveMessage::handle));

        CHANNEL.registerMessage(i++, PacketCheck.class, PacketCheck::encode, PacketCheck::decode,
                makeClientBoundHandler(PacketCheck.Handler::handle));
        CHANNEL.registerMessage(i++, PacketOpenScreen.class, PacketOpenScreen::encode, PacketOpenScreen::decode,
                makeClientBoundHandler(PacketOpenScreen.Handler::handle));
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeServerBoundHandler(TriConsumer<T, MinecraftServer, ServerPlayer> handler) {
        return (m, ctx) -> {
            handler.accept(m, ctx.get().getSender().getServer(), ctx.get().getSender());
            ctx.get().setPacketHandled(true);
        };
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeClientBoundHandler(Consumer<T> consumer) {
        return (m, ctx) -> {
            consumer.accept(m);
            ctx.get().setPacketHandled(true);
        };
    }

}
