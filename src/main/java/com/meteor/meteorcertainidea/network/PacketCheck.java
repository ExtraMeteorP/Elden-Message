package com.meteor.meteorcertainidea.network;

import com.meteor.meteorcertainidea.client.screen.ScreenMessageCheck;
import com.meteor.meteorcertainidea.common.entity.EntityMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class PacketCheck {

    private int id;
    private boolean hasLike;

    public PacketCheck(int id, boolean hasLike){
        this.id = id;
        this.hasLike = hasLike;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeBoolean(this.hasLike);
    }

    public static PacketCheck decode(FriendlyByteBuf buf) {
        return new PacketCheck(buf.readInt(), buf.readBoolean());
    }

    public static class Handler {
        public static void handle(PacketCheck packet) {
            Minecraft.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            Entity e = Minecraft.getInstance().player.level.getEntity(packet.id);
                            if(e != null && e instanceof EntityMessage){
                                EntityMessage message = (EntityMessage) e;
                                Minecraft.getInstance().setScreen(new ScreenMessageCheck(message, packet.hasLike));
                            }
                        }
                    }
            );
        }
    }

}
