package com.meteor.meteorcertainidea.common.item;

import com.meteor.meteorcertainidea.lib.LibItemName;
import com.meteor.meteorcertainidea.lib.LibMisc;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, LibMisc.MOD_ID);

    public static final RegistryObject<Item> FINGER = register(LibItemName.TARNISHED_WIZENED_FINGER, () -> new ItemFinger(new Item.Properties().tab(CreativeModeTab.TAB_MISC).rarity(Rarity.UNCOMMON).stacksTo(1).setNoRepair()));

    private static RegistryObject<Item> register(String name, Supplier<Item> item) {
        return REGISTER.register(name, item);
    }
}
