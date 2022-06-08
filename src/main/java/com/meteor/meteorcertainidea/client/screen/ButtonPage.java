package com.meteor.meteorcertainidea.client.screen;

import com.meteor.meteorcertainidea.lib.LibMisc;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ButtonPage extends Button {

    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/pageicon.png");
    private boolean prev;

    public ButtonPage(int p_93721_, int p_93722_, int p_93723_, int p_93724_, Component p_93725_, OnPress p_93726_, boolean prev) {
        super(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_);
        this.prev = prev;
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
        this.blit(p_93676_, this.x, this.y, prev ? 24 : 0 , i*22, 14, 22);
    }
}
