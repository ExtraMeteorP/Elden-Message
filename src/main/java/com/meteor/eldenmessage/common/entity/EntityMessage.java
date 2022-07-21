package com.meteor.eldenmessage.common.entity;

import com.google.common.collect.Lists;
import com.meteor.eldenmessage.network.NetworkHandler;
import com.meteor.eldenmessage.network.PacketCheck;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EntityMessage extends Entity {

    private static final String TAG_APPEARANCE = "Appearance";
    private static final String TAG_MESSAGE = "Message";
    private static final String TAG_ROTATION = "Rotation";
    private static final String TAG_OWNER_UUID = "Owner";
    private static final String TAG_OWNER_NAME = "Owner_Name";
    private static final String TAG_THUMB_UP = "Thumbup";
    private static final String TAG_THUMB_DOWN = "Thumbdown";
    private static final String TAG_LIST = "List";

    private static final EntityDataAccessor<String> APPEARANCE = SynchedEntityData.defineId(EntityMessage.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> MESSAGE = SynchedEntityData.defineId(EntityMessage.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(EntityMessage.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(EntityMessage.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<String> OWNER_NAME = SynchedEntityData.defineId(EntityMessage.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> THUMB_UP = SynchedEntityData.defineId(EntityMessage.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> THUMB_DOWN = SynchedEntityData.defineId(EntityMessage.class, EntityDataSerializers.INT);

    private final List<String> players = Lists.newArrayList();

    public EntityMessage(EntityType<EntityMessage> type, Level level) {
        super(type, level);
    }

    public EntityMessage(Level level) {
        super(ModEntities.MESSAGE.get(), level);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(APPEARANCE, "");
        this.entityData.define(MESSAGE, "");
        this.entityData.define(ROTATION, 0f);
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(OWNER_NAME, "");
        this.entityData.define(THUMB_UP, 0);
        this.entityData.define(THUMB_DOWN, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setAppearance(tag.getString(TAG_APPEARANCE));
        setMessage(tag.getString(TAG_MESSAGE));
        setRotation(tag.getFloat(TAG_ROTATION));
        if(tag.hasUUID(TAG_OWNER_UUID)){
            UUID uuid = tag.getUUID(TAG_OWNER_UUID);
            if(uuid != null){
                setOwnerUUID(uuid);
            }
        }
        setOwnerName(tag.getString(TAG_OWNER_NAME));
        setLike(tag.getInt(TAG_THUMB_UP));
        setDislike(tag.getInt(TAG_THUMB_DOWN));
        if(tag.contains(TAG_LIST, 9)) {
            ListTag listtag = tag.getList(TAG_LIST, 8);
            this.players.clear();
            for(int i = 0; i < listtag.size(); ++i) {
                this.players.add(listtag.getString(i));
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putString(TAG_APPEARANCE, getAppearance());
        tag.putString(TAG_MESSAGE, getMessage());
        tag.putFloat(TAG_ROTATION, getRotation());
        if(getOwnerUUID() != null){
            tag.putUUID(TAG_OWNER_UUID, getOwnerUUID());
        }
        tag.putString(TAG_OWNER_NAME, getOwnerName());
        tag.putInt(TAG_THUMB_UP, getLike());
        tag.putInt(TAG_THUMB_DOWN, getDislike());
        if(!this.players.isEmpty()){
            ListTag listtag = new ListTag();
            for(String s : players){
                listtag.add(StringTag.valueOf(s));
            }
            tag.put(TAG_LIST, listtag);
        }
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }else {
            if(!level.isClientSide()){
                NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PacketCheck(this.getId(), hasLike(player.getGameProfile().getName())));
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public void addLike(String name){
        if(!hasLike(name)){
            setLike(getLike() + 1);
            this.players.add(name);
        }
    }

    public void addDislike(String name){
        if(!hasLike(name)){
            setDislike(getDislike() + 1);
            this.players.add(name);
        }
    }

    public boolean hasLike(String name){
        return players.contains(name);
    }

    public String getAppearance() {
        return this.entityData.get(MESSAGE);
    }

    public void setAppearance(String s) {
        this.entityData.set(APPEARANCE, s);
    }

    public String getMessage() {
        return this.entityData.get(MESSAGE);
    }

    public void setMessage(String message) {
        this.entityData.set(MESSAGE, message);
    }

    public float getRotation(){
        return this.entityData.get(ROTATION);
    }

    public void setRotation(float f){
        this.entityData.set(ROTATION, f);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse((UUID)null);
    }

    public void setOwnerUUID(@Nullable UUID p_21817_) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(p_21817_));
    }

    public String getOwnerName() {
        return this.entityData.get(OWNER_NAME);
    }

    public void setOwnerName(String message) {
        this.entityData.set(OWNER_NAME, message);
    }

    public int getLike() {
        return this.entityData.get(THUMB_UP);
    }

    public void setLike(int i){
        this.entityData.set(THUMB_UP, i);
    }

    public int getDislike() {
        return this.entityData.get(THUMB_DOWN);
    }

    public void setDislike(int i){
        this.entityData.set(THUMB_DOWN, i);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
