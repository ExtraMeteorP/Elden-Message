package com.meteor.meteorcertainidea.client;

import com.meteor.meteorcertainidea.lib.LibMisc;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientInitializer {

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent evt) {
        MiscellaneousModels.INSTANCE.onModelRegister(ForgeModelBakery::addSpecialModel);
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent evt) {
        MiscellaneousModels.INSTANCE.onModelBake(evt.getModelLoader(), evt.getModelRegistry());
    }

}
