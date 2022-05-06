package com.meteor.meteorcertainidea.network;

import com.meteor.meteorcertainidea.common.entity.EntityMessage;
import com.meteor.meteorcertainidea.lib.LibWords;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class PacketLeaveMessage {

    private double x;
    private double y;
    private double z;
    private String message;
    private UUID owner;

    public PacketLeaveMessage(double x, double y, double z, String message, UUID owner){
        this.x = x;
        this.y = y;
        this.z = z;
        this.message = message;
        this.owner = owner;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeUtf(message);
        buf.writeUUID(owner);
    }

    public static PacketLeaveMessage decode(FriendlyByteBuf buf) {
        return new PacketLeaveMessage(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUtf(), buf.readUUID());
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            EntityMessage e = new EntityMessage(player.getLevel());
            char[] name = new char[7];
            for(int i = 0; i < name.length; i++){
                name[i] = LibWords.charset.charAt(player.getLevel().random.nextInt(26));
            }
            e.setAppearance(String.copyValueOf(name));
            e.setPos(x,y,z);
            e.setRotation(player.getXRot());
            e.setYRot(player.getYRot());
            e.setOwnerUUID(owner);
            e.setMessage(message);
            player.getLevel().addFreshEntity(e);
        });
    }

}
