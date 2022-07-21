package com.meteor.eldenmessage.network;

import com.meteor.eldenmessage.common.entity.EntityMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class PacketDeleteMessage {

    private int messageID;

    public PacketDeleteMessage(int messageID){
        this.messageID = messageID;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(messageID);
    }

    public static PacketDeleteMessage decode(FriendlyByteBuf buf) {
        return new PacketDeleteMessage(buf.readInt());
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            Entity e = player.level.getEntity(this.messageID);
            if(e instanceof EntityMessage){
                e.setRemoved(Entity.RemovalReason.KILLED);
            }
        });
    }
}
