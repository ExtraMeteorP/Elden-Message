package com.meteor.eldenmessage.network;

public class MessageData {

    public int id;
    public String content;
    public String ownername;
    public double x;
    public double y;
    public double z;
    public int like;
    public int dislike;

    public MessageData(int id, String content, String ownername, double x, double y, double z, int like, int dislike){
        this.id = id;
        this.content = content;
        this.ownername = ownername;
        this.x = x;
        this.y = y;
        this.z = z;
        this.like = like;
        this.dislike = dislike;
    }

}
