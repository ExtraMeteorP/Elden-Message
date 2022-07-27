package com.meteor.eldenmessage.network;

import com.meteor.eldenmessage.common.entity.EntityMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.compress.utils.Lists;

import java.util.HashMap;
import java.util.List;

public class PacketNotify {

    public PacketNotify(){

    }

    public void encode(FriendlyByteBuf buf) {

    }

    public static PacketNotify decode(FriendlyByteBuf buf) {
        return new PacketNotify();
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            HashMap<Integer, MessageData> tags = new HashMap<>();
            for(Entity e : player.getLevel().getAllEntities()){
                if(e instanceof EntityMessage){
                    EntityMessage message = (EntityMessage) e;
                    tags.put(e.getId(), new MessageData(e.getId(), message.getMessage(), message.getOwnerName(),
                            e.getX(), e.getY(), e.getZ(),
                            message.getLike(), message.getDislike()));
                }
            }
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketSync(tags));
        });
    }
}
