package com.meteor.eldenmessage.client.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class WordBox extends EditBox {

    private ScreenMessageInput parent;
    private List<String> msgList;

    public WordBox(Font p_94114_, int p_94115_, int p_94116_, int p_94117_, int p_94118_, Component p_94119_, ScreenMessageInput parent, List<String> msgList) {
        super(p_94114_, p_94115_, p_94116_, p_94117_, p_94118_, p_94119_);
        setEditable(false);
        setFocus(false);
        setCanLoseFocus(true);
        this.parent = parent;
        this.msgList = msgList;
    }

    @Override
    public boolean mouseClicked(double p_94125_, double p_94126_, int p_94127_) {
        boolean flag = p_94125_ >= (double)this.x && p_94125_ < (double)(this.x + this.width) && p_94126_ >= (double)this.y && p_94126_ < (double)(this.y + this.height);
        if(flag){
            if(parent.selectedMsgPart != this){
                parent.selectedMsgPart = this;
                parent.unsorted_wordList = msgList;
                parent.wordList = msgList;
                parent.search.setValue("");
                parent.wordListWidget.refreshList();
                setFocused(true);
            }
            return true;
        }else
        {
            setFocused(false);
            return false;
        }

    }

}
