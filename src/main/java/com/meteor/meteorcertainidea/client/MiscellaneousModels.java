package com.meteor.meteorcertainidea.client;

import com.meteor.meteorcertainidea.lib.LibMisc;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;

public class MiscellaneousModels {

    public static final MiscellaneousModels INSTANCE = new MiscellaneousModels();

    public BakedModel messageModels;

    public void onModelRegister(Consumer<ResourceLocation> consumer) {
        consumer.accept(new ResourceLocation(LibMisc.MOD_ID, "icon/message"));
    }

    public void onModelBake(ModelBakery loader, Map<ResourceLocation, BakedModel> map) {
        messageModels = map.get(new ResourceLocation(LibMisc.MOD_ID, "icon/message"));
    }
}
