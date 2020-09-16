package cn.ricoco.bedwarsplus.entity;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.Task;
import cn.ricoco.bedwarsplus.Main;


public class BridgeEgg extends EntityProjectile {
    public static final int NETWORK_ID = 82;
    public Block block=Block.get(1, 0);
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
        return 0.02f;
    }

    @Override
    protected float getDrag() {
        return 0.01f;
    }

    public BridgeEgg(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public BridgeEgg(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }
    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        boolean hasUpdate = super.onUpdate(currentTick);
        Vector3 vector = this.getDirectionVector();
        double x = (vector.getX() > 0.0D) ? vector.getX() : -vector.getX();
        double y = (vector.getY() > 0.0D) ? vector.getY() : -vector.getY();
        double z = (vector.getZ() > 0.0D) ? vector.getZ() : -vector.getZ();
        Position pos=this.getPosition();
        if (this.age>1200 || pos.getLevelBlock().getId()!=0) {
            this.kill();
            hasUpdate = true;
        }
        pos.level.addSound(pos, Sound.BUBBLE_POP);
        Main.plugin.getServer().getScheduler().scheduleDelayedTask(new BlockPlaceTask(x,y,z,pos,block),5).getTaskId();
        return hasUpdate;
    }
}
class BlockPlaceTask extends Task {
    public Position pos;
    public Block block;
    double x,y,z;
    public BlockPlaceTask(double x,double y,double z,Position pos,Block block){
        this.x=x;
        this.y=y;
        this.z=z;
        this.block=block;
        this.pos=pos;
    }
    private void setBlock(Vector3 vector3, Block block, Level level){
        level.setBlock(vector3,block);
    }
    @Override
    public void onRun(int i) {
        setBlock(new Vector3(pos.x + 0, pos.y + 0, pos.z + 0), this.block,pos.level);
        if (y < x || y < z) {
            setBlock(new Vector3(pos.x + -1, pos.y + 0, pos.z + -1), this.block,pos.level);
            setBlock(new Vector3(pos.x + -1, pos.y + 0, pos.z + 0), this.block,pos.level);
            setBlock(new Vector3(pos.x + 0, pos.y + 0, pos.z + -1), this.block,pos.level);
        } else {
            setBlock(new Vector3(pos.x + 0, pos.y + 1, pos.z + 0), this.block,pos.level);
            setBlock(new Vector3(pos.x + -1, pos.y + 1, pos.z + -1), this.block,pos.level);
            setBlock(new Vector3(pos.x + -1, pos.y + 1, pos.z + 0), this.block,pos.level);
            setBlock(new Vector3(pos.x + 0, pos.y + 1, pos.z + -1), this.block,pos.level);
            setBlock(new Vector3(pos.x + -1, pos.y + 0, pos.z + -1), this.block,pos.level);
            setBlock(new Vector3(pos.x + -1, pos.y + 0, pos.z + 0), this.block,pos.level);
            setBlock(new Vector3(pos.x + 0, pos.y + 0, pos.z + -1), this.block,pos.level);
        }
    }
}

