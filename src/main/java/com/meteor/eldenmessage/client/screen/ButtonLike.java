package com.meteor.eldenmessage.client.screen;

import com.meteor.eldenmessage.lib.LibMisc;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ButtonLike extends Button {

    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/messageicon.png");
    private boolean like;

    public ButtonLike(int i, int j, int w, int h, Component component, OnPress onPress, boolean like) {
        super(i, j, w, h, component, onPress);
        this.like = like;
    }

    @Override
    public void renderButton(PoseStack p_93676_, int p_93677_, int p_93678_, float p_93679_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.isHoveredOrFocused() ? 1 : 0;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(p_93676_, this.x, this.y, like ? 0 : 32, i * 32, 32, 32);
    }

}
