package com.meteor.eldenmessage.client;

import com.meteor.eldenmessage.client.screen.ScreenMessageInput;
import com.meteor.eldenmessage.client.screen.ScreenMessageList;
import com.meteor.eldenmessage.common.item.ModItems;
import com.meteor.eldenmessage.lib.LibMisc;
import com.meteor.eldenmessage.network.NetworkHandler;
import com.meteor.eldenmessage.network.PacketNotify;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientInitializer {

    public static KeyMapping MESSAGE_INPUT;
    public static KeyMapping MESSAGE_LIST;

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent evt) {
        initKeybindings(ClientRegistry::registerKeyBinding);
        var bus = MinecraftForge.EVENT_BUS;
        bus.addListener((InputEvent.KeyInputEvent e) ->{
            Minecraft mc = Minecraft.getInstance();
            if(mc.player == null){
                return;
            }
            if(MESSAGE_INPUT.matches(e.getKey(),e.getScanCode()) && mc.player.getInventory().contains(new ItemStack(ModItems.FINGER.get()))){
                if(mc.screen == null){
                    mc.setScreen(new ScreenMessageInput());
                }
            }
            if(MESSAGE_LIST.matches(e.getKey(), e.getScanCode())){
                if(mc.screen == null){
                    NetworkHandler.CHANNEL.sendToServer(new PacketNotify());
                    mc.setScreen(new ScreenMessageList());
                }
            }
        });
    }

    public static void initKeybindings(Consumer<KeyMapping> consumer) {
        MESSAGE_INPUT = new KeyMapping("key.eldenmessage_message_input", GLFW.GLFW_KEY_C, LibMisc.MOD_ID);
        consumer.accept(MESSAGE_INPUT);
        MESSAGE_LIST = new KeyMapping("key.eldenmessage_message_list", GLFW.GLFW_KEY_L, LibMisc.MOD_ID);
        consumer.accept(MESSAGE_LIST);
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent evt) {
        MiscellaneousModels.INSTANCE.onModelRegister(ForgeModelBakery::addSpecialModel);
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent evt) {
        MiscellaneousModels.INSTANCE.onModelBake(evt.getModelLoader(), evt.getModelRegistry());
    }

}
