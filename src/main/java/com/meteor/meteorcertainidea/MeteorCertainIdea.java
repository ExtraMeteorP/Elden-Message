package com.meteor.meteorcertainidea;

import com.meteor.meteorcertainidea.client.ClientHandler;
import com.meteor.meteorcertainidea.common.CommonHandler;
import com.meteor.meteorcertainidea.common.entity.ModEntities;
import com.meteor.meteorcertainidea.common.item.ModItems;
import com.meteor.meteorcertainidea.lib.LibMisc;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LibMisc.MOD_ID)
public class MeteorCertainIdea {

    public static final Logger LOGGER = LogManager.getLogger(LibMisc.MOD_ID);

    public MeteorCertainIdea(){
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.REGISTER.register(eventBus);
        ModEntities.REGISTER.register(eventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(CommonHandler::setup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ClientHandler::setup);
    }
}
