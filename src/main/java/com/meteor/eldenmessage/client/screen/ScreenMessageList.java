package com.meteor.eldenmessage.client.screen;

import com.google.common.collect.Lists;
import com.meteor.eldenmessage.EldenMessage;
import com.meteor.eldenmessage.common.entity.EntityMessage;
import com.meteor.eldenmessage.network.MessageData;
import com.meteor.eldenmessage.network.NetworkHandler;
import com.meteor.eldenmessage.network.PacketDeleteMessage;
import com.meteor.eldenmessage.network.PacketNotify;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;


@OnlyIn(Dist.CLIENT)
public class ScreenMessageList extends Screen {

    private  final int COUNT = 6;

    private final List<ButtonDelete> buttons = Lists.newArrayList();
    private final List<MessageData> dataList = Lists.newArrayList();

    protected Component deleteButton;

    protected Button prevButton;
    protected Button nextButton;

    private int curPage = 0;
    private int maxPage;

    public ScreenMessageList() {
        super(new TranslatableComponent("message_list.title"));
        this.deleteButton = new TranslatableComponent("gui.delete");
    }

    @Override
    public void init() {
        super.init();

        AbstractClientPlayer player = Minecraft.getInstance().player;
        ClientLevel level = player.clientLevel;
        this.buttons.clear();
        this.dataList.clear();

        for(Integer key : EldenMessage.tagsMap.keySet()){
            MessageData mData = EldenMessage.tagsMap.get(key);
            if(player.isCreative() || player.getGameProfile().getName().equals(mData.ownername)){
                dataList.add(mData);
            }
        }

        maxPage = dataList.size()/(COUNT + 1);

        for(int i = 0; i < 6; i++){
            addButtons(this.height/7 + 20 + i * 25, 0);
        }

        prevButton = new ButtonPage(this.width/2 - 80 - 11, this.height/6*5, 22, 22, new TranslatableComponent("gui.prev"), (button) -> {
            pageDown();
        }, true);

        nextButton = new ButtonPage(this.width/2 + 80 - 6, this.height/6*5, 22, 22, new TranslatableComponent("gui.next"), (button) -> {
            pageUp();
        }, false);

        reload();

        this.addRenderableWidget(prevButton);
        this.addRenderableWidget(nextButton);

        updateButton();
    }

    private void pageUp(){
        if(curPage < maxPage){
            curPage++;
        }
        reload();
        updateButton();
    }

    private void pageDown(){
        if(curPage > 0){
            curPage--;
        }
        reload();
        updateButton();
    }

    private void updateButton(){
        prevButton.visible = curPage != 0;
        nextButton.visible = curPage != maxPage;
    }

    private void reload(){
        for(int i = 0; i < COUNT; i++){
            ButtonDelete button = buttons.get(i);
            if(curPage * COUNT + i >= dataList.size()){
                button.visible = false;
                continue;
            }
            button.visible = true;
            button.setID(curPage * COUNT + i);
        }
    }

    protected void addButtons(int j, int id) {
        this.addButton(new ButtonDelete(this.width/2 + 120, j, 20, 20, this.deleteButton, (button) -> {
            if(button instanceof ButtonDelete){
                ButtonDelete b = (ButtonDelete) button;

                NetworkHandler.CHANNEL.sendToServer(new PacketDeleteMessage(dataList.get(b.getID()).id));
                dataList.remove(b.getID());

                maxPage = dataList.size() / (COUNT + 1);
                curPage = Math.min(curPage, maxPage);
                reload();
                updateButton();
            }
        }, id));
    }

    protected void addButton(ButtonDelete button) {
        this.buttons.add(this.addRenderableWidget(button));
    }


    @Override
    public boolean keyPressed(int p_96552_, int p_96553_, int p_96554_) {
        if ((p_96552_ == GLFW.GLFW_KEY_E || p_96552_ == 256) && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }
        return super.keyPressed(p_96552_,p_96553_,p_96554_);
    }

    @Override
    public void render(PoseStack poseStack, int mx, int my, float pTicks) {
        this.fillGradient(poseStack, this.width/6, this.height/6, this.width - this.width/6, this.height - this.height/7 + 15, -1072689136, -804253680);
        for(int i = 0; i < buttons.size(); i++){
            ButtonDelete button = buttons.get(i);
            if(!button.visible)
                continue;

            int id = button.getID();
            MessageData tag = dataList.get(id);
            if(tag != null){
                String message = tag.content;
                String owner_name = tag.ownername;
                int like = tag.like;
                int dislike = tag.dislike;
                double x = tag.x;
                double y = tag.y;
                double z = tag.z;
                this.drawString(poseStack, this.font, message, this.width/6 + 12, button.y, 16777215);
                this.drawString(poseStack, this.font,
                        String.format("x:%.2f y:%.2f z:%.2f  Owner:%s L:%d D:%d",
                                x,y,z,
                                owner_name, like, dislike),
                        this.width/6 + 12, button.y + 12, 16777215);
            }
        }
        this.drawString(poseStack, this.font, String.format("%d / %d", curPage + 1, maxPage + 1), this.width/2 - 12, this.height/6*5 + 5, 16777215);
        super.render(poseStack, mx, my, pTicks);
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public Font getFontRenderer()
    {
        return font;
    }

}
