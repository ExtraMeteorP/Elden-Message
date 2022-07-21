package com.meteor.eldenmessage.common.item;

import com.meteor.eldenmessage.network.NetworkHandler;
import com.meteor.eldenmessage.network.PacketOpenScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class ItemFinger extends Item {

    public ItemFinger(Properties prop) {
        super(prop);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide()){
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PacketOpenScreen());
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

}
