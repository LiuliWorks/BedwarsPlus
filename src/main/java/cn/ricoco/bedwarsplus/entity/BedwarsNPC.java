package cn.ricoco.bedwarsplus.entity;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.passive.EntityNPC;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class BedwarsNPC extends EntityCreature implements EntityNPC {
    public static final int NETWORK_ID = 15;

    public BedwarsNPC(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public String getName() {
        return "BedwarsNPC";
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(20);
    }
}
