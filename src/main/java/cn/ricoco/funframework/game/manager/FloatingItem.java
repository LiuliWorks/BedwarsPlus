package cn.ricoco.funframework.game.manager;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.AddItemEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

import java.util.Random;

public class FloatingItem {
    public float x,y,z;
    public Long rand;
    public Item item;
    public FloatingItem(Item item,float x,float y,float z){
        Random random = new Random();
        this.rand=random.nextLong();
        this.x=x;
        this.y=y;
        this.z=z;
        this.item=item;
    }
    public void addFloatingItem(Level level){
        AddItemEntityPacket addItemEntityPacket = new AddItemEntityPacket();
        addItemEntityPacket.speedX = 0f;
        addItemEntityPacket.speedY = 0f;
        addItemEntityPacket.speedZ = 0f;
        addItemEntityPacket.x = this.x;
        addItemEntityPacket.y = this.y;
        addItemEntityPacket.z = this.z;
        addItemEntityPacket.entityUniqueId = this.rand;
        addItemEntityPacket.entityRuntimeId = this.rand;
        addItemEntityPacket.item=this.item;
        long c = 1 << Entity.DATA_FLAG_IMMOBILE;
        addItemEntityPacket.metadata = new EntityMetadata().putLong(Entity.DATA_FLAGS, c).putLong(Entity.DATA_LEAD_HOLDER_EID, -1).putFloat(Entity.DATA_SCALE, 0f);
        level.getPlayers().values().forEach((player -> player.dataPacket(addItemEntityPacket)));
    }
    public void removeFloatingItem(Level level){
        RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
        removeEntityPacket.eid = this.rand;
        level.getPlayers().values().forEach((player -> player.dataPacket(removeEntityPacket)));
    }
}
