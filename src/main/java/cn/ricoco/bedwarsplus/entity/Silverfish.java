package cn.ricoco.bedwarsplus.entity;

import cn.nukkit.entity.EntityArthropod;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.route.WalkerRouteFinder;

public class Silverfish extends EntityGuarder implements EntityArthropod {

    public Silverfish(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.route = new WalkerRouteFinder(this);
        this.setNameTagAlwaysVisible();
    }

    @Override
    public float getWidth() {
        return 0.4f;
    }

    @Override
    public float getHeight() {
        return 0.3f;
    }

    @Override
    public double getSpeed() {
        return 1.4;
    }

    @Override
    public void initEntity() {
        NETWORK_ID=39;
        super.initEntity();
        this.setMaxHealth(8);
        this.setDamage(new float[] { 0, 1, 1, 1 });
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public String getName() {
        return "BW+_Silverfish";
    }
}
