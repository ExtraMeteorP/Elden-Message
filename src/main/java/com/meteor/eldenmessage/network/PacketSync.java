package com.meteor.eldenmessage.network;

import com.meteor.eldenmessage.EldenMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;

public class PacketSync {

    private HashMap<Integer, MessageData> data;

    public PacketSync(HashMap<Integer, MessageData> data){
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        for(Integer key : data.keySet()){
            buf.writeInt(key);
            MessageData mData = data.get(key);
            buf.writeInt(mData.id);
            buf.writeUtf(mData.content);
            buf.writeUtf(mData.ownername);
            buf.writeDouble(mData.x);
            buf.writeDouble(mData.y);
            buf.writeDouble(mData.z);
            buf.writeInt(mData.like);
            buf.writeInt(mData.dislike);
        }
    }

    public static PacketSync decode(FriendlyByteBuf buf) {
        HashMap<Integer, MessageData> tags = new HashMap<>();
        int length = buf.readInt();
        for(int i = 0; i < length; i++){
            int key = buf.readInt();
            int id = buf.readInt();
            String content = buf.readUtf();
            String ownername = buf.readUtf();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            int like = buf.readInt();
            int dislike = buf.readInt();
            MessageData mData = new MessageData(id, content, ownername, x, y, z, like, dislike);
            tags.put(key, mData);
        }
        return new PacketSync(tags);
    }

    public static class Handler {
        public static void handle(PacketSync packet) {
            Minecraft.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            EldenMessage.tagsMap = packet.data;
                        }
                    }
            );
        }
    }

}
