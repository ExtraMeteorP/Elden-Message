package com.meteor.meteorcertainidea.common.entity;

import com.meteor.meteorcertainidea.lib.LibMisc;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, LibMisc.MOD_ID);

    public static final RegistryObject<EntityType<EntityMessage>> MESSAGE = register("message", EntityType.Builder.<EntityMessage>of((type, world) -> new EntityMessage(world), MobCategory.MISC)
            .sized(0.6F, 0.1F).fireImmune()
            .clientTrackingRange(4)
            .updateInterval(40));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return REGISTER.register(name, () -> builder.build(name));
    }
}
