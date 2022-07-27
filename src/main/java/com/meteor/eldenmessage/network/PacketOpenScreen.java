package com.meteor.eldenmessage.network;

import com.meteor.eldenmessage.client.screen.ScreenMessageInput;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class PacketOpenScreen {

    public PacketOpenScreen(){

    }

    public void encode(FriendlyByteBuf buf) {

    }

    public static PacketOpenScreen decode(FriendlyByteBuf buf) {
        return new PacketOpenScreen();
    }

    public static class Handler {
        public static void handle(PacketOpenScreen packet) {
            Minecraft.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            Minecraft.getInstance().setScreen(new ScreenMessageInput());
                        }
                    }
            );
        }
    }
}
