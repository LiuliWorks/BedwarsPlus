package cn.ricoco.bedwarsplus.entity;

import cn.nukkit.entity.EntitySmite;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.route.WalkerRouteFinder;

public class IronGolem extends EntityGuarder implements EntitySmite {

    public IronGolem(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.route = new WalkerRouteFinder(this);
        this.setNameTagAlwaysVisible();
    }

    @Override
    public float getWidth() {
        return 1.4f;
    }

    @Override
    public float getHeight() {
        return 2.7f;
    }

    @Override
    public double getSpeed() {
        return 0.8;
    }

    @Override
    public void initEntity() {
        NETWORK_ID=20;
        this.setMaxHealth(20);
        super.initEntity();
        this.setDamage(new float[] { 0, 21, 21, 21 });
        this.setMinDamage(new float[] { 0, 7, 7, 7 });
    }

    @Override
    public String getName() {
        return "BW+_IronGolem";
    }
}
