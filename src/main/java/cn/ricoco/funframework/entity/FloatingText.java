package cn.ricoco.funframework.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public class FloatingText extends EntityCreature {
    private int NetWorkId = 61;

    public float getWidth() {
        return 0.1F;
    }

    public float getLength() {
        return 0.1F;
    }

    public float getHeight() {
        return 0.1F;
    }

    public FloatingText(FullChunk chunk, CompoundTag nbt){
        super(chunk, nbt);
    }
    @Override
    public int getNetworkId() {
        return this.NetWorkId;
    }
    @Override
    public void spawnTo(Player player){
        if(this.chunk != null && !this.closed){
            super.spawnTo(player);
        }
    }
    @Override
    public void saveNBT() {
        super.saveNBT();
    }
    @Override
    public void close() {
        this.setNameTagVisible(false);
        this.setNameTagAlwaysVisible(false);
        super.close();
    }
    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        return false;
    }
    @Override
    public String getName(){
        return "Bedwars+_FloatingText";
    }
}
