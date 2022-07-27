package com.meteor.eldenmessage.client.screen;

import com.meteor.eldenmessage.EldenMessage;
import com.meteor.eldenmessage.common.entity.EntityMessage;
import com.meteor.eldenmessage.lib.LibWords;
import com.meteor.eldenmessage.network.NetworkHandler;
import com.meteor.eldenmessage.network.PacketLeaveMessage;
import com.meteor.eldenmessage.network.PacketNotify;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class ScreenMessageInput extends Screen {

    protected WordBox msgPart1;
    protected WordBox msgPart2;
    protected WordBox msgPart3;
    protected WordBox msgPart4;
    protected WordBox msgPart5;

    protected WordBox selectedMsgPart = null;

    protected EditBox search;

    protected WordListWidget wordListWidget;

    protected List<String> unsorted_wordList = new ArrayList<>();
    private String lastFilterText = "";

    protected List<String> wordList = new ArrayList<>();

    private List<String> wordList1 = Arrays.asList(LibWords.words1);
    private List<String> wordList2 = Arrays.asList(LibWords.words2);
    private List<String> wordList3 = Arrays.asList(LibWords.words3);
    private List<String> wordList4 = Arrays.asList(LibWords.words1);
    private List<String> wordList5 = Arrays.asList(LibWords.words2);

    protected ButtonConfirm confirmButton;

    public ScreenMessageInput() {
        super(new TranslatableComponent("message_input.title"));
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
    public void init()
    {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        NetworkHandler.CHANNEL.sendToServer(new PacketNotify());
        
        wordListWidget = new WordListWidget(this, this.width - this.width/5 - (this.width/5 + 175), this.height/7, this.height - this.height/7 + 20);
        wordListWidget.setLeftPos(this.width/5 + 175);

        msgPart1 = new WordBox(getFontRenderer(), this.width/5 + 45, this.height/7 + 15 - 2, 120, 14, new TranslatableComponent("msg.part1"), this, wordList1);
        msgPart2 = new WordBox(getFontRenderer(), this.width/5 + 45, this.height/7 + 35 - 2, 120, 14, new TranslatableComponent("msg.part2"), this, wordList2);
        msgPart3 = new WordBox(getFontRenderer(), this.width/5 + 45, this.height/7 + 65 - 2, 120, 14, new TranslatableComponent("msg.part3"), this, wordList3);
        msgPart4 = new WordBox(getFontRenderer(), this.width/5 + 45, this.height/7 + 95 - 2, 120, 14, new TranslatableComponent("msg.part4"), this, wordList4);
        msgPart5 = new WordBox(getFontRenderer(), this.width/5 + 45, this.height/7 + 115 - 2, 120, 14, new TranslatableComponent("msg.part5"), this, wordList5);

        if(player.isCreative()){
            msgPart1.setEditable(true);
            msgPart2.setEditable(true);
            msgPart3.setEditable(true);
            msgPart4.setEditable(true);
            msgPart5.setEditable(true);
        }

        confirmButton = new ButtonConfirm(this.width/5 + 45, this.height/7 + 150, 32, 32, new TranslatableComponent("gui.confirm"),(button) -> {
            if(canConfirm()){
                int cnt = 0;
                if(!player.isCreative()){
                    for(Integer key : EldenMessage.tagsMap.keySet()){
                        if(player.getGameProfile().getName().equals(EldenMessage.tagsMap.get(key).ownername)){
                            cnt++;
                        }
                    }
                }
                if(cnt >= 5 && !player.isCreative()) {
                    player.sendMessage(new TranslatableComponent("eldenmessage.maxmessage"), Util.NIL_UUID);
                }else{
                    NetworkHandler.CHANNEL.sendToServer(new PacketLeaveMessage(player.getX(), player.getY(), player.getZ(), getMessage(), player.getUUID()));
                    NetworkHandler.CHANNEL.sendToServer(new PacketNotify());
                    Minecraft.getInstance().setScreen(null);
                }
            }
        });

        search = new EditBox(getFontRenderer(), this.width/5 + 45, this.height/7 + 135 - 2, 120, 14, new TranslatableComponent("msg.search"));

        this.addRenderableWidget(wordListWidget);
        this.addRenderableWidget(confirmButton);
        this.addRenderableWidget(search);
        this.addWidget(msgPart1);
        this.addWidget(msgPart2);
        this.addWidget(msgPart3);
        this.addWidget(msgPart4);
        this.addWidget(msgPart5);

        search.setFocus(false);
        search.setCanLoseFocus(true);
    }

    private String getMessage(){
        return msgPart1.getValue().replaceAll("[*][*][*]", msgPart2.getValue())
                + "/n"
                + msgPart3.getValue()
                + msgPart4.getValue().replaceAll("[*][*][*]", msgPart5.getValue());
    }

    private boolean canConfirm() {
        return !msgPart1.getValue().isBlank() && !msgPart2.getValue().isBlank() &&
                msgPart3.getValue().isBlank() && msgPart4.getValue().isBlank() && msgPart5.getValue().isBlank()
                || !msgPart1.getValue().isBlank() && !msgPart2.getValue().isBlank() &&
                !msgPart3.getValue().isBlank() && !msgPart4.getValue().isBlank() && !msgPart5.getValue().isBlank();
    }

    private void updateConfirmButtonStatus() {
        this.confirmButton.active = canConfirm();
    }

    @Override
    public void render(PoseStack poseStack, int mx, int my, float pTicks) {


        this.fillGradient(poseStack, this.width/5, this.height/7, this.width - this.width/5, this.height - this.height/7 + 20, -1072689136, -804253680);
        wordListWidget.render(poseStack, mx, my, pTicks);

        this.drawString(poseStack, this.font, "范本", this.width/5 + 10, this.height/7 + 15, 16777215);
        this.drawString(poseStack, this.font, "单字", this.width/5 + 10, this.height/7 + 35, 16777215);
        this.drawString(poseStack, this.font, "连接词", this.width/5 + 10, this.height/7 + 65, 16777215);
        this.drawString(poseStack, this.font, "范本2", this.width/5 + 10, this.height/7 + 95, 16777215);
        this.drawString(poseStack, this.font, "单字2", this.width/5 + 10, this.height/7 + 115, 16777215);
        this.drawString(poseStack, this.font, "搜索", this.width/5 + 10, this.height/7 + 135, 16777215);
        this.drawString(poseStack, this.font, "写下谏言", this.width/5 + 10, this.height/7 + 160, 16777215);

        msgPart1.render(poseStack, mx, my, pTicks);
        msgPart2.render(poseStack, mx, my, pTicks);
        msgPart3.render(poseStack, mx, my, pTicks);
        msgPart4.render(poseStack, mx, my, pTicks);
        msgPart5.render(poseStack, mx, my, pTicks);

        super.render(poseStack, mx, my, pTicks);


    }

    @Override
    public void tick() {
        this.msgPart1.tick();
        this.msgPart2.tick();
        this.msgPart3.tick();
        this.msgPart4.tick();
        this.msgPart5.tick();
        updateConfirmButtonStatus();

        search.tick();
        if (!search.getValue().equals(lastFilterText))
        {
            reloadWords();
        }
    }

    private void reloadWords()
    {
        this.wordList = this.unsorted_wordList.stream().
                filter(s-> StringUtils.toLowerCase(s).contains(StringUtils.toLowerCase(search.getValue()))).collect(Collectors.toList());
        lastFilterText = search.getValue();
        wordListWidget.refreshList();
    }

    public <T extends ObjectSelectionList.Entry<T>> void buildWordList(Consumer<T> wordListViewConsumer, Function<String, T> newEntry)
    {
        wordList.forEach(word->wordListViewConsumer.accept(newEntry.apply(word)));
    }

    public Font getFontRenderer()
    {
        return font;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
