package cn.ricoco.funframework.Utils;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;

public class EntityUtils {
    public static Entity spawnEntity(String name, Position pos){
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;
        Level level = pos.level;
        Entity entity = Entity.createEntity(name,level.getChunk(((int)x)>>4,((int)z)>>4),Entity.getDefaultNBT(new Vector3(x,y,z)));
        level.addEntity(entity);
        entity.spawnToAll();
        return entity;
    }
}
