package com.meteor.meteorcertainidea.client.screen;

import com.google.common.collect.Lists;
import com.meteor.meteorcertainidea.common.entity.EntityMessage;
import com.meteor.meteorcertainidea.lib.LibMisc;
import com.meteor.meteorcertainidea.network.NetworkHandler;
import com.meteor.meteorcertainidea.network.PacketLike;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ScreenMessageCheck extends Screen {

    public static final ResourceLocation MESSAGE_LOCATION = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/message.png");

    private EntityMessage entityMessage;
    private boolean hasLike;
    private MultiLineLabel message = MultiLineLabel.EMPTY;
    protected Component likeButton;
    protected Component dislikeButton;

    private final List<Button> buttons = Lists.newArrayList();

    public ScreenMessageCheck(EntityMessage message, boolean hasLike) {
        super(new TranslatableComponent("message_screen.title"));
        this.entityMessage = message;
        this.hasLike = hasLike;
        this.likeButton = new TranslatableComponent("gui.like");
        this.dislikeButton = new TranslatableComponent("gui.dislike");
    }

    public void renderChar(PoseStack poseStack, char c, int x, int y){
        if(c < 'a' || c > 'z'){
            return;
        }
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, MESSAGE_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(poseStack, x, y, (c - 'a') * 8, 0, 8, 8);
    }

    @Override
    public void render(PoseStack poseStack, int p_98827_, int p_98828_, float p_98829_) {
        this.fillGradient(poseStack, this.width/6, this.height/6, this.width - this.width/6, this.height/6 + this.height/3, -1072689136, -804253680);
        String str1 = "meteor";
        String str2 = "present";
        for(int i = 0; i < str1.length(); i++){
            renderChar(poseStack, str1.charAt(i),this.width/6 + 10 + 8 * i, this.height/6 + 40);
        }
        for(int i = 0; i < str2.length(); i++){
            renderChar(poseStack, str2.charAt(i),this.width/6 + 20 + 8 * i, this.height/6 + 52);
        }
        String[] msgs = entityMessage.getMessage().split("/n");
        this.drawString(poseStack, this.font, msgs[0], this.width/6 + 70, this.height/6 + 15, 16777215);
        if(msgs.length > 1){
            this.drawString(poseStack, this.font, msgs[1], this.width/6 + 70, this.height/6 + 35, 16777215);
        }
        this.drawString(poseStack, this.font, "好评 " + String.valueOf(entityMessage.getLike()), this.width / 2 + 40, this.height/6 + 60, 16777215);
        this.drawString(poseStack, this.font, "差评 " + String.valueOf(entityMessage.getDislike()), this.width / 2 + 100, this.height/6 + 60, 16777215);
        super.render(poseStack, p_98827_, p_98828_, p_98829_);
    }

    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.font, new TextComponent("前有希望 /n 正因如此别停下来啊"), this.width - 50);
        int j = this.height/6 + 20;
        this.buttons.clear();
        if(!hasLike){
            this.addButtons(j);
        }
    }

    protected void addButtons(int j) {
        this.addButton(new ButtonLike(this.width/2 + 40, j, 32, 32, this.likeButton, (button) -> {
            NetworkHandler.CHANNEL.sendToServer(new PacketLike(entityMessage.getId(), true, Minecraft.getInstance().player.getGameProfile().getName()));
            buttons.forEach( (b) -> { b.visible = false;});
        }, true));
        this.addButton(new ButtonLike(this.width/2 + 100, j, 32, 32, this.dislikeButton, (button) -> {
            NetworkHandler.CHANNEL.sendToServer(new PacketLike(entityMessage.getId(), false, Minecraft.getInstance().player.getGameProfile().getName()));
            buttons.forEach( (b) -> { b.visible = false;});
        }, false));
    }

    protected void addButton(Button button) {
        this.buttons.add(this.addRenderableWidget(button));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
