package com.meteor.meteorcertainidea.client;

import com.meteor.meteorcertainidea.common.entity.EntityMessage;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Random;

public class RendererMessage extends EntityRenderer<EntityMessage> {

    public RendererMessage(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public void render(EntityMessage e, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
        super.render(e, yaw, partialTicks, ms, buffers, light);
        ms.pushPose();

        double time = (e.level.getGameTime() % 24000) + partialTicks + new Random(e.getId()).nextInt(200);
        float a = 0.1F + (e.isInvisible() ? 0 : 1) * 0.8F;
        int alpha = (int) ((0.7 + 0.3 * (Math.sin(time / 5.0) + 0.5) * 2) * a * 255.0);
        int iconColor = 0xFFFFFF | (alpha << 24);

        ms.mulPose(Vector3f.YP.rotationDegrees(e.getYRot()));
        ms.mulPose(Vector3f.XP.rotationDegrees(-90F));
        ms.translate(-0.5D, -0.5D, -0.5D);
        BakedModel model = MiscellaneousModels.INSTANCE.messageModels;
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffers.getBuffer(Sheets.translucentItemSheet()), null, model, 1, 1, 1, 0xF000F0, OverlayTexture.NO_OVERLAY);
        ms.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMessage p_114482_) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    @Override
    protected void renderNameTag(EntityMessage entity, Component component, PoseStack stack, MultiBufferSource source, int light) {}
}
