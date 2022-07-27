package com.meteor.eldenmessage.network;

import com.meteor.eldenmessage.common.entity.EntityMessage;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityAccess;

import java.util.UUID;

public class PacketLike {

    private int id;
    private boolean like;
    private String name;

    public PacketLike(int id, boolean like, String name){
        this.id = id;
        this.like = like;
        this.name = name;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeBoolean(this.like);
        buf.writeUtf(name);
    }

    public static PacketLike decode(FriendlyByteBuf buf) {
        return new PacketLike(buf.readInt(), buf.readBoolean(), buf.readUtf());
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {

            Entity e = player.level.getEntity(this.id);

            if(e != null && e instanceof EntityMessage){
                EntityMessage message = (EntityMessage) e;
                if(like){
                    message.addLike(name);
                    UUID owner = message.getOwnerUUID();
                    if(owner != null){
                        Player p = e.getLevel().getPlayerByUUID(owner);
                        if(p != null && p != player){
                            p.heal(5f);
                            p.sendMessage(new TranslatableComponent("eldenmessage.recievelike"), Util.NIL_UUID);
                        }
                    }
                }else{
                    message.addDislike(name);
                }
            }
        });
    }

}
