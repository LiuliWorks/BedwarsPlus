package cn.ricoco.funframework.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;

public class FloatingTextSpawner {
    public static FloatingText spawner(Position pos,String name) {
        Entity k = Entity.createEntity("Bedwars+_FloatingText", pos.level.getChunk(((int) pos.x) >> 4, ((int) pos.z) >> 4), Entity.getDefaultNBT(new Vector3(pos.x, pos.y, pos.z)));
        FloatingText floatingText=(FloatingText) k;
        floatingText.setNameTagVisible(true);
        floatingText.setNameTagAlwaysVisible(true);
        floatingText.setNameTag(name);
        floatingText.setScale(0.0001f);
        floatingText.spawnToAll();
        return floatingText;
    }
}
