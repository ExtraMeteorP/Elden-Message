package com.meteor.meteorcertainidea.client;

import com.meteor.meteorcertainidea.common.entity.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class ClientHandler {

    public static void setup() {
        registerEntityRenderers();
    }

    private static void registerEntityRenderers() {
        EntityRenderers.register(ModEntities.MESSAGE.get(), RendererMessage::new);
    }

}
