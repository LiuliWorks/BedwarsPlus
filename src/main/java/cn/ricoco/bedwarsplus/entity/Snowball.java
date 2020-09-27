package cn.ricoco.bedwarsplus.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.ricoco.funframework.game.Room;

public class Snowball extends EntityProjectile {
    public static final int NETWORK_ID = 81;

    public String teamName="";
    public String teamColor="";
    public Room room=null;
    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }

    @Override
    protected float getGravity() {
        return 0.03f;
    }

    @Override
    protected float getDrag() {
        return 0.01f;
    }

    public Snowball(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public Snowball(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        this.timing.startTiming();
        boolean hasUpdate = super.onUpdate(currentTick);
        if (this.age > 1200 || this.isCollided) {
            Position pos=this.getPosition();
            Entity k = Entity.createEntity("Bedwars+_Silverfish", pos.level.getChunk(((int) pos.x) >> 4, ((int) pos.z) >> 4), Entity.getDefaultNBT(new Vector3(pos.x, pos.y, pos.z)));
            k.spawnToAll();
            if(k instanceof Silverfish){
                Silverfish silverfish=(Silverfish)k;
                silverfish.teamColor=teamColor;
                silverfish.teamName=teamName;
                silverfish.room=room;
            }
            this.kill();
            hasUpdate = true;
        }
        this.timing.stopTiming();
        return hasUpdate;
    }
}