package com.meteor.meteorcertainidea.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class WordListWidget extends ObjectSelectionList<WordListWidget.WordEntry>{

    private final int listWidth;
    private ScreenMessageInput parent;

    public WordListWidget(ScreenMessageInput parent, int listWidth, int top, int bottom)
    {
        super(Minecraft.getInstance(), listWidth, parent.height, top, bottom, parent.getFontRenderer().lineHeight * 2);
        this.parent = parent;
        this.listWidth = listWidth;
        this.setRenderBackground(false);
        this.setRenderTopAndBottom(false);
        this.refreshList();
    }

    @Override
    public void render(PoseStack poseStack, int p_100706_, int p_100707_, float p_100708_) {
        double d0 = this.minecraft.getWindow().getGuiScale();
        RenderSystem.enableScissor((int)((double)(this.getRowLeft()-4) * d0), (int)((double)(this.height - this.y1) * d0), (int)((double)(this.getScrollbarPosition() + 6) * d0), (int)((double)(this.height - (this.height - this.y1) - this.y0) * d0));
        super.render(poseStack, p_100706_, p_100707_, p_100708_);
        RenderSystem.disableScissor();
    }

    @Override
    protected int getScrollbarPosition()
    {
        return parent.width - parent.width/5;
    }

    @Override
    public int getRowWidth()
    {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        parent.buildWordList(this::addEntry, word->new WordListWidget.WordEntry(word, this.parent));
    }

    @Override
    protected void renderBackground(PoseStack poseStack) {

    }

    public class WordEntry extends ObjectSelectionList.Entry<WordListWidget.WordEntry> {
        private final String word;
        private final ScreenMessageInput parent;

        WordEntry(String word, ScreenMessageInput parent) {
            this.word = word;
            this.parent = parent;
        }

        @Override
        public Component getNarration() {
            return new TranslatableComponent("narrator.select", word);
        }

        @Override
        public void render(PoseStack poseStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTick)
        {
            Component name = new TextComponent(word);
            Font font = this.parent.getFontRenderer();
            font.draw(poseStack, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, listWidth))), left + 3, top + 2, 0xFFFFFF);
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_)
        {
            WordListWidget.this.setSelected(this);
            if(parent.selectedMsgPart != null){
                parent.selectedMsgPart.setValue(word);
            }
            return false;
        }

        public String getWord()
        {
            return word;
        }
    }
}
